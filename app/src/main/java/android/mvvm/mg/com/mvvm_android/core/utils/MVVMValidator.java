package android.mvvm.mg.com.mvvm_android.core.utils;

import android.text.TextUtils;

public final class MVVMValidator {

    public static boolean isValidEmail(final CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
