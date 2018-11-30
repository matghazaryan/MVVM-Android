package biometric.dm.com.dmbiometric.utils;


import biometric.dm.com.dmbiometric.cache.BIOBiometricPrefsCacheManager;
import biometric.dm.com.dmbiometric.constants.IBIOConstants;

public final class DMBasePrefUtils {

    public static String getData() {
        return BIOBiometricPrefsCacheManager.getInstance().getStringFromCache(IBIOConstants.PrefKey.DATA, "");
    }

    public static void setData(final String data) {
        BIOBiometricPrefsCacheManager.getInstance().putInCache(IBIOConstants.PrefKey.DATA, data);
    }

    public static String getIV() {
        return BIOBiometricPrefsCacheManager.getInstance().getStringFromCache(IBIOConstants.PrefKey.KEY_IV, "");
    }

    public static void setIV(final String iv) {
        BIOBiometricPrefsCacheManager.getInstance().putInCache(IBIOConstants.PrefKey.KEY_IV, iv);
    }
}
