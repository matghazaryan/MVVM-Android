package android.mvvm.mg.com.mvvm_android.constants;

public interface IConstants {

    String TAG = "MVVM";

    interface PrefName {
        String CONFIGS = "configs";
        String TOKEN = "token";
        String LANGUAGE_CODE = "language_code";
        String IS_USER_LOGGED_IN = "isUserLoggedIn";
        String IS_CHECKED_REMEMBER = "prefIsCheckedRemember";
        String PROFILE_PHOTO = "profile_photo";

    }

    interface Language {
        String HY = "hy";
        String EN = "en";
        String RU = "ru";
    }

    interface BundleKey {
        String USER = "user";
    }

    interface Delay {
        int SPLASH = 1500;
    }

    interface RequestCode {
        int CAMERA = 1000;
    }

    enum Action {
        OPEN_ACCOUNT_FRAGMENT,
        OPEN_BIOMETRIC,
        OPEN_ERROR_DIALOG,
        OPEN_LOGIN_FRAGMENT,
        DO_LOGIN,
        ON_NEW_IMAGE_PATH,
        SHOW_NO_INTERNET
    }
}
