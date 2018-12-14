package android.mvvm.mg.com.mvvm_android.ui.fragments.login.view;

import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.dialog.MVVMDialog;
import android.mvvm.mg.com.mvvm_android.core.models.User;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentLoginBinding;
import android.mvvm.mg.com.mvvm_android.ui.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.ui.fragments.base.IBaseRequestListener;
import android.mvvm.mg.com.mvvm_android.ui.fragments.login.handler.ILoginHandler;
import android.mvvm.mg.com.mvvm_android.ui.fragments.login.viewModel.LoginViewModel;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;

import androidx.navigation.Navigation;
import biometric.dm.com.dmbiometric.constants.IBIOConstants;
import biometric.dm.com.dmbiometric.listeners.IDMBiometricListener;
import biometric.dm.com.dmbiometric.main.DMBiometricManager;

public class LoginFragment extends BaseFragment<LoginViewModel, FragmentLoginBinding> implements ILoginHandler {

    private DMBiometricManager<User> mBiometricManager;

    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    protected void initBinding(final FragmentLoginBinding binding, final LoginViewModel viewModel) {
        binding.setViewModel(viewModel);
        binding.setHandler(this);
    }

    @Override
    public int getTitleRes() {
        return R.string.login_title;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBiometricManager != null) {
            mBiometricManager.onStop();
        }
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
    public void subscribes(final LifecycleOwner owner) {
        mViewModel.<User>getAction(Action.OPEN_ACCOUNT_FRAGMENT).observe(owner, this::openAccount);
        mViewModel.<User>getAction(Action.OPEN_BIOMETRIC).observe(owner, this::showBiometric);
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
