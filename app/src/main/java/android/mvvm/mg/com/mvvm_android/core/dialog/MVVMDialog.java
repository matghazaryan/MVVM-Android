package android.mvvm.mg.com.mvvm_android.core.dialog;

import alertdialog.dm.com.dmalertdialog.DMDialogBaseConfigs;
import android.content.Context;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.models.User;
import android.os.Build;
import android.support.annotation.RequiresApi;
import biometric.dm.com.dmbiometric.DMBIOConfigs;
import biometric.dm.com.dmbiometric.DMBIOIConstants;
import biometric.dm.com.dmbiometric.DMBIOIListener;
import biometric.dm.com.dmbiometric.DMBIOManager;

public class MVVMDialog {

    public static void showErrorDialog(final Context context, final String message) {
        new MVVMAlertDialog().showErrorDialog(new DMDialogBaseConfigs<>(context).setContent(message));
    }

    public static void showNoInternetDialog(final Context context) {
        new MVVMAlertDialog().showWarningDialog(new DMDialogBaseConfigs<>(context).setContentRes(R.string.dialog_no_internet_connection));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static DMBIOManager<User> showBiometricForEncrypt(final Context context, final User user, final DMBIOIListener<User> listener) {
        final DMBIOConfigs<User> configs = new DMBIOConfigs<User>(context)
                .setTitle(context.getString(R.string.biometric_title))
                .setSubtitle(context.getString(R.string.biometric_sub_title))
                .setDescription(context.getString(R.string.biometric_description))
                .setNegativeButtonText(context.getString(R.string.biometric_cancel))
                .setClass(User.class)
                .setObjectForEncrypt(user)
                .setEncrypt(DMBIOIConstants.EncryptionMode.ENCRYPT)
                .setBiometricListener(listener);

        final DMBIOManager<User> manager = new DMBIOManager<>(configs);
        manager.showBiometric();

        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static DMBIOManager<User> showBiometricForDecrypt(final Context context, final DMBIOIListener<User> listener) {
        final DMBIOConfigs<User> configs = new DMBIOConfigs<User>(context)
                .setTitle(context.getString(R.string.biometric_title))
                .setSubtitle(context.getString(R.string.biometric_sub_title))
                .setDescription(context.getString(R.string.biometric_description))
                .setNegativeButtonText(context.getString(R.string.biometric_cancel))
                .setClass(User.class)
                .setEncrypt(DMBIOIConstants.EncryptionMode.DECRYPT)
                .setBiometricListener(listener);

        final DMBIOManager<User> manager = new DMBIOManager<>(configs);
        manager.showBiometric();

        return manager;
    }
}
