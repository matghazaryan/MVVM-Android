package android.mvvm.mg.com.mvvm_android.core.base;

import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;

public interface IBaseFragment {

    default void initialize() {
    }

    default void subscribers(final LifecycleOwner owner) {
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
