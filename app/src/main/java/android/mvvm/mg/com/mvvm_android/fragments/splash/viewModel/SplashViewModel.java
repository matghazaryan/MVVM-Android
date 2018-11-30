package android.mvvm.mg.com.mvvm_android.fragments.splash.viewModel;

import android.app.Application;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.constants.IConstants;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseViewModel;
import android.mvvm.mg.com.mvvm_android.models.Configs;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.mvvm.mg.com.mvvm_android.repository.DataRepository;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import biometric.dm.com.dmbiometric.constants.IBIOConstants;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;
import com.dm.dmnetworking.api_client.base.model.success.SuccessT;
import org.json.JSONObject;

public class SplashViewModel extends BaseViewModel {

    public SplashViewModel(final @NonNull Application application) {
        super(application);
    }

    public void saveConfigs(final JSONObject jsonObject) {
        if (jsonObject != null) {
            DataRepository.getInstance().prefSaveConfigs(jsonObject.toString());

            openNextPage();
        } else {
            doAction(Action.OPEN_ERROR_DIALOG, getApplication().getApplicationContext().getString(R.string.error_general_error));
        }
    }

    public DMLiveDataBag<Configs, RequestError> getConfigs() {
        return DataRepository.getInstance().apiGetConfigs(getApplication().getApplicationContext());
    }

    private void openNextPage() {
        if (DataRepository.getInstance().prefIsCheckedRemember() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            doAction(Action.OPEN_BIOMETRIC, null);
        } else {
            new Handler().postDelayed(() -> doAction(Action.OPEN_LOGIN_FRAGMENT, null), IConstants.Delay.SPLASH);
        }
    }

    public void onLoginSuccess(final SuccessT<User> userSuccessT) {
        final User user = userSuccessT.getT();
        DataRepository.getInstance().prefSaveToken(user.getToken());

        new Handler().postDelayed(() -> doAction(Action.OPEN_ACCOUNT_FRAGMENT, user), IConstants.Delay.SPLASH);
    }

    public DMLiveDataBag<User, RequestError> login(final User user) {
        return DataRepository.getInstance().apiLogin(getApplication().getApplicationContext(), user);
    }

    public void handleBiometricErrors(final User user, final IBIOConstants.FailedType type, final int helpCode, final CharSequence helpString) {
        switch (type) {
            case AUTHENTICATION_FAILED:
                break;
            default:
                doAction(Action.OPEN_LOGIN_FRAGMENT, user);
        }
    }
}
