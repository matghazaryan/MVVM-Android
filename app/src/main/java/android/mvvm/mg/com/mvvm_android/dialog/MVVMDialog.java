package android.mvvm.mg.com.mvvm_android.dialog;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.os.Build;
import android.support.annotation.RequiresApi;

import alertdialog.dm.com.dmalertdialog.configs.DMBaseDialogConfigs;
import biometric.dm.com.dmbiometric.constants.IBIOConstants;
import biometric.dm.com.dmbiometric.listeners.IDMBiometricListener;
import biometric.dm.com.dmbiometric.main.DMBiometricManager;
import biometric.dm.com.dmbiometric.prepare.DMBiometricConfigs;

public class MVVMDialog {

    public static void showErrorDialog(final Context context, final String message) {
        new MVVMAlertDialog().showErrorDialog(new DMBaseDialogConfigs<>(context).setContent(message));
    }

    public static void showNoInternetDialog(final Context context) {
        new MVVMAlertDialog().showWarningDialog(new DMBaseDialogConfigs<>(context).setContentRes(R.string.dialog_no_internet_connection));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static DMBiometricManager<User> showBiometricForEncrypt(final Context context, final User user, final IDMBiometricListener<User> listener) {
        final DMBiometricConfigs<User> configs = new DMBiometricConfigs<User>(context)
                .setTitle(context.getString(R.string.biometric_title))
                .setSubtitle(context.getString(R.string.biometric_sub_title))
                .setDescription(context.getString(R.string.biometric_description))
                .setNegativeButtonText(context.getString(R.string.biometric_cancel))
                .setClass(User.class)
                .setObjectForEncrypt(user)
                .setEncrypt(IBIOConstants.EncryptionMode.ENCRYPT)
                .setBiometricListener(listener);

        final DMBiometricManager<User> manager = new DMBiometricManager<>(configs);
        manager.showBiometric();

        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static DMBiometricManager<User> showBiometricForDecrypt(final Context context, final IDMBiometricListener<User> listener) {
        final DMBiometricConfigs<User> configs = new DMBiometricConfigs<User>(context)
                .setTitle(context.getString(R.string.biometric_title))
                .setSubtitle(context.getString(R.string.biometric_sub_title))
                .setDescription(context.getString(R.string.biometric_description))
                .setNegativeButtonText(context.getString(R.string.biometric_cancel))
                .setClass(User.class)
                .setEncrypt(IBIOConstants.EncryptionMode.DECRYPT)
                .setBiometricListener(listener);

        final DMBiometricManager<User> manager = new DMBiometricManager<>(configs);
        manager.showBiometric();

        return manager;
    }
}
