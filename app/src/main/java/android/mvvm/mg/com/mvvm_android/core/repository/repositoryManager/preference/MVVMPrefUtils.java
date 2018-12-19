package android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.preference;

import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;

import dmutils.com.dmutils.pref.DMPrefsCacheManager;

public class MVVMPrefUtils {

    public static String getConfigsJson() {
        return DMPrefsCacheManager.getInstance().getStringFromCashe(IMVVMConstants.PrefName.CONFIGS, null);
    }

    public static void saveConfigsJson(final String configsJson) {
        DMPrefsCacheManager.getInstance().putInCashe(IMVVMConstants.PrefName.CONFIGS, configsJson);
    }

    public static String getLanguageCode() {
        return DMPrefsCacheManager.getInstance().getStringFromCashe(IMVVMConstants.PrefName.LANGUAGE_CODE, IMVVMConstants.Language.EN);
    }

    public static void setLanguageCode(final String languageCode) {
        DMPrefsCacheManager.getInstance().putInCashe(IMVVMConstants.PrefName.LANGUAGE_CODE, languageCode);
    }

    public static String getToken() {
        return DMPrefsCacheManager.getInstance().getStringFromCashe(IMVVMConstants.PrefName.TOKEN, null);
    }

    public static void setToken(final String token) {
        DMPrefsCacheManager.getInstance().putInCashe(IMVVMConstants.PrefName.TOKEN, token);
    }

    public static boolean isCheckedRemember() {
        return DMPrefsCacheManager.getInstance().getBooleanFromCashe(IMVVMConstants.PrefName.IS_CHECKED_REMEMBER, false);
    }

    public static void setRemember(final boolean token) {
        DMPrefsCacheManager.getInstance().putInCashe(IMVVMConstants.PrefName.IS_CHECKED_REMEMBER, token);
    }

    public static void setLoggedIn(final boolean isUserLoggedIn) {
        DMPrefsCacheManager.getInstance().putInCashe(IMVVMConstants.PrefName.IS_USER_LOGGED_IN, isUserLoggedIn);
    }

    public static boolean isUserLoggedIn() {
        return DMPrefsCacheManager.getInstance().getBooleanFromCashe(IMVVMConstants.PrefName.IS_USER_LOGGED_IN, false);
    }

    public static String getProfilePhoto() {
        return DMPrefsCacheManager.getInstance().getStringFromCashe(IMVVMConstants.PrefName.PROFILE_PHOTO, null);
    }

    public static void saveProfilePhoto(final String path) {
        DMPrefsCacheManager.getInstance().putInCashe(IMVVMConstants.PrefName.PROFILE_PHOTO, path);
    }
}
