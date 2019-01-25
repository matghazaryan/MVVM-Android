package android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.api;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.BuildConfig;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.core.constants.IUrls;
import android.mvvm.mg.com.mvvm_android.core.dialog.MVVMAlertDialog;
import android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.preference.MVVMPrefUtils;

import com.dm.dmnetworking.DMNetworkBaseRequest;
import com.dm.dmnetworking.DMNetworkBaseTokenHandler;
import com.dm.dmnetworking.DMNetworkIStatusHandleListener;

import org.json.JSONException;
import org.json.JSONObject;

import alertdialog.dm.com.dmalertdialog.DMDialogBaseConfigs;


public final class MVVMNetworking extends DMNetworkBaseRequest {

    private static MVVMNetworking ourInstance;

    private final static String INVALID_DATA = "INVALID_DATA";

    private MVVMNetworking() {
    }

    public static MVVMNetworking getInstance() {
        if (ourInstance == null) {
            synchronized (MVVMNetworking.class) {
                if (ourInstance == null) {
                    ourInstance = new MVVMNetworking();
                }
            }
        }
        return ourInstance;
    }

    @Override
    protected void handleStatuses(final Context context, final int statusCode, final JSONObject jsonObject, final DMNetworkIStatusHandleListener listener) {
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
    public DMNetworkBaseTokenHandler onTokenRefresh() {
        return new DMNetworkBaseTokenHandler(IUrls.Method.REFRESH_TOKEN) {

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
                new MVVMAlertDialog().showWarningDialog(new DMDialogBaseConfigs<>(context)
                        .setContentRes(R.string.dialog_no_internet_connection));
            }
        };
    }

    @Override
    public boolean isEnableFakeJson() {
        return false;
    }

    @Override
    public void onSuccessOrFailureResponseForDebug(final String url, final JSONObject jsonObject, final ResponseType responseType) {

    }

    @Override
    public String getFakeJsonFilePath(final String url) {
        switch (url) {
            case IUrls.Method.CONFIGS:
                return "configs/configs.json";
            case IUrls.Method.LOGIN:
                return "auth/login.json";
            case IUrls.Method.LOGOUT:
                return "auth/logout.json";
            case IUrls.Method.CARDS:
                return "cards/cards.json";
            case IUrls.Method.SAVE_IMAGE:
                return "saveImage/save_image.json";
            case IUrls.Method.TRANSACTION:
                return "transactions/transaction.json";
            case IUrls.Method.TRANSACTION_1:
                return "transactions/transaction_1.json";
            case IUrls.Method.TRANSACTION_2:
                return "transactions/transaction_2.json";
            case IUrls.Method.TRANSACTION_3:
                return "transactions/transaction_3.json";
            default:
                return null;
        }
    }
}
