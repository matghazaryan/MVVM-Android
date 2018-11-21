package android.mvvm.mg.com.mvvm_android.fragments.account.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.constants.IConstants;
import android.mvvm.mg.com.mvvm_android.fragments.account.view.AccountFragmentArgs;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.mvvm.mg.com.mvvm_android.repository.DataRepository;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class AccountViewModel extends AndroidViewModel {

    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> imagePath = new ObservableField<>();

    public AccountViewModel(final @NonNull Application application) {
        super(application);
    }

    public void load(final Bundle bundle) {
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
            setEmail(text);
        }
    }

    public void setEmail(final String email) {
        this.email.set(email);
    }

    private void showProfilePhoto() {
        final String path = DataRepository.getInstance().getProfilePhoto();
        imagePath.set(path);
    }

    public void logout() {
        DataRepository.getInstance().saveToken(null);
        DataRepository.getInstance().setRemember(false);
    }
}
