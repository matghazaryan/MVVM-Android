package android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.api;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.BuildConfig;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.core.constants.IUrls;
import android.mvvm.mg.com.mvvm_android.core.dialog.MVVMAlertDialog;
import android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.preference.MVVMPrefUtils;

import com.dm.dmnetworking.api_client.base.DMBaseRequest;
import com.dm.dmnetworking.api_client.base.DMBaseTokenHandler;
import com.dm.dmnetworking.api_client.listeners.DMIStatusHandleListener;

import org.json.JSONException;
import org.json.JSONObject;

import alertdialog.dm.com.dmalertdialog.configs.DMBaseDialogConfigs;


public final class MVVMNetworking extends DMBaseRequest {

    private static MVVMNetworking ourInstance;

    private final static String INVALID_DATA = "INVALID_DATA";

    private MVVMNetworking() {
    }

    public static MVVMNetworking getInstance() {
        if (ourInstance == null) {
            ourInstance = new MVVMNetworking();
        }

        return ourInstance;
    }

    @Override
    protected void handleStatuses(final Context context, final int statusCode, final JSONObject jsonObject, final DMIStatusHandleListener listener) {
        try {
            String status = "";
            if (jsonObject != null) {
                status = jsonObject.getString("status");
            }
            switch (status) {
                case INVALID_DATA:
                    listener.onError(status, jsonObject);
                    break;
//                case "REFRESH_TOKEN":
//                    listener.onTokenUpdate();
//                    break;
                default:
                    listener.onComplete(status, jsonObject);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(e.getMessage(), jsonObject);
        }
    }

    @Override
    protected int getRequestTimeOut() {
        return 20000;
    }

    @Override
    protected String getFullUrl(final Context context, final String url) {
        return BuildConfig.BASE_URL + "/" + url;
    }

    @Override
    public String getTagForLogger() {
        return IMVVMConstants.TAG;
    }

    @Override
    public boolean isEnableLogger() {
        return true;
    }

    @Override
    public DMBaseTokenHandler onTokenRefresh() {
        return new DMBaseTokenHandler(IUrls.Method.REFRESH_TOKEN) {

            @Override
            protected void onTokenRefreshed(final Context context, final JSONObject jsonObject) {
                try {
                    final String token = jsonObject.getString("data");
                    MVVMPrefUtils.setToken(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onTokenRefreshFailure(final Context context, final JSONObject jsonObject) {

            }

            @Override
            protected void onNoInternetConnection(final Context context) {
                new MVVMAlertDialog().showWarningDialog(new DMBaseDialogConfigs<>(context)
                        .setContentRes(R.string.dialog_no_internet_connection));

            }
        };
    }
}
