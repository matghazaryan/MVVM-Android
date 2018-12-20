package android.mvvm.mg.com.mvvm_android.ui.fragments.splash.viewModel;

import android.app.Application;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.core.models.Configs;
import android.mvvm.mg.com.mvvm_android.core.models.User;
import android.mvvm.mg.com.mvvm_android.core.models.error.RequestError;
import android.mvvm.mg.com.mvvm_android.core.repository.DataRepository;
import android.mvvm.mg.com.mvvm_android.ui.fragments.base.BaseViewModel;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

import org.json.JSONObject;

import biometric.dm.com.dmbiometric.constants.IBIOConstants;

public class SplashViewModel extends BaseViewModel {

    public SplashViewModel(final @NonNull Application application) {
        super(application);
    }

    public void prefSaveConfigs(final JSONObject jsonObject) {
        if (jsonObject != null) {
            DataRepository.preference().saveConfigs(jsonObject.toString());
            navigateToNextPage();
        } else {
            doAction(Action.OPEN_ERROR_DIALOG, getApplication().getApplicationContext().getString(R.string.error_general_error));
        }
    }

    public DMLiveDataBag<Configs, RequestError> apiConfigs() {
        return DataRepository.api().getConfigs(getApplication().getApplicationContext());
    }

    private void navigateToNextPage() {
        if (DataRepository.preference().isCheckedRemember() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            doAction(Action.OPEN_BIOMETRIC, null);
        } else {
            new Handler().postDelayed(() -> doAction(Action.OPEN_LOGIN_FRAGMENT, null), IMVVMConstants.Delay.SPLASH);
        }
    }

    public void onLoginSuccess(final User user) {
        if (user != null) {
            DataRepository.preference().saveToken(user.getToken());
            new Handler().postDelayed(() -> doAction(Action.OPEN_ACCOUNT_FRAGMENT, user), IMVVMConstants.Delay.SPLASH);
        } else {
            doAction(Action.OPEN_ERROR_DIALOG, getApplication().getApplicationContext().getString(R.string.error_general_error));
        }
    }

    public DMLiveDataBag<User, RequestError> apiLogin(final User user) {
        return DataRepository.api().login(getApplication().getApplicationContext(), user);
    }

    public void handleBiometricErrors(final IBIOConstants.FailedType type, final int helpCode, final CharSequence helpString) {
        switch (type) {
            case AUTHENTICATION_FAILED:
                break;
            default:
                doAction(Action.OPEN_LOGIN_FRAGMENT, null);
        }
    }
}
