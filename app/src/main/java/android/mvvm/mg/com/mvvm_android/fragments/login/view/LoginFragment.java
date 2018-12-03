package android.mvvm.mg.com.mvvm_android.fragments.login.view;

import android.arch.lifecycle.ViewModelProviders;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentLoginBinding;
import android.mvvm.mg.com.mvvm_android.dialog.MVVMDialog;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.base.IBaseRequestListener;
import android.mvvm.mg.com.mvvm_android.fragments.login.handler.ILoginHandler;
import android.mvvm.mg.com.mvvm_android.fragments.login.viewModel.LoginViewModel;
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

public class LoginFragment extends BaseFragment<LoginViewModel> implements ILoginHandler {

    private FragmentLoginBinding mBinding;

    private DMBiometricManager<User> mBiometricManager;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

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
        handleRequest(mViewModel.login(), new IBaseRequestListener<User>() {
            @Override
            public void onSuccess(final User user) {
                mViewModel.onSuccessLogin(user);
            }
        });
    }

    @Override
    public void onCheckedChange(final boolean isChecked) {
        mViewModel.setRemember(isChecked);
    }

    private void subscribes() {
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
