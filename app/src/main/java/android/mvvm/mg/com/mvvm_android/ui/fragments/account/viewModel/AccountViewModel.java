package android.mvvm.mg.com.mvvm_android.ui.fragments.account.viewModel;

import android.app.Application;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseViewModel;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.core.models.User;
import android.mvvm.mg.com.mvvm_android.core.models.error.RequestError;
import android.mvvm.mg.com.mvvm_android.core.repository.DataRepository;
import android.mvvm.mg.com.mvvm_android.ui.fragments.account.view.AccountFragmentArgs;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.dm.dmnetworking.DMNetworkLiveDataBag;


public class AccountViewModel extends DMBaseViewModel {

    public final ObservableField<String> email = new ObservableField<>();
    public final ObservableField<String> imagePath = new ObservableField<>();

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
            final User user = bundle.getParcelable(IMVVMConstants.BundleKey.USER);
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
        imagePath.set(DataRepository.preference().getProfilePhoto());
    }

    public DMNetworkLiveDataBag<String, RequestError> doLogout() {
        return DataRepository.api().logout(getApplication().getApplicationContext());
    }

    public void logout() {
        DataRepository.preference().saveToken(null);
        DataRepository.preference().setRemember(false);
        doAction(Action.OPEN_LOGIN_FRAGMENT, null);
    }
}
