package android.mvvm.mg.com.mvvm_android.fragments.settings.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

public class SettingsViewModel extends AndroidViewModel {

    public ObservableField<String> imagePath = new ObservableField<>();

    public SettingsViewModel(final @NonNull Application application) {
        super(application);
    }
}
