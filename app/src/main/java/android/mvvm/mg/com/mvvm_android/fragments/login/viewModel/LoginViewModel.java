package android.mvvm.mg.com.mvvm_android.fragments.login.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.constants.RequestKeys;
import android.mvvm.mg.com.mvvm_android.dialog.MVVMAlertDialog;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.mvvm.mg.com.mvvm_android.repository.DataRepository;
import android.mvvm.mg.com.mvvm_android.utils.MVVMUtils;
import android.mvvm.mg.com.mvvm_android.utils.MVVMValidator;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;
import com.dm.dmnetworking.api_client.base.model.error.ErrorE;
import com.dm.dmnetworking.api_client.base.model.success.SuccessT;

import java.util.HashMap;
import java.util.Map;

import alertdialog.dm.com.dmalertdialog.configs.DMBaseDialogConfigs;

public class LoginViewModel extends AndroidViewModel {

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    public ObservableField<Boolean> isButtonEnable = new ObservableField<>();
    public ObservableField<Boolean> isCheckedRemember = new ObservableField<>();
    public ObservableField<Boolean> isProgressDialogVisible = new ObservableField<>();
    public ObservableField<String> emailError = new ObservableField<>();
    public ObservableField<String> passwordError = new ObservableField<>();

    private MediatorLiveData<Boolean> emailAndPasswordValidation = new MediatorLiveData<>();

    private Map<String, ObservableField<String>> uiTextFieldsTags;

    public MutableLiveData<User> openNextPage = new MutableLiveData<>();

    public LoginViewModel(final @NonNull Application application) {
        super(application);

        makeUniteEmailAndPassword();
        initUiTextFieldsTags();

        isCheckedRemember.set(DataRepository.getInstance().isCheckedRemember());

        email.setValue("david@helix.am");
        password.setValue("123456789");
    }

    private void initUiTextFieldsTags() {
        uiTextFieldsTags = new HashMap<>();
        uiTextFieldsTags.put(RequestKeys.EMAIL, emailError);
        uiTextFieldsTags.put(RequestKeys.PASSWORD, passwordError);
        uiTextFieldsTags.put(RequestKeys.SIGNIN, emailError);
    }

    private void makeUniteEmailAndPassword() {
        emailAndPasswordValidation.addSource(email, email -> checkValidation());
        emailAndPasswordValidation.addSource(password, password -> checkValidation());
    }

    private void checkValidation() {
        emailAndPasswordValidation.setValue(!(TextUtils.isEmpty(email.getValue()) || TextUtils.isEmpty(password.getValue()) || !MVVMValidator.isValidEmail(email.getValue())));
    }

    public MediatorLiveData<Boolean> getEmailAndPasswordObservable() {
        return emailAndPasswordValidation;
    }

    public MutableLiveData<User> getOpenNextPage() {
        return openNextPage;
    }

    public void setRemember(final boolean isChecked) {
        DataRepository.getInstance().setRemember(isChecked);
    }

    public void onLoginSuccess(final SuccessT<User> userSuccessT) {
        isProgressDialogVisible.set(false);

        if (userSuccessT != null) {
            final User user = userSuccessT.getT();
            DataRepository.getInstance().saveToken(user.getToken());

            openNextPage.setValue(user);
        }
    }

    public void onError(final ErrorE<RequestError> errorErrorE) {
        isProgressDialogVisible.set(false);
        if (errorErrorE != null) {
            final RequestError error = errorErrorE.getE();
            if (error != null) {
                if (error.getErrors() != null) {
                    MVVMUtils.showInvalidData(uiTextFieldsTags, error.getErrors());
                } else if (error.getMessage() != null) {
                    new MVVMAlertDialog().showErrorDialog(new DMBaseDialogConfigs<>(getApplication().getApplicationContext())
                            .setContent(error.getMessage()));
                }
            }
        }
    }

    public DMLiveDataBag<User, RequestError> login() {
        isProgressDialogVisible.set(true);
        return DataRepository.getInstance().login(getApplication().getApplicationContext(), new User(email.getValue(), password.getValue()));
    }

    public void updateButtonStatus(final Boolean isEnable) {
        isButtonEnable.set(isEnable);
    }
}
