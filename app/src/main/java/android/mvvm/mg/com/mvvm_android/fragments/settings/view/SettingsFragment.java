package android.mvvm.mg.com.mvvm_android.fragments.settings.view;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentSettingsBinding;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.base.IBaseRequestListener;
import android.mvvm.mg.com.mvvm_android.fragments.settings.handler.ISettingsHandler;
import android.mvvm.mg.com.mvvm_android.fragments.settings.viewModel.SettingsViewModel;
import android.view.View;
import androidx.navigation.Navigation;
import com.dm.dmnetworking.api_client.base.model.progress.FileProgress;
import com.eraz.camera.activities.ErazCameraActivity;
import com.eraz.camera.constants.IConstants;
import org.json.JSONObject;

public class SettingsFragment extends BaseFragment<SettingsViewModel, FragmentSettingsBinding> implements ISettingsHandler {

    @Override
    protected int getLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    protected Class<SettingsViewModel> getViewModelClass() {
        return SettingsViewModel.class;
    }

    @Override
    protected void initBinding(final FragmentSettingsBinding binding, final SettingsViewModel viewModel) {
        binding.setViewModel(viewModel);
        binding.setHandler(this);
    }

    @Override
    public int getTitleRes() {
        return R.string.settings_title;
    }

    @Override
    public void subscribes(final LifecycleOwner owner) {
        mViewModel.<String>getAction(Action.ON_NEW_IMAGE_PATH).observe(owner, this::subscribeOnFileUpload);
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

    @Override
    public void onLanguageClick(final View view) {
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_settingsFragment_to_languageFragment);
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
}
