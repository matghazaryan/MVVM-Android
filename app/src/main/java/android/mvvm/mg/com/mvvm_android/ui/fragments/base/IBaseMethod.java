package android.mvvm.mg.com.mvvm_android.ui.fragments.base;

import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;

public interface IBaseMethod {

    default void initialize() {
    }

    default void subscribes(final LifecycleOwner owner) {
    }

    default int getTitleRes() {
        return R.string.app_name;
    }

    default String getTitle() {
        return null;
    }

    default boolean isShowActionBar() {
        return true;
    }
}
