package android.mvvm.mg.com.mvvm_android.ui.fragments.account.view;

import android.Manifest;
import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseIOnPermissionSuccessListener;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseIOnSharedDataListener;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseIRequestListener;
import android.mvvm.mg.com.mvvm_android.core.base.DMBasePermissionFragment;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.core.models.User;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentAccountBinding;
import android.mvvm.mg.com.mvvm_android.ui.fragments.account.handler.IAccountHandler;
import android.mvvm.mg.com.mvvm_android.ui.fragments.account.viewModel.AccountViewModel;
import android.util.Log;
import android.view.View;
import androidx.navigation.Navigation;
import org.json.JSONObject;

public class AccountFragment extends DMBasePermissionFragment<AccountViewModel, FragmentAccountBinding> implements IAccountHandler {

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
    public void initialize() {
        getSharedDataOnActiveScreenAlways(IMVVMConstants.SendCode.LOGIN_TO_ACCOUNT, (DMBaseIOnSharedDataListener<User>) user -> mViewModel.initUser(user));
        getSharedDataImmediately(IMVVMConstants.SendCode.CARD_TO_ACCOUNT, (DMBaseIOnSharedDataListener<String>) s -> Log.d("myLogs", s));
    }

    @Override
    public void subscribers(final LifecycleOwner owner) {
        mViewModel.getAction(IMVVMConstants.Action.OPEN_LOGIN_FRAGMENT).observe(owner, o -> openLoginPage());

    }

    @Override
    public void onCardsClick(final View view) {
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_cardsFragment);
    }

    @Override
    public void onTransactionClick(final View view) {
        accessToPermission(new DMBaseIOnPermissionSuccessListener() {
            @Override
            public void onPermissionsGranted() {
                Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_paymentHistoryFragment);
                sendSharedData(IMVVMConstants.SendCode.ACCOUNT_TO_TRANSACTION, "Message receive transaction");
            }
        }, IMVVMConstants.PermissionRequestCode.STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onSettingsClick(final View view) {
        accessToPermission(new DMBaseIOnPermissionSuccessListener() {
            @Override
            public void onPermissionsGranted() {
                Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_fileUploadFragment);
                sendSharedData(IMVVMConstants.SendCode.ACCOUNT_TO_SETTINGS, "Message receive settings");
            }
        }, IMVVMConstants.PermissionRequestCode.LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void openLoginPage() {
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_loginFragment);
    }

    @Override
    public void onLogoutClick(final View view) {
        mViewModel.logout();
        handleRequestFor(mViewModel.doLogout(), new DMBaseIRequestListener<String>() {
            @Override
            public void onSuccessJsonObject(final JSONObject jsonObject) {
            }
        });
    }
}
