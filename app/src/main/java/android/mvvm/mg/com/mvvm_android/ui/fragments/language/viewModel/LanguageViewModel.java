package android.mvvm.mg.com.mvvm_android.ui.fragments.language.viewModel;

import android.app.Application;
import android.mvvm.mg.com.mvvm_android.core.base.BaseViewModel;
import android.mvvm.mg.com.mvvm_android.core.repository.DataRepository;
import android.support.annotation.NonNull;

public class LanguageViewModel extends BaseViewModel {

    public LanguageViewModel(final @NonNull Application application) {
        super(application);
    }

    public void saveLanguageCode(final String code) {
        DataRepository.preference().languageCode(code);
    }

    public String getLanguageCode() {
        return DataRepository.preference().getLanguageCode();
    }
}
