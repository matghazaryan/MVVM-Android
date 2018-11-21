package android.mvvm.mg.com.mvvm_android.fragments.splash.view;

import android.arch.lifecycle.ViewModelProviders;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.dialog.MVVMAlertDialog;
import android.mvvm.mg.com.mvvm_android.fragments.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.splash.viewModel.SplashViewModel;
import android.mvvm.mg.com.mvvm_android.models.Configs;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

import alertdialog.dm.com.dmalertdialog.DMAlertDialogItem;
import alertdialog.dm.com.dmalertdialog.configs.DMBaseDialogConfigs;
import alertdialog.dm.com.dmalertdialog.listeners.DMBaseClickListener;
import androidx.navigation.Navigation;

public class SplashFragment extends BaseFragment {

    private SplashViewModel mViewModel;

    public SplashFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        init();

        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    private void init() {
        hideActionBar();
        mViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        subscribes();
    }

    private void subscribes() {
        final DMLiveDataBag<Configs, RequestError> liveDataBag = mViewModel.getConfigs();

        liveDataBag.getSuccessJsonResponse().observe(mActivity, jsonObject -> {
            mViewModel.saveConfigs(jsonObject);
            mViewModel.openNextPage();
        });

        liveDataBag.getErrorE().observe(mActivity, requestErrorErrorE ->
                new MVVMAlertDialog().showErrorDialog(new DMBaseDialogConfigs<>(mActivity)
                        .setContent(requestErrorErrorE != null ? requestErrorErrorE.getE().getMessage() : getString(R.string.error_general_error))));

        liveDataBag.getNoInternetConnection().observe(mActivity, s
                -> new MVVMAlertDialog().showWarningDialog(new DMBaseDialogConfigs<>(mActivity)
                .setContentRes(R.string.dialog_no_internet_connection)
                .setListener(new DMBaseClickListener<DMAlertDialogItem>() {
                    @Override
                    public void onPositive() {
                        mActivity.finishAffinity();
                    }
                })));


        mViewModel.getDoLogin().observe(this, user -> doLogin(mViewModel.login()));

        mViewModel.getOpenAccountFragment().observe(this, this::openAccount);
        mViewModel.getOpenLoginFragment().observe(this, user -> openLogin());
    }

    private void doLogin(final DMLiveDataBag<User, RequestError> loginLiveDataBug) {
        loginLiveDataBug.getSuccessT().observe(this, loginSuccessSuccessT -> mViewModel.onLoginSuccess(loginSuccessSuccessT));
        loginLiveDataBug.getErrorE().observe(this, requestErrorErrorE -> mViewModel.onError(requestErrorErrorE));
        loginLiveDataBug.getNoInternetConnection().observe(mActivity,
                s -> new MVVMAlertDialog().showWarningDialog(new DMBaseDialogConfigs<>(mActivity).setContentRes(R.string.dialog_no_internet_connection)));
    }

    private void openLogin() {
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_splashFragment_to_loginFragment);
    }

    private void openAccount(final User user) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKey.USER, user);
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_splashFragment_to_accountFragment, bundle);
    }
}
