package android.mvvm.mg.com.mvvm_android.fragments.language.viewModel;

import android.app.Application;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseViewModel;
import android.mvvm.mg.com.mvvm_android.repository.DataRepository;
import android.support.annotation.NonNull;

public class LanguageViewModel extends BaseViewModel {

    public LanguageViewModel(final @NonNull Application application) {
        super(application);
    }

    public void saveLanguageCode(final String code) {
        DataRepository.getInstance().prefLanguageCode(code);
    }

    public String getLanguageCode() {
        return DataRepository.getInstance().prefGetLanguageCode();
    }
}
