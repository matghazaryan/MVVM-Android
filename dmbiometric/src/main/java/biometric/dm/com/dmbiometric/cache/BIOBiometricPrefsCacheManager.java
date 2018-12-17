package biometric.dm.com.dmbiometric.cache;


import android.content.Context;
import android.content.SharedPreferences;


public final class BIOBiometricPrefsCacheManager {

    private static BIOBiometricPrefsCacheManager instance;
    private SharedPreferences mSharedPreferences;

    private BIOBiometricPrefsCacheManager() {
    }

    public static BIOBiometricPrefsCacheManager getInstance() {
        if (instance == null) {
            instance = new BIOBiometricPrefsCacheManager();
        }

        return instance;
    }

    public void Initialize(final Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void putInCache(final String key, final String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public String getStringFromCache(final String key, final String defValue) {
        final String v = mSharedPreferences.getString(key, null);
        return v != null ? v : defValue;
    }
}
