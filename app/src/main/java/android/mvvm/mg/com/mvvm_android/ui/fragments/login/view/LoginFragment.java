package android.mvvm.mg.com.mvvm_android.ui.fragments.login.view;

import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseFragment;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseIRequestListener;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.core.dialog.MVVMDialog;
import android.mvvm.mg.com.mvvm_android.core.models.User;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentLoginBinding;
import android.mvvm.mg.com.mvvm_android.ui.fragments.account.viewModel.AccountViewModel;
import android.mvvm.mg.com.mvvm_android.ui.fragments.login.handler.ILoginHandler;
import android.mvvm.mg.com.mvvm_android.ui.fragments.login.viewModel.LoginViewModel;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import androidx.navigation.Navigation;
import biometric.dm.com.dmbiometric.DMBIOIConstants;
import biometric.dm.com.dmbiometric.DMBIOIListener;
import biometric.dm.com.dmbiometric.DMBIOManager;


public class LoginFragment extends DMBaseFragment<LoginViewModel, FragmentLoginBinding> implements ILoginHandler {

    private DMBIOManager<User> mBiometricManager;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_login;
    }

    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    protected void setBinding(final FragmentLoginBinding binding, final LoginViewModel viewModel) {
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
        handleRequestFor(mViewModel.apiLogin(), new DMBaseIRequestListener<User>() {
            @Override
            public void onSuccess(final User user) {
                mViewModel.onSuccessLogin(user);
            }
        });
    }

    @Override
    public void subscribers(final LifecycleOwner owner) {
        mViewModel.<User>getAction(IMVVMConstants.Action.OPEN_ACCOUNT_FRAGMENT).observe(owner, this::openAccount);
        mViewModel.<User>getAction(IMVVMConstants.Action.OPEN_BIOMETRIC).observe(owner, this::showBiometric);
    }

    private void openAccount(final User user) {
        //2 way for send data

//        final LoginFragmentDirections.ActionLoginFragmentToAccountFragment action = LoginFragmentDirections.actionLoginFragmentToAccountFragment();
//        action.setName("Henri");
//        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(action);           //send data (without custom object)

//        final Bundle bundle = new Bundle();
//        bundle.putParcelable(IMVVMConstants.BundleKey.USER, user);
//        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_accountFragment, bundle); //Send custom object

        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_accountFragment);
        sendSharedData(AccountViewModel.class, IMVVMConstants.SendCode.LOGIN_TO_ACCOUNT, user);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showBiometric(final User user) {
        mBiometricManager = MVVMDialog.showBiometricForEncrypt(mActivity, user, new DMBIOIListener<User>() {
            @Override
            public void onSuccessEncrypted() {
                openAccount(user);
            }

            @Override
            public void onFailed(final DMBIOIConstants.FailedType type, final int helpCode, final CharSequence helpString) {
                mViewModel.handleBiometricErrors(user, type, helpCode, helpString);
            }
        });
    }
}
