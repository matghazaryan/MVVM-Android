package android.mvvm.mg.com.mvvm_android.core.base;

import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;
import android.support.v4.app.FragmentActivity;

import java.util.List;

/**
 * IBaseFragment interface which has few function for use in child classes
 */
interface DMBaseIFragment {

    default void initialize() {
    }

    default void subscribers(final LifecycleOwner owner) {
    }

    default void actionsForClearOnDestroyView(final List<Integer> actions) {

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

    default FragmentActivity getActivityForViewModel() {
        return null;
    }
}
