package biometric.dm.com.dmbiometric.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

@SuppressLint({"MissingPermission"})
public final class DMBiometricUseConditionUtils {

    public static boolean isBiometricPromptEnabled() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P);
    }

    public static boolean isSdkVersionSupported() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public static boolean isHardwareSupported(final Context context) {
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }

    public static boolean isFingerprintAvailable(final Context context) {
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }
}
