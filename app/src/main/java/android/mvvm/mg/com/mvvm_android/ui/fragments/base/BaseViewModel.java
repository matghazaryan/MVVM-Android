package android.mvvm.mg.com.mvvm_android.ui.fragments.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.R;
import android.os.Handler;
import android.support.annotation.NonNull;
import com.dm.dmnetworking.api_client.base.model.error.ErrorE;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseViewModel extends AndroidViewModel implements IBaseModelView {

    private final Map<Action, MutableLiveData<Object>> mutableLiveDataHashMap = new HashMap<>();

    private final Map<String, ObservableField<String>> uiTextFieldsTags = new HashMap<>();

    //For show/hide progress on start/end request (include progress_dialog.xml and add app:isVisible="@{safeUnbox(viewModel.isProgressDialogVisible)}" )
    public final ObservableField<Boolean> isProgressDialogVisible = new ObservableField<>();

    //For gone at first and visible with fade animation after request completed
    public final ObservableField<Boolean> isRootVisibleAfterLoading = new ObservableField<>(false);

    //For gone at first and visible with delay with fade animation after opened page
    public final ObservableField<Boolean> isRootVisibleDelay = new ObservableField<>(false);



    protected BaseViewModel(final @NonNull Application application) {
        super(application);
        new Handler().postDelayed(() -> isRootVisibleDelay.set(true), 280);
    }

    @Override
    public void initialize() {
        initUiTextFieldsTags(uiTextFieldsTags);
    }

    void showProgress() {
        isProgressDialogVisible.set(true);
    }

    void hideProgress() {
        new Handler().postDelayed(() -> {
            isProgressDialogVisible.set(false);
        }, 250);
        new Handler().postDelayed(() -> isRootVisibleAfterLoading.set(true), 280);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LiveData<T> getAction(final Action action) {
        final LiveData<Object> data = mutableLiveDataHashMap.get(action);
        if (data == null) {
            mutableLiveDataHashMap.put(action, new MutableLiveData<>());
        }

        return (LiveData<T>) mutableLiveDataHashMap.get(action);
    }

    @Override
    public <T> void doAction(final Action action, final T t) {
        MutableLiveData<Object> data = mutableLiveDataHashMap.get(action);
        if (data == null) {
            mutableLiveDataHashMap.put(action, new MutableLiveData<>());
        }

        data = mutableLiveDataHashMap.get(action);
        if (data != null) {
            data.setValue(t);
        }
    }

    <ErrorRequest extends IBaseError> void handleErrors(final ErrorE<ErrorRequest> errorE) {
        if (errorE != null) {
            final ErrorRequest error = errorE.getE();
            if (error != null) {
                final HashMap<String, String> errors = error.getErrors();
                if (errors != null && errors.size() > 0) {
                    showInvalidData(uiTextFieldsTags, error.getErrors());
                } else if (error.getMessage() != null) {
                    doAction(Action.OPEN_ERROR_DIALOG, error.getMessage());
                } else {
                    doAction(Action.OPEN_ERROR_DIALOG, getApplication().getApplicationContext().getString(R.string.error_general_error));
                }
            } else {
                doAction(Action.OPEN_ERROR_DIALOG, getApplication().getApplicationContext().getString(R.string.error_general_error));
            }
        }
    }

    private static void showInvalidData(final Map<String, ObservableField<String>> mapUiFields, final Map<String, String> errorMap) {
        if (mapUiFields != null && errorMap != null) {
            for (final Map.Entry<String, String> entry : errorMap.entrySet()) {
                final ObservableField<String> field = mapUiFields.get(entry.getKey());
                if (field != null) {
                    field.set(entry.getValue());
                }
            }
        }
    }

    void noInternetConnection() {
        doAction(Action.SHOW_NO_INTERNET, getApplication().getApplicationContext().getString(R.string.dialog_no_internet_connection));
    }
}
