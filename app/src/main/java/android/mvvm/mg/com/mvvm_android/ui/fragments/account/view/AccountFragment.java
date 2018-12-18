package android.mvvm.mg.com.mvvm_android.ui.fragments.account.view;

import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentAccountBinding;
import android.mvvm.mg.com.mvvm_android.ui.fragments.account.handler.IAccountHandler;
import android.mvvm.mg.com.mvvm_android.ui.fragments.account.viewModel.AccountViewModel;
import android.mvvm.mg.com.mvvm_android.ui.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.ui.fragments.base.IBaseRequestListener;
import android.view.View;
import androidx.navigation.Navigation;
import org.json.JSONObject;

public class AccountFragment extends BaseFragment<AccountViewModel, FragmentAccountBinding> implements IAccountHandler {

    @Override
    protected int getLayout() {
        return R.layout.fragment_account;
    }

    @Override
    protected Class<AccountViewModel> getViewModelClass() {
        return AccountViewModel.class;
    }

    @Override
    protected void initBinding(final FragmentAccountBinding binding, final AccountViewModel viewModel) {
        binding.setViewModel(viewModel);
        binding.setHandler(this);
    }

    @Override
    public int getTitleRes() {
        return R.string.account_title;
    }

    @Override
    public void subscribes(final LifecycleOwner owner) {
        mViewModel.getAction(Action.OPEN_LOGIN_FRAGMENT).observe(owner, o -> openLoginPage());
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
        mViewModel.logout();
        makeRequest(mViewModel.doLogout(), new IBaseRequestListener<String>() {
            @Override
            public void onSuccessJsonObject(final JSONObject jsonObject) {
            }
        });
    }
}
