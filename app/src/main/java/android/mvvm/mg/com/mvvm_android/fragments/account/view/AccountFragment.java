package android.mvvm.mg.com.mvvm_android.fragments.account.view;

import android.arch.lifecycle.ViewModelProviders;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentAccountBinding;
import android.mvvm.mg.com.mvvm_android.fragments.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.account.IAccountHandler;
import android.mvvm.mg.com.mvvm_android.fragments.account.viewModel.AccountViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;

public class AccountFragment extends BaseFragment implements IAccountHandler {

    private FragmentAccountBinding mBinding;
    private AccountViewModel mViewModel;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentAccountBinding.inflate(inflater, container, false);

        init();

        return mBinding.getRoot();
    }

    private void init() {
        showActionBar();
        setTitle(R.string.account_title);

        mViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setHandler(this);

        mViewModel.load(getArguments());
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

    @Override
    public void onLogoutClick(final View view) {
        mViewModel.logout();
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_loginFragment, null);
    }
}
