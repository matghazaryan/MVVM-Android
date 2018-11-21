package android.mvvm.mg.com.mvvm_android.fragments.splash.viewModel;

import alertdialog.dm.com.dmalertdialog.configs.DMBaseDialogConfigs;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.constants.IConstants;
import android.mvvm.mg.com.mvvm_android.dialog.MVVMAlertDialog;
import android.mvvm.mg.com.mvvm_android.models.Configs;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.mvvm.mg.com.mvvm_android.repository.DataRepository;
import android.os.Handler;
import android.support.annotation.NonNull;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;
import com.dm.dmnetworking.api_client.base.model.error.ErrorE;
import com.dm.dmnetworking.api_client.base.model.success.SuccessT;
import org.json.JSONObject;

public class SplashViewModel extends AndroidViewModel {

    private MutableLiveData<User> openLoginFragment = new MutableLiveData<>();
    private MutableLiveData<User> openAccountFragment = new MutableLiveData<>();
    private MutableLiveData<User> doLogin = new MutableLiveData<>();

    public SplashViewModel(final @NonNull Application application) {
        super(application);
    }

    public void saveConfigs(final JSONObject jsonObject) {
        if (jsonObject != null) {
            DataRepository.getInstance().saveConfigs(jsonObject.toString());
        } else {
            new MVVMAlertDialog().showErrorDialog(new DMBaseDialogConfigs<>(getApplication().getApplicationContext())
                    .setContentRes(R.string.error_general_error));
        }
    }

    public DMLiveDataBag<Configs, RequestError> getConfigs() {
        return DataRepository.getInstance().getConfigs(getApplication().getApplicationContext());
    }

    public void openNextPage() {
        if (DataRepository.getInstance().isCheckedRemember()) {
            doLogin.setValue(null);
        } else {
            new Handler().postDelayed(() -> openLoginFragment.setValue(null), IConstants.Delay.SPLASH);
        }
    }

    public void onLoginSuccess(final SuccessT<User> userSuccessT) {
        final User user = userSuccessT.getT();
        DataRepository.getInstance().saveToken(user.getToken());

        new Handler().postDelayed(() -> openAccountFragment.setValue(user), IConstants.Delay.SPLASH);
    }

    public DMLiveDataBag<User, RequestError> login() {
        final String email = DataRepository.getInstance().getEmail();
        final String password = DataRepository.getInstance().getPassword();

        return DataRepository.getInstance().login(getApplication().getApplicationContext(), new User(email, password));
    }

    public void onError(final ErrorE<RequestError> errorErrorE) {
        final RequestError error = errorErrorE.getE();
        if (error != null) {
            new MVVMAlertDialog().showErrorDialog(new DMBaseDialogConfigs<>(getApplication().getApplicationContext())
                    .setContent(error.getMessage()));
        } else {
            new MVVMAlertDialog().showErrorDialog(new DMBaseDialogConfigs<>(getApplication().getApplicationContext())
                    .setContentRes(R.string.error_general_error));
        }
    }

    public MutableLiveData<User> getOpenLoginFragment() {
        return openLoginFragment;
    }

    public MutableLiveData<User> getOpenAccountFragment() {
        return openAccountFragment;
    }

    public MutableLiveData<User> getDoLogin() {
        return doLogin;
    }
}
