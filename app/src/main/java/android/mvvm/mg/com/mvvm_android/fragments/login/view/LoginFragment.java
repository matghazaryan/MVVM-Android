package android.mvvm.mg.com.mvvm_android.fragments.login.view;

import android.arch.lifecycle.ViewModelProviders;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentLoginBinding;
import android.mvvm.mg.com.mvvm_android.dialog.MVVMAlertDialog;
import android.mvvm.mg.com.mvvm_android.fragments.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.login.ILoginHandler;
import android.mvvm.mg.com.mvvm_android.fragments.login.viewModel.LoginViewModel;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

import alertdialog.dm.com.dmalertdialog.configs.DMBaseDialogConfigs;
import androidx.navigation.Navigation;

public class LoginFragment extends BaseFragment implements ILoginHandler {

    private LoginViewModel mViewModel;

    private FragmentLoginBinding mBinding;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentLoginBinding.inflate(inflater, container, false);

        init();

        return mBinding.getRoot();
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
        mViewModel.getEmailAndPasswordObservable().observe(this, aBoolean -> mViewModel.updateButtonStatus(aBoolean));
        mViewModel.getOpenNextPage().observe(this, this::openAccount);
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
        loginLiveDataBug.getErrorE().observe(this, requestErrorErrorE -> mViewModel.onError(requestErrorErrorE));
        loginLiveDataBug.getNoInternetConnection().observe(mActivity,
                s -> new MVVMAlertDialog().showWarningDialog(new DMBaseDialogConfigs<>(mActivity).setContentRes(R.string.dialog_no_internet_connection)));
    }
}
