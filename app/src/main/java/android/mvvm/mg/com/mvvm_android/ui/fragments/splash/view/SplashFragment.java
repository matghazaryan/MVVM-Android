package android.mvvm.mg.com.mvvm_android.ui.fragments.splash.view;

import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.dialog.MVVMDialog;
import android.mvvm.mg.com.mvvm_android.core.models.Configs;
import android.mvvm.mg.com.mvvm_android.core.models.error.RequestError;
import android.mvvm.mg.com.mvvm_android.core.models.User;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentSplashBinding;
import android.mvvm.mg.com.mvvm_android.ui.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.ui.fragments.base.IBaseRequestListener;
import android.mvvm.mg.com.mvvm_android.ui.fragments.splash.viewModel.SplashViewModel;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import androidx.navigation.Navigation;
import biometric.dm.com.dmbiometric.constants.IBIOConstants;
import biometric.dm.com.dmbiometric.listeners.IDMBiometricListener;
import biometric.dm.com.dmbiometric.main.DMBiometricManager;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;
import org.json.JSONObject;

public class SplashFragment extends BaseFragment<SplashViewModel, FragmentSplashBinding> {

    private DMBiometricManager<User> mBiometricManager;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_splash;
    }

    @Override
    protected Class<SplashViewModel> getViewModelClass() {
        return SplashViewModel.class;
    }

    @Override
    protected void setBinding(final FragmentSplashBinding binding, final SplashViewModel viewModel) {
    }

    @Override
    public boolean isShowActionBar() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBiometricManager != null) {
            mBiometricManager.onStop();
        }
    }

    @Override
    public void initialize() {
        getConfigs();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void subscribers(final LifecycleOwner owner) {
        mViewModel.<User>getAction(Action.OPEN_ACCOUNT_FRAGMENT).observe(owner, this::openAccount);
        mViewModel.<User>getAction(Action.OPEN_LOGIN_FRAGMENT).observe(owner, user -> openLogin());
        mViewModel.<User>getAction(Action.OPEN_BIOMETRIC).observe(owner, t -> showBiometric());
    }

    private void getConfigs() {
        handleRequestFor(mViewModel.apiConfigs(), new IBaseRequestListener<Configs>() {
            @Override
            public void onSuccessJsonObject(final JSONObject jsonObject) {
                mViewModel.prefSaveConfigs(jsonObject);
            }
        });
    }

    private void doLogin(final DMLiveDataBag<User, RequestError> loginLiveDataBug) {
        handleRequestFor(loginLiveDataBug, new IBaseRequestListener<User>() {
            @Override
            public void onSuccess(final User user) {
                mViewModel.onLoginSuccess(user);
            }
        });
    }

    private void openLogin() {
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_splashFragment_to_loginFragment);
    }

    private void openAccount(final User user) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKey.USER, user);
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_splashFragment_to_accountFragment, bundle);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showBiometric() {
        mBiometricManager = MVVMDialog.showBiometricForDecrypt(mActivity, new IDMBiometricListener<User>() {
            @Override
            public void onSuccessDecrypted(final User user) {
                doLogin(mViewModel.apiLogin(user));
            }

            @Override
            public void onFailed(final IBIOConstants.FailedType type, final int helpCode, final CharSequence helpString) {
                mViewModel.handleBiometricErrors(type, helpCode, helpString);
            }
        });
    }
}
