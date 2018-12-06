package android.mvvm.mg.com.mvvm_android.fragments.settings.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentSettingsBinding;
import android.mvvm.mg.com.mvvm_android.databinding.LanguageLayoutBinding;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.base.IBaseRequestListener;
import android.mvvm.mg.com.mvvm_android.fragments.settings.handler.ISettingsHandler;
import android.mvvm.mg.com.mvvm_android.fragments.settings.viewModel.SettingsViewModel;
import android.mvvm.mg.com.mvvm_android.utils.MVVMUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dm.dmnetworking.api_client.base.model.progress.FileProgress;
import com.eraz.camera.activities.ErazCameraActivity;
import com.eraz.camera.constants.IConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends BaseFragment<SettingsViewModel> implements ISettingsHandler {

    private FragmentSettingsBinding mBinding;

    private List<LanguageLayoutBinding> mLanguageLayoutBindingList = new ArrayList<>();

    public SettingsFragment() {
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        mViewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void initialize() {
        setTitle(R.string.settings_title);

        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setHandler(this);

        mViewModel.showProfilePhoto();

        initLanguageViews();
    }

    @Override
    public void subscribes() {
        mViewModel.<String>getAction(Action.ON_NEW_IMAGE_PATH).observe(this, this::subscribeOnFileUpload);
    }

    @Override
    public void onImageClick(final View view) {
        final Intent intent = new Intent(mActivity, ErazCameraActivity.class);

        intent.putExtra(IConstants.BundleKey.CAMERA_TYPE, IConstants.Camera.PHOTO_ONLY);
        intent.putExtra(IConstants.BundleKey.IS_MULTIPLY_GALLERY_IMAGE, false);
        intent.putExtra(IConstants.BundleKey.PICKER_TYPE, IConstants.Picker.DEFAULT);
        intent.putExtra(IConstants.BundleKey.ACTIONBAR_TITLE, getResources().getString(R.string.settings_gallery_title));
        intent.putExtra(IConstants.BundleKey.MAX_COUNT, 1);

        startActivityForResult(intent, RequestCode.CAMERA);
    }

    private void subscribeOnFileUpload(final String path) {
        handleRequest(mViewModel.sendImage(path), new IBaseRequestListener<String>() {
            @Override
            public void onSuccessJsonObject(final JSONObject jsonObject) {
                mViewModel.updateImagePath(path);
            }

            @Override
            public void onSuccessFileProgress(final FileProgress fileProgress) {
                mViewModel.updateProgress(fileProgress);
            }
        });
    }

    private void initLanguageViews() {
        mLanguageLayoutBindingList.add(createLanguageView(getString(R.string.settings_language_armenian), Language.HY));
        mLanguageLayoutBindingList.add(createLanguageView(getString(R.string.settings_language_english), Language.EN));
        mLanguageLayoutBindingList.add(createLanguageView(getString(R.string.settings_language_russian), Language.RU));
    }

    private LanguageLayoutBinding createLanguageView(final String name, final String code) {
        final LanguageLayoutBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.language_layout, null, false);
        binding.setName(name);
        binding.setIsVisible(code.equalsIgnoreCase(mViewModel.getLanguageCode()));
        binding.setHandler((View) -> changeLanguage(binding, code));

        mBinding.llLanguage.addView(binding.getRoot());

        return binding;
    }

    private void changeLanguage(final LanguageLayoutBinding layoutBinding, final String code) {
        for (final LanguageLayoutBinding binding : mLanguageLayoutBindingList) {
            binding.setIsVisible(false);
        }
        layoutBinding.setIsVisible(true);
        mViewModel.saveLanguageCode(code);
        MVVMUtils.updateLanguage(mActivity, code);
        setTitle(R.string.settings_title);
    }
}
