package android.mvvm.mg.com.mvvm_android.fragments.settings.view;

import android.arch.lifecycle.ViewModelProviders;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentSettingsBinding;
import android.mvvm.mg.com.mvvm_android.fragments.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.settings.ISettingsHandler;
import android.mvvm.mg.com.mvvm_android.fragments.settings.viewModel.SettingsViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends BaseFragment implements ISettingsHandler {

    private SettingsViewModel mViewModel;
    private FragmentSettingsBinding mBinding;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentSettingsBinding.inflate(inflater, container, false);

        init();

        return mBinding.getRoot();
    }

    private void init() {
        setTitle(R.string.settings_title);

        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setHandler(this);
    }

    @Override
    public void onImageClick(final View view) {

    }
}
