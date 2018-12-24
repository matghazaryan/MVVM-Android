package android.mvvm.mg.com.mvvm_android.core.base;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.os.Bundle;

import java.util.Map;

/**
 * IBaseModelView interface which has few function for use in child classes
 */
public interface IBaseModelView extends IBaseConstants, IMVVMConstants {

    default void initUiTextFieldsTags(final Map<String, ObservableField<String>> uiTextFieldsTags) {
    }

    default void initialize() {
    }

    default void initialize(final Bundle bundle) {
    }

    default void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    }

    <T> LiveData<T> getAction(final int action);

    <T> void doAction(final int action, final T t);

    default void setEnableEmptyView(final boolean enableEmptyView) {
    }
}
