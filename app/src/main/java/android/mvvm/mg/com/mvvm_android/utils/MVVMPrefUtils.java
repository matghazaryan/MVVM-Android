package android.mvvm.mg.com.mvvm_android.utils;

import android.mvvm.mg.com.mvvm_android.constants.IConstants;
import dmutils.com.dmutils.pref.DMPrefsCacheManager;

public class MVVMPrefUtils {

    public static String getConfigsJson() {
        return DMPrefsCacheManager.getInstance().getStringFromCashe(IConstants.PrefName.CONFIGS, null);
    }

    public static void saveConfigsJson(final String configsJson) {
        DMPrefsCacheManager.getInstance().putInCashe(IConstants.PrefName.CONFIGS, configsJson);
    }

    public static String getLanguageCode() {
        return DMPrefsCacheManager.getInstance().getStringFromCashe(IConstants.PrefName.LANGUAGE_CODE, IConstants.Language.EN);
    }

    public static void setLanguageCode(final String languageCode) {
        DMPrefsCacheManager.getInstance().putInCashe(IConstants.PrefName.LANGUAGE_CODE, languageCode);
    }

    public static String getToken() {
        return DMPrefsCacheManager.getInstance().getStringFromCashe(IConstants.PrefName.TOKEN, null);
    }

    public static void setToken(final String token) {
        DMPrefsCacheManager.getInstance().putInCashe(IConstants.PrefName.TOKEN, token);
    }

    public static boolean isCheckedRemember() {
        return DMPrefsCacheManager.getInstance().getBooleanFromCashe(IConstants.PrefName.IS_CHECKED_REMEMBER, false);
    }

    public static void setRemember(final boolean token) {
        DMPrefsCacheManager.getInstance().putInCashe(IConstants.PrefName.IS_CHECKED_REMEMBER, token);
    }

    public static void setLoggedIn(final boolean isUserLoggedIn) {
        DMPrefsCacheManager.getInstance().putInCashe(IConstants.PrefName.IS_USER_LOGGED_IN, isUserLoggedIn);
    }

    public static boolean isUserLoggedIn() {
        return DMPrefsCacheManager.getInstance().getBooleanFromCashe(IConstants.PrefName.IS_USER_LOGGED_IN, false);
    }

    public static String getEmail() {
        return DMPrefsCacheManager.getInstance().getStringFromCashe(IConstants.PrefName.EMAIL, null);
    }

    public static void saveEmail(final String email) {
        DMPrefsCacheManager.getInstance().putInCashe(IConstants.PrefName.EMAIL, email);
    }

    public static String getProfilePhoto() {
        return DMPrefsCacheManager.getInstance().getStringFromCashe(IConstants.PrefName.PROFILE_PHOTO, null);
    }

    public static void saveProfilePhoto(final String path) {
        DMPrefsCacheManager.getInstance().putInCashe(IConstants.PrefName.PROFILE_PHOTO, path);
    }

    public static String getPassword() {
        return DMPrefsCacheManager.getInstance().getStringFromCashe(IConstants.PrefName.PASSWORD, null);
    }

    public static void savePassword(final String password) {
        DMPrefsCacheManager.getInstance().putInCashe(IConstants.PrefName.PASSWORD, password);
    }

}
