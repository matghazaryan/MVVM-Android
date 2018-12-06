package android.mvvm.mg.com.mvvm_android.fragments.account.view;

import android.arch.lifecycle.ViewModelProviders;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentAccountBinding;
import android.mvvm.mg.com.mvvm_android.fragments.account.handler.IAccountHandler;
import android.mvvm.mg.com.mvvm_android.fragments.account.viewModel.AccountViewModel;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.base.IBaseRequestListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import androidx.navigation.Navigation;

public class AccountFragment extends BaseFragment<AccountViewModel> implements IAccountHandler {

    private FragmentAccountBinding mBinding;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mBinding = FragmentAccountBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void initialize() {
        showActionBar();
        setTitle(R.string.account_title);

        mViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setHandler(this);

        mViewModel.load(getArguments());
    }

    @Override
    public void subscribes() {
        mViewModel.getAction(Action.OPEN_LOGIN_FRAGMENT).observe(mActivity, o -> openLoginPage());
    }

    @Override
    public void onCardsClick(final View view) {
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_cardsFragment);
    }

    @Override
    public void onPaymentHistoryClick(final View view) {
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_paymentHistoryFragment);
    }

    @Override
    public void onSettingsClick(final View view) {
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_fileUploadFragment);
    }

    private void openLoginPage() {
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_loginFragment);
    }

    @Override
    public void onLogoutClick(final View view) {
        handleRequest(mViewModel.doLogout(), new IBaseRequestListener<String>() {
            @Override
            public void onSuccessJsonObject(final JSONObject jsonObject) {
                mViewModel.logout();
            }
        });
    }
}
