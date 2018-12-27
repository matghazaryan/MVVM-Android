package android.mvvm.mg.com.mvvm_android.application;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseApplicationConfigs;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.core.dialog.MVVMDialog;

public class MVVMApplicationConfigs extends DMBaseApplicationConfigs {

    private static MVVMApplicationConfigs instance;

    private MVVMApplicationConfigs() {
    }

    public static MVVMApplicationConfigs getInstance() {
        if (instance == null) {
            synchronized (MVVMApplicationConfigs.class) {
                if (instance == null) {
                    instance = new MVVMApplicationConfigs();
                }
            }
        }
        return instance;
    }

    @Override
    public void showErrorDialog(final Context context, final String message) {
        MVVMDialog.showErrorDialog(context, message);
    }

    @Override
    public void showNoInternetDialog(final Context context) {
        MVVMDialog.showNoInternetDialog(context);
    }

    @Override
    public int getGeneralErrorMessage() {
        return R.string.error_general_error;
    }

    @Override
    public int getNoInternetMessage() {
        return R.string.dialog_no_internet_connection;
    }

    @Override
    public String getTag() {
        return IMVVMConstants.TAG;
    }
}
