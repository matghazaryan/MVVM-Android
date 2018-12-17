package biometric.dm.com.dmbiometric.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import biometric.dm.com.dmbiometric.cache.BIOBiometricPrefsCacheManager;
import biometric.dm.com.dmbiometric.constants.IBIOConstants;
import biometric.dm.com.dmbiometric.interfaces.IBIOBiometric;
import biometric.dm.com.dmbiometric.listeners.IBIODecryptListener;
import biometric.dm.com.dmbiometric.listeners.IBIOEncryptListener;
import biometric.dm.com.dmbiometric.listeners.IBIOFinishListener;
import biometric.dm.com.dmbiometric.listeners.IBIOPrepareListener;
import biometric.dm.com.dmbiometric.prepare.BIOBiometricV23;
import biometric.dm.com.dmbiometric.prepare.BIOBiometricV28;
import biometric.dm.com.dmbiometric.prepare.BIOPrepareBiometric;
import biometric.dm.com.dmbiometric.prepare.DMBiometricConfigs;
import biometric.dm.com.dmbiometric.utils.*;

import javax.crypto.Cipher;


public final class DMBiometricManager<T> extends BIOPrepareBiometric {

    private DMBiometricConfigs<T> configs;
    private IBIOBiometric biometric;

    public DMBiometricManager(final DMBiometricConfigs<T> configs) {
        this.configs = configs;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isConfigsCorrect(final DMBiometricConfigs<T> configs) {

        boolean isSuccess = true;

        try {
            if (configs == null) {
                throw new Exception("DMBiometricConfigs configs can't be null");
            } else if (configs.getBiometricListener() == null) {
                throw new Exception("DMBiometricConfigs biometricListener can't be null");
            } else if (configs.getTClass() == null) {
                throw new Exception("DMBiometricConfigs tClass (for encrypt and decrypt) can't be null");
            } else if (configs.getEncryptionMode() == IBIOConstants.EncryptionMode.ENCRYPT && configs.getObjectForEncrypt() == null) {
                throw new Exception("DMBiometricConfigs object for encrypt can't be null");
            } else if (configs.getDialogViewV23() == null) {
                if (configs.getTitle() == null) {
                    throw new Exception("DMBiometricConfigs Dialog title can't be null");
                } else if (configs.getDescription() == null) {
                    throw new Exception("DMBiometricConfigs Dialog description can't be null");
                } else if (configs.getNegativeButtonText() == null) {
                    throw new Exception("DMBiometricConfigs Dialog negative button text can't be null");
                }
            } else {
                if (configs.getUpdateStatusV23Listener() == null) {
                    throw new Exception("DMBiometricConfigs updateStatusV23Listener can't be null");
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            if (configs != null) {
                configs.getBiometricListener().onFailed(IBIOConstants.FailedType.INCORRECT_CONFIGS, 0, null);
            }
            isSuccess = false;
        }

        if (isSuccess) {

            final Context context = configs.getContext();

            if (!DMBiometricUseConditionUtils.isSdkVersionSupported()) {
                onSdkVersionNotSupported();
                isSuccess = false;
            } else if (!DMBiometricUseConditionUtils.isHardwareSupported(context)) {
                onBiometricAuthenticationNotSupported();
                isSuccess = false;
            } else if (!DMBiometricUseConditionUtils.isFingerprintAvailable(context)) {
                onBiometricAuthenticationNotAvailable();
                isSuccess = false;
            }
        }

        return isSuccess;
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showBiometric() {
        if (isConfigsCorrect(configs)) {
            BIOBiometricPrefsCacheManager.getInstance().Initialize(configs.getContext());
            prepare(configs, new IBIOPrepareListener() {
                @Override
                public void onSuccess() {
                    if (DMBiometricUseConditionUtils.isBiometricPromptEnabled()) {
                        biometric = new BIOBiometricV28(configs, DMBiometricManager.this);
                    } else {
                        biometric = new BIOBiometricV23(configs, DMBiometricManager.this);
                    }

                    biometric.showBiometricDialog();
                }

                @Override
                public void onFailed() {
                    configs.getBiometricListener().onFailed(IBIOConstants.FailedType.AUTHENTICATION_FAILED_EXCEPTION, 0, null);
                }
            });
        }
    }

    public void cancel() {
        if (biometric != null) {
            biometric.cancel();
        }
    }

    private void dismiss() {
        if (biometric != null) {
            biometric.dismiss();
        }
    }

    /**
     * Call in onStop()
     */
    public void onStop() {
        cancel();
    }

    private void doEncryptingOrDecrypting(final Cipher cipher, final IBIOFinishListener listener) {
        switch (configs.getEncryptionMode()) {
            case ENCRYPT:
                final String json = DMJsonUtils.objectToJson(configs.getObjectForEncrypt());
                final String textForEncrypt = DMPrepareUtils.beforeEncryption(json);

                DMEncryptionAndDecryptionUtils.encryptString(cipher, textForEncrypt, new IBIOEncryptListener() {
                    @Override
                    public void onSuccess(final String encryptedText) {
                        DMBasePrefUtils.setData(encryptedText);
                        configs.getBiometricListener().onSuccessEncrypted();

                        listener.onFinish();
                    }

                    @Override
                    public void onFailed() {
                        configs.getBiometricListener().onFailed(IBIOConstants.FailedType.AUTHENTICATION_FAILED_EXCEPTION, 0, null);

                        listener.onFinish();
                    }
                });
                break;
            case DECRYPT:
                final String textForDecrypt = DMBasePrefUtils.getData();
                DMEncryptionAndDecryptionUtils.decryptString(cipher, textForDecrypt, new IBIODecryptListener() {
                    @Override
                    public void onSuccess(final String decryptedText) {
                        final String json = DMPrepareUtils.afterDecryption(decryptedText);
                        final T t = DMJsonUtils.jsonToObject(json, configs.getTClass());
                        configs.getBiometricListener().onSuccessDecrypted(t);

                        listener.onFinish();
                    }

                    @Override
                    public void onFailed() {
                        configs.getBiometricListener().onFailed(IBIOConstants.FailedType.AUTHENTICATION_FAILED_EXCEPTION, 0, null);

                        listener.onFinish();
                    }
                });
                break;
            default:
                listener.onFinish();
                configs.getBiometricListener().onFailed(IBIOConstants.FailedType.AUTHENTICATION_FAILED, 0, null);
        }
    }

    @Override
    protected void onSdkVersionNotSupported() {
        configs.getBiometricListener().onFailed(IBIOConstants.FailedType.SDK_VERSION_NOT_SUPPORTED, 0, null);
    }

    @Override
    protected void onBiometricAuthenticationNotSupported() {
        configs.getBiometricListener().onFailed(IBIOConstants.FailedType.BIOMETRIC_AUTHENTICATION_NOT_SUPPORTED, 0, null);
    }

    @Override
    protected void onBiometricAuthenticationNotAvailable() {
        configs.getBiometricListener().onFailed(IBIOConstants.FailedType.BIOMETRIC_AUTHENTICATION_NOT_AVAILABLE, 0, null);
    }

    @Override
    protected void onAuthenticationFailed() {
        configs.getBiometricListener().onFailed(IBIOConstants.FailedType.AUTHENTICATION_FAILED, 0, null);
    }

    @Override
    protected void onAuthenticationCancelled() {
        configs.getBiometricListener().onFailed(IBIOConstants.FailedType.AUTHENTICATION_CANCELLED, 0, null);
    }

    @Override
    protected void onAuthenticationSuccessful(final Cipher cipher) {
        doEncryptingOrDecrypting(cipher, this::dismiss);
    }

    @Override
    protected void onAuthenticationHelp(final int helpCode, final CharSequence helpString) {
        configs.getBiometricListener().onFailed(IBIOConstants.FailedType.AUTHENTICATION_HELP, helpCode, helpString);
    }

    @Override
    protected void onAuthenticationError(final int errorCode, final CharSequence errString) {
        switch (errorCode) {
            case 10:
                configs.getBiometricListener().onFailed(IBIOConstants.FailedType.AUTHENTICATION_CANCELLED, errorCode, errString);
                break;
            case 9:
                configs.getBiometricListener().onFailed(IBIOConstants.FailedType.BIOMETRIC_SENSOR_DISABLED, errorCode, errString);
                break;
//            case 7:
//                configs.getBiometricListener().onFailed(DMConstants.FailedType.OTHER, errorCode, errString);
//                break;
            default:
                configs.getBiometricListener().onFailed(IBIOConstants.FailedType.OTHER, errorCode, errString);
        }
    }
}
