package android.mvvm.mg.com.mvvm_android.fragments.login.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentLoginBinding;
import android.mvvm.mg.com.mvvm_android.dialog.MVVMDialog;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.login.handler.ILoginHandler;
import android.mvvm.mg.com.mvvm_android.fragments.login.viewModel.LoginViewModel;
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

public class LoginFragment extends BaseFragment<LoginViewModel> implements ILoginHandler {

    private LoginViewModel mViewModel;

    private FragmentLoginBinding mBinding;

    private DMBiometricManager<User> mBiometricManager;

    public LoginFragment() {
        //TODO BaseView modelener@ sharunakel, dialogner-i optimal kanchuma@, error handling@
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentLoginBinding.inflate(inflater, container, false);

        init();

        return mBinding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBiometricManager != null) {
            mBiometricManager.onStop();
        }
    }

    private void init() {
        showActionBar();
        setTitle(R.string.login_title);

        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setHandler(this);

        subscribes();
    }

    @Override
    public void onClickLogin(final View view) {
        doLogin(mViewModel.login());
    }

    @Override
    public void onCheckedChange(final boolean isChecked) {
        mViewModel.setRemember(isChecked);
    }

    private void subscribes() {
        mViewModel.<Boolean>getAction(Action.EMAIL_AND_PASSWORD).observe(this, aBoolean -> mViewModel.updateButtonStatus(aBoolean));
        mViewModel.<User>getAction(Action.OPEN_ACCOUNT_FRAGMENT).observe(this, this::openAccount);
        mViewModel.<User>getAction(Action.OPEN_BIOMETRIC).observe(this, this::showBiometric);
    }

    private void openAccount(final User user) {
        //2 way for send data

//        final LoginFragmentDirections.ActionLoginFragmentToAccountFragment action = LoginFragmentDirections.actionLoginFragmentToAccountFragment();
//        action.setName("Henri");
//        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(action);           //send data (without custom object)

        final Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKey.USER, user);
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_accountFragment, bundle); //Send custom object
    }

    private void doLogin(final DMLiveDataBag<User, RequestError> loginLiveDataBug) {
        loginLiveDataBug.getSuccessT().observe(this, loginSuccessSuccessT -> mViewModel.onLoginSuccess(loginSuccessSuccessT));
        loginLiveDataBug.getErrorE().observe(this, requestErrorErrorE -> mViewModel.handleLoginErrors(requestErrorErrorE));
        loginLiveDataBug.getNoInternetConnection().observe(mActivity, s -> MVVMDialog.showNoInternetDialog(mActivity));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showBiometric(final User user) {
        mBiometricManager = MVVMDialog.showBiometricForEncrypt(mActivity, user, new IDMBiometricListener<User>() {
            @Override
            public void onSuccessEncrypted() {
                openAccount(user);
            }

            @Override
            public void onFailed(final IBIOConstants.FailedType type, final int helpCode, final CharSequence helpString) {
                mViewModel.handleBiometricErrors(user, type, helpCode, helpString);
            }
        });
    }
}
