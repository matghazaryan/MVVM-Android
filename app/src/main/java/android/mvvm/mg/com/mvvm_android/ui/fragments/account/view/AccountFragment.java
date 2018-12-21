package android.mvvm.mg.com.mvvm_android.ui.fragments.account.view;

import android.Manifest;
import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.base.IBaseOnPermissionSuccessListener;
import android.mvvm.mg.com.mvvm_android.core.base.IBaseRequestListener;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentAccountBinding;
import android.mvvm.mg.com.mvvm_android.ui.fragments.account.handler.IAccountHandler;
import android.mvvm.mg.com.mvvm_android.ui.fragments.account.viewModel.AccountViewModel;
import android.mvvm.mg.com.mvvm_android.ui.fragments.base.MVVMBasePermissionFragment;
import android.view.View;

import org.json.JSONObject;

import androidx.navigation.Navigation;

public class AccountFragment extends MVVMBasePermissionFragment<AccountViewModel, FragmentAccountBinding> implements IAccountHandler {

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_account;
    }

    @Override
    protected Class<AccountViewModel> getViewModelClass() {
        return AccountViewModel.class;
    }

    @Override
    protected void setBinding(final FragmentAccountBinding binding, final AccountViewModel viewModel) {
        binding.setViewModel(viewModel);
        binding.setHandler(this);
    }

    @Override
    public int getTitleRes() {
        return R.string.account_title;
    }

    @Override
    public void subscribers(final LifecycleOwner owner) {
        mViewModel.getAction(Action.OPEN_LOGIN_FRAGMENT).observe(owner, o -> openLoginPage());
    }

    @Override
    public void onCardsClick(final View view) {
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_cardsFragment);
    }

    @Override
    public void onPaymentHistoryClick(final View view) {
        accessToPermission(new IBaseOnPermissionSuccessListener() {
            @Override
            public void onPermissionsGranted() {
                Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_paymentHistoryFragment);
            }
        }, PermissionRequestCode.STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    @Override
    public void onSettingsClick(final View view) {
        accessToPermission(new IBaseOnPermissionSuccessListener() {
            @Override
            public void onPermissionsGranted() {
                Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_fileUploadFragment);
            }
        }, PermissionRequestCode.LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void openLoginPage() {
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_loginFragment);
    }

    @Override
    public void onLogoutClick(final View view) {
        mViewModel.logout();
        handleRequestFor(mViewModel.doLogout(), new IBaseRequestListener<String>() {
            @Override
            public void onSuccessJsonObject(final JSONObject jsonObject) {
            }
        });
    }
}
