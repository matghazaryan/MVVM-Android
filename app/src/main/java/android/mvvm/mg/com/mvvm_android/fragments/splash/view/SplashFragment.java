package android.mvvm.mg.com.mvvm_android.fragments.splash.view;

import android.arch.lifecycle.ViewModelProviders;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.dialog.MVVMDialog;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.splash.viewModel.SplashViewModel;
import android.mvvm.mg.com.mvvm_android.models.Configs;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.Navigation;
import biometric.dm.com.dmbiometric.constants.IBIOConstants;
import biometric.dm.com.dmbiometric.listeners.IDMBiometricListener;
import biometric.dm.com.dmbiometric.main.DMBiometricManager;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

public class SplashFragment extends BaseFragment<SplashViewModel> {

    private SplashViewModel mViewModel;

    private DMBiometricManager<User> mBiometricManager;

    public SplashFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        init();

        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBiometricManager != null) {
            mBiometricManager.onStop();
        }
    }

    private void init() {
        hideActionBar();
        mViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        subscribes();
    }

    private void subscribes() {
        subscribeGetConfigs();

        mViewModel.<User>getAction(Action.DO_LOGIN).observe(this, user -> doLogin(mViewModel.login(user)));
        mViewModel.<User>getAction(Action.OPEN_ACCOUNT_FRAGMENT).observe(this, this::openAccount);
        mViewModel.<User>getAction(Action.OPEN_LOGIN_FRAGMENT).observe(this, user -> openLogin());
        mViewModel.<User>getAction(Action.OPEN_BIOMETRIC).observe(this, this::showBiometric);
    }

    private void subscribeGetConfigs() {
        final DMLiveDataBag<Configs, RequestError> liveDataBag = mViewModel.getConfigs();
        liveDataBag.getSuccessJsonResponse().observe(mActivity, jsonObject -> mViewModel.saveConfigs(jsonObject));
        liveDataBag.getErrorE().observe(mActivity, requestErrorErrorE -> mViewModel.handleErrors(requestErrorErrorE));
        liveDataBag.getNoInternetConnection().observe(mActivity, s -> MVVMDialog.showNoInternetDialog(mActivity));
    }

    private void doLogin(final DMLiveDataBag<User, RequestError> loginLiveDataBug) {
        loginLiveDataBug.getSuccessT().observe(this, loginSuccessSuccessT -> mViewModel.onLoginSuccess(loginSuccessSuccessT));
        loginLiveDataBug.getErrorE().observe(this, requestErrorErrorE -> mViewModel.handleErrors(requestErrorErrorE));
        loginLiveDataBug.getNoInternetConnection().observe(mActivity, s -> MVVMDialog.showNoInternetDialog(mActivity));
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
    private void showBiometric(final User user) {
        mBiometricManager = MVVMDialog.showBiometricForDecrypt(mActivity, new IDMBiometricListener<User>() {
            @Override
            public void onSuccessDecrypted(final User user) {
                doLogin(mViewModel.login(user));
            }

            @Override
            public void onFailed(final IBIOConstants.FailedType type, final int helpCode, final CharSequence helpString) {
                mViewModel.handleBiometricErrors(user, type, helpCode, helpString);
            }
        });
    }
}
