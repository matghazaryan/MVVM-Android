package biometric.dm.com.dmbiometric.listeners;


import biometric.dm.com.dmbiometric.constants.IBIOConstants;

public interface IDMBiometricListener<T> {

    default void onSuccessEncrypted() {
    }

    default void onSuccessDecrypted(T t) {
    }

    void onFailed(IBIOConstants.FailedType type, final int helpCode, final CharSequence helpString);
}
