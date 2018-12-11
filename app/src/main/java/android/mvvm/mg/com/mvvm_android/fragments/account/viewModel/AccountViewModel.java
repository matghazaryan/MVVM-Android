package android.mvvm.mg.com.mvvm_android.fragments.account.viewModel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.constants.IConstants;
import android.mvvm.mg.com.mvvm_android.fragments.account.view.AccountFragmentArgs;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseViewModel;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.mvvm.mg.com.mvvm_android.repository.DataRepository;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

public class AccountViewModel extends BaseViewModel {

    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> imagePath = new ObservableField<>();

    public AccountViewModel(final @NonNull Application application) {
        super(application);
    }

    @Override
    public void initialize(final Bundle bundle) {
        showProfilePhoto();
        initData(bundle);
    }

    private void initData(final Bundle bundle) {
        if (bundle != null) {
            final User user = bundle.getParcelable(IConstants.BundleKey.USER);
            final String text;
            if (user != null) {
                text = user.getEmail();
            } else {
                final AccountFragmentArgs args = AccountFragmentArgs.fromBundle(bundle);
                text = args.getName();
            }

            this.email.set(text);
        }
    }

    private void showProfilePhoto() {
        imagePath.set(DataRepository.getInstance().prefGetProfilePhoto());
    }

    public DMLiveDataBag<String, RequestError> doLogout() {
        return DataRepository.getInstance().apiLogout(getApplication().getApplicationContext());
    }

    public void logout() {
        DataRepository.getInstance().prefSaveToken(null);
        DataRepository.getInstance().prefSetRemember(false);
        doAction(Action.OPEN_LOGIN_FRAGMENT, null);
    }
}
