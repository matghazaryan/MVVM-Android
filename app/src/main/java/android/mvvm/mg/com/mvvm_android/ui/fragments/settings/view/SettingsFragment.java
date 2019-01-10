package android.mvvm.mg.com.mvvm_android.ui.fragments.settings.view;

import android.content.Intent;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseFragment;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseIOnSharedDataListener;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseIRequestListener;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentSettingsBinding;
import android.mvvm.mg.com.mvvm_android.ui.fragments.settings.handler.ISettingsHandler;
import android.mvvm.mg.com.mvvm_android.ui.fragments.settings.viewModel.SettingsViewModel;
import android.util.Log;
import android.view.View;
import androidx.navigation.Navigation;
import com.dm.camera.activities.DMCameraActivity;
import com.dm.camera.constants.IDMCameraConstants;
import com.dm.dmnetworking.model.progress.FileProgress;
import org.json.JSONObject;

public class SettingsFragment extends DMBaseFragment<SettingsViewModel, FragmentSettingsBinding> implements ISettingsHandler {

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_settings;
    }

    @Override
    protected Class<SettingsViewModel> getViewModelClass() {
        return SettingsViewModel.class;
    }

    @Override
    protected void setBinding(final FragmentSettingsBinding binding, final SettingsViewModel viewModel) {
        binding.setViewModel(viewModel);
        binding.setHandler(this);
    }

    @Override
    public void initialize() {
        //Receive data from AccountViewModel
        getSharedDataOnActiveScreen(IMVVMConstants.SendCode.ACCOUNT_TO_SETTINGS, (DMBaseIOnSharedDataListener<String>) s -> Log.d("myLogs", s));
    }

    @Override
    public int getTitleRes() {
        return R.string.settings_title;
    }

    @Override
    public void onImageClick(final View view) {
        final Intent intent = new Intent(mActivity, DMCameraActivity.class);

        intent.putExtra(IDMCameraConstants.BundleKey.CAMERA_TYPE, IDMCameraConstants.Camera.PHOTO_ONLY);
        intent.putExtra(IDMCameraConstants.BundleKey.IS_MULTIPLY_GALLERY_IMAGE, false);
        intent.putExtra(IDMCameraConstants.BundleKey.PICKER_TYPE, IDMCameraConstants.Picker.DEFAULT);
        intent.putExtra(IDMCameraConstants.BundleKey.ACTIONBAR_TITLE, getResources().getString(R.string.settings_gallery_title));
        intent.putExtra(IDMCameraConstants.BundleKey.MAX_COUNT, 1);

        startActivityForResult(intent, IMVVMConstants.RequestCode.CAMERA);
    }

    @Override
    public void onLanguageClick(final View view) {
        Navigation.findNavController(mActivity, R.id.nav_host_fragment).navigate(R.id.action_settingsFragment_to_languageFragment);
    }

    @Override
    public void onSavePictureClick(final View view) {
        subscribeOnFileUpload();
    }

    private void subscribeOnFileUpload() {
        handleRequestFor(mViewModel.uploadImage(), new DMBaseIRequestListener<String>() {
            @Override
            public void onSuccessJsonObject(final JSONObject jsonObject) {
                mViewModel.updateImagePath();
            }

            @Override
            public void onSuccessFileProgress(final FileProgress fileProgress) {
                mViewModel.updateProgress(fileProgress);
            }
        });
    }
}
