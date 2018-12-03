package android.mvvm.mg.com.mvvm_android.fragments.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.utils.MVVMUtils;
import android.support.annotation.NonNull;
import com.dm.dmnetworking.api_client.base.model.error.ErrorE;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseViewModel extends AndroidViewModel implements IBaseModelView {

    private Map<Action, MediatorLiveData<Object>> mediatorLiveDataHashMap = new HashMap<>();
    private Map<String, ObservableField<String>> uiTextFieldsTags = new HashMap<>();
    public ObservableField<Boolean> isProgressDialogVisible = new ObservableField<>();


    public BaseViewModel(final @NonNull Application application) {
        super(application);

        initUiTextFieldsTags(uiTextFieldsTags);
    }

    void showProgress() {
        isProgressDialogVisible.set(true);
    }

    void hideProgress() {
        isProgressDialogVisible.set(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> MediatorLiveData<T> getAction(final Action action) {
        final MediatorLiveData<Object> data = mediatorLiveDataHashMap.get(action);
        if (data == null) {
            mediatorLiveDataHashMap.put(action, new MediatorLiveData<>());
        }

        return (MediatorLiveData<T>) mediatorLiveDataHashMap.get(action);
    }

    @Override
    public <T> void doAction(final Action action, final T t) {
        MediatorLiveData<Object> data = mediatorLiveDataHashMap.get(action);
        if (data == null) {
            mediatorLiveDataHashMap.put(action, new MediatorLiveData<>());
        }

        data = mediatorLiveDataHashMap.get(action);
        if (data != null) {
            data.setValue(t);
        }
    }

    void handleErrors(final ErrorE<RequestError> requestErrorErrorE) {
        hideProgress();
        if (requestErrorErrorE != null) {
            final RequestError error = requestErrorErrorE.getE();
            if (error != null) {
                final HashMap<String, String> errors = error.getErrors();
                if (errors != null && errors.size() > 0) {
                    MVVMUtils.showInvalidData(uiTextFieldsTags, error.getErrors());
                } else if (error.getMessage() != null) {
                    getAction(Action.OPEN_ERROR_DIALOG).setValue(error.getMessage());
                } else {
                    getAction(Action.OPEN_ERROR_DIALOG).setValue(getApplication().getApplicationContext().getString(R.string.error_general_error));
                }
            } else {
                getAction(Action.OPEN_ERROR_DIALOG).setValue(getApplication().getApplicationContext().getString(R.string.error_general_error));
            }
        }
    }

    void noInternetConnection() {
        hideProgress();
        getAction(Action.OPEN_ERROR_DIALOG).setValue(null);
    }
}
