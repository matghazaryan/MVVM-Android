package biometric.dm.com.dmbiometric.prepare;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import biometric.dm.com.dmbiometric.R;
import biometric.dm.com.dmbiometric.interfaces.IBIOBiometric;

@RequiresApi(api = Build.VERSION_CODES.M)
@SuppressLint("MissingPermission")
public final class BIOBiometricV23 implements IBIOBiometric {

    private DMBiometricConfigs configs;
    private BIOBaseBiometric biometric;

    private CancellationSignal cancellationSignal;
    private BIOBiometricDialogV23 biometricDialog;

    public BIOBiometricV23(final DMBiometricConfigs configs, final BIOBaseBiometric biometric) {
        this.configs = configs;
        this.biometric = biometric;
    }

    @Override
    public void showBiometricDialog() {
        if (configs != null) {
            initBiometric();
            displayBiometricDialog();
        }
    }

    private void initBiometric() {
        cancellationSignal = new CancellationSignal();
        FingerprintManagerCompat.from(configs.getContext()).authenticate(
                new FingerprintManagerCompat.CryptoObject(configs.getCipher()), 0, cancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(final int errMsgId, final CharSequence errString) {
                        super.onAuthenticationError(errMsgId, errString);
                        updateStatus(String.valueOf(errString), R.color.status_normal_color);
                        biometric.onAuthenticationError(errMsgId, errString);
                    }

                    @Override
                    public void onAuthenticationHelp(final int helpMsgId, final CharSequence helpString) {
                        super.onAuthenticationHelp(helpMsgId, helpString);
                        updateStatus(String.valueOf(helpString), R.color.status_normal_color);
                        biometric.onAuthenticationHelp(helpMsgId, helpString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(final FingerprintManagerCompat.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        biometric.onAuthenticationSuccessful(result.getCryptoObject().getCipher());
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        updateStatus(configs.getContext().getString(R.string.biometric_not_recognized), R.color.status_error_color);
                        biometric.onAuthenticationFailed();
                    }
                }, null);
    }

    private void displayBiometricDialog() {
        biometricDialog = new BIOBiometricDialogV23(configs, biometric);
        biometricDialog.setOnCancelListener(dialog -> cancel());
        biometricDialog.setOnDismissListener(dialog -> cancel());
        biometricDialog.show();
    }

    private void updateStatus(final String status, final int color) {
        if (biometricDialog != null) {
            biometricDialog.updateStatus(status, color);
        }
    }

    @Override
    public void cancel() {
        if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
            cancellationSignal.cancel();
        }

        if (biometricDialog != null && biometricDialog.isShowing()) {
            biometricDialog.dismiss();
        }
    }

    @Override
    public void dismiss() {
        if (biometricDialog != null) {
            biometricDialog.dismiss();
        }
    }
}
