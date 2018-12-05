package android.mvvm.mg.com.mvvm_android.fragments.base;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.constants.IConstants;

import java.util.Map;

public interface IBaseModelView extends IConstants {

    default void initUiTextFieldsTags(final Map<String, ObservableField<String>> uiTextFieldsTags) {

    }

    <T> LiveData<T> getAction(final Action action);

    <T> void doAction(final Action action, final T t);
}
