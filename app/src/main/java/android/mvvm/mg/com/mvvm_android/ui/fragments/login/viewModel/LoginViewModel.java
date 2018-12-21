package android.mvvm.mg.com.mvvm_android.ui.fragments.login.viewModel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.base.BaseViewModel;
import android.mvvm.mg.com.mvvm_android.core.constants.IRequestKeys;
import android.mvvm.mg.com.mvvm_android.core.models.User;
import android.mvvm.mg.com.mvvm_android.core.models.error.RequestError;
import android.mvvm.mg.com.mvvm_android.core.repository.DataRepository;
import android.mvvm.mg.com.mvvm_android.core.utils.MVVMValidator;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

import java.util.Map;

import biometric.dm.com.dmbiometric.constants.IBIOConstants;

public class LoginViewModel extends BaseViewModel {

    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();

    public final ObservableField<Boolean> isButtonEnable = new ObservableField<>();
    public final ObservableField<Boolean> isCheckedRemember = new ObservableField<>();
    public final ObservableField<String> emailError = new ObservableField<>();
    public final ObservableField<String> passwordError = new ObservableField<>();


    public LoginViewModel(final @NonNull Application application) {
        super(application);
    }

    @Override
    public void initialize() {
        super.initialize();

        isCheckedRemember.set(DataRepository.preference().isCheckedRemember());

        email.setValue("david@helix.am");
        password.setValue("123456789");
    }

    @Override
    public void initUiTextFieldsTags(final Map<String, ObservableField<String>> uiTextFieldsTags) {
        uiTextFieldsTags.put(IRequestKeys.EMAIL, emailError);
        uiTextFieldsTags.put(IRequestKeys.PASSWORD, passwordError);
        uiTextFieldsTags.put(IRequestKeys.SIGN_IN, emailError);
    }

    private void checkValidation() {
        isButtonEnable.set(!(TextUtils.isEmpty(email.getValue()) || TextUtils.isEmpty(password.getValue()) || !MVVMValidator.isValidEmail(email.getValue())));
    }

    public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        checkValidation();
    }

    public void handleBiometricErrors(final User user, final IBIOConstants.FailedType type, final int helpCode, final CharSequence helpString) {
        switch (type) {
            case AUTHENTICATION_FAILED:
                break;
            default:
                doAction(Action.OPEN_ACCOUNT_FRAGMENT, user);
        }
    }

    public DMLiveDataBag<User, RequestError> apiLogin() {
        final Boolean isChecked = isCheckedRemember.get();
        DataRepository.preference().setRemember(isChecked != null ? isChecked : false);
        return DataRepository.api().login(getApplication().getApplicationContext(), new User(email.getValue(), password.getValue()));
    }

    public void onSuccessLogin(final User user) {
        if (user != null) {
            DataRepository.preference().saveToken(user.getToken());
            final Boolean isChecked = isCheckedRemember.get();
            if (isChecked != null && isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                doAction(Action.OPEN_BIOMETRIC, user);
            } else {
                doAction(Action.OPEN_ACCOUNT_FRAGMENT, user);
            }
        } else {
            doAction(Action.SHOW_ERROR_DIALOG, getApplication().getApplicationContext().getString(R.string.error_general_error));
        }
    }
}
