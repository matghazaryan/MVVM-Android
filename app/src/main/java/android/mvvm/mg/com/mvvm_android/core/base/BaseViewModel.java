package android.mvvm.mg.com.mvvm_android.core.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.dm.dmnetworking.api_client.base.model.error.ErrorE;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dmutils.com.dmutils.permission.DMEasyPermissions;

public abstract class BaseViewModel extends AndroidViewModel implements IBaseModelView {

    protected final BaseApplicationConfigs mApplicationConfigs;

    private final SparseArray<MutableLiveData<Object>> baseMutableLiveDataSparseArray = new SparseArray<>();

    private final Map<String, ObservableField<String>> baseUITextFieldsTags = new HashMap<>();

    //For show/hide progress on start/end request (include progress_dialog.xml and add app:isVisible="@{safeUnbox(viewModel.isBaseProgressDialogVisible)}" )
    public final ObservableField<Boolean> isBaseProgressDialogVisible = new ObservableField<>();

    //For gone at first and visible with fade animation after request completed
    public final ObservableField<Boolean> isBaseRootVisibleAfterLoading = new ObservableField<>(false);

    //For gone at first and visible with delay with fade animation after opened page
    public final ObservableField<Boolean> isBaseRootVisibleDelay = new ObservableField<>(false);

    protected BaseViewModel(final @NonNull Application application) {
        super(application);
        mApplicationConfigs = ((BaseApplication) Objects.requireNonNull(application)).getApplicationConfigs();
        new Handler().postDelayed(() -> isBaseRootVisibleDelay.set(true), AnimDuration.ROOT_VISIBLE_DELAY);
    }

    @Override
    public void initialize() {
        initUiTextFieldsTags(baseUITextFieldsTags);
    }

    void showProgress() {
        setEnableEmptyViewFromNetwork(false);
        isBaseProgressDialogVisible.set(true);
    }

    void hideProgress() {
        new Handler().postDelayed(() -> isBaseProgressDialogVisible.set(false), AnimDuration.PROGRESS_DIALOG_VISIBLE_DELAY);
        new Handler().postDelayed(() -> isBaseRootVisibleAfterLoading.set(true), AnimDuration.ROOT_VISIBLE_DELAY);
        setEnableEmptyViewFromNetwork(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LiveData<T> getAction(final int action) {
        final LiveData<Object> data = baseMutableLiveDataSparseArray.get(action);
        if (data == null) {
            baseMutableLiveDataSparseArray.put(action, new MutableLiveData<>());
        }

        return (LiveData<T>) baseMutableLiveDataSparseArray.get(action);
    }

    @Override
    public <T> void doAction(final int action, final T t) {
        MutableLiveData<Object> data = baseMutableLiveDataSparseArray.get(action);
        if (data == null) {
            baseMutableLiveDataSparseArray.put(action, new MutableLiveData<>());
        }

        data = baseMutableLiveDataSparseArray.get(action);
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
                    showInvalidData(baseUITextFieldsTags, error.getErrors());
                } else if (error.getMessage() != null) {
                    doAction(BaseAction.SHOW_ERROR_DIALOG, error.getMessage());
                } else {
                    doAction(BaseAction.SHOW_ERROR_DIALOG, getApplication().getApplicationContext().getString(mApplicationConfigs.getGeneralErrorMessage()));
                }
            } else {
                doAction(BaseAction.SHOW_ERROR_DIALOG, getApplication().getApplicationContext().getString(mApplicationConfigs.getGeneralErrorMessage()));
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
        new Handler().postDelayed(() -> doAction(BaseAction.SHOW_NO_INTERNET_DIALOG, getApplication().getApplicationContext().getString(mApplicationConfigs.getNoInternetMessage())), AnimDuration.ROOT_VISIBLE_DELAY);
    }

    void setEnableEmptyViewFromNetwork(final boolean enableEmptyView) {

    }

    void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults, final Object... receivers) {
        DMEasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, receivers);
    }
}
