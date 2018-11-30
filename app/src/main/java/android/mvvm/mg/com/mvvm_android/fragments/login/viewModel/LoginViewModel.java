package android.mvvm.mg.com.mvvm_android.fragments.login.viewModel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.constants.RequestKeys;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseViewModel;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.mvvm.mg.com.mvvm_android.repository.DataRepository;
import android.mvvm.mg.com.mvvm_android.utils.MVVMValidator;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import biometric.dm.com.dmbiometric.constants.IBIOConstants;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;
import com.dm.dmnetworking.api_client.base.model.error.ErrorE;
import com.dm.dmnetworking.api_client.base.model.success.SuccessT;

import java.util.ArrayList;
import java.util.Map;

public class LoginViewModel extends BaseViewModel {

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    public ObservableField<Boolean> isButtonEnable = new ObservableField<>();
    public ObservableField<Boolean> isCheckedRemember = new ObservableField<>();
    public ObservableField<Boolean> isProgressDialogVisible = new ObservableField<>();
    public ObservableField<String> emailError = new ObservableField<>();
    public ObservableField<String> passwordError = new ObservableField<>();


    public LoginViewModel(final @NonNull Application application) {
        super(application);

        makeUniteEmailAndPassword();

        isCheckedRemember.set(DataRepository.getInstance().prefIsCheckedRemember());

        email.setValue("david@helix.am");
        password.setValue("123456789");
    }

    @Override
    public void initUiTextFieldsTags(final Map<String, ObservableField<String>> uiTextFieldsTags) {
        uiTextFieldsTags.put(RequestKeys.EMAIL, emailError);
        uiTextFieldsTags.put(RequestKeys.PASSWORD, passwordError);
        uiTextFieldsTags.put(RequestKeys.SIGNIN, emailError);
    }

    private void makeUniteEmailAndPassword() {
        getAction(Action.EMAIL_AND_PASSWORD).addSource(email, email -> checkValidation());
        getAction(Action.EMAIL_AND_PASSWORD).addSource(password, password -> checkValidation());
    }

    private void checkValidation() {
        getAction(Action.EMAIL_AND_PASSWORD).setValue(!(TextUtils.isEmpty(email.getValue()) || TextUtils.isEmpty(password.getValue()) || !MVVMValidator.isValidEmail(email.getValue())));
    }

    public void handleBiometricErrors(final User user, final IBIOConstants.FailedType type, final int helpCode, final CharSequence helpString) {
        switch (type) {
            case AUTHENTICATION_FAILED:
                break;
            default:
                doAction(Action.OPEN_ACCOUNT_FRAGMENT, new ArrayList<>());
        }
    }

    public void setRemember(final boolean isChecked) {
        isCheckedRemember.set(isChecked);
        DataRepository.getInstance().prefSetRemember(isChecked);
    }

    public void handleLoginErrors(final ErrorE<RequestError> requestErrorErrorE) {
        isProgressDialogVisible.set(false);
        handleErrors(requestErrorErrorE);
    }

    public void onLoginSuccess(final SuccessT<User> userSuccessT) {
        isProgressDialogVisible.set(false);

        if (userSuccessT != null) {
            final User user = userSuccessT.getT();
            DataRepository.getInstance().prefSaveToken(user.getToken());

            final Boolean isChecked = isCheckedRemember.get();
            if (isChecked != null && isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                doAction(Action.OPEN_BIOMETRIC, user);
            } else {
                doAction(Action.OPEN_ACCOUNT_FRAGMENT, user);
            }
        }
    }

    public DMLiveDataBag<User, RequestError> login() {
        isProgressDialogVisible.set(true);
        return DataRepository.getInstance().apiLogin(getApplication().getApplicationContext(), new User(email.getValue(), password.getValue()));
    }

    public void updateButtonStatus(final Boolean isEnable) {
        isButtonEnable.set(isEnable);
    }
}
