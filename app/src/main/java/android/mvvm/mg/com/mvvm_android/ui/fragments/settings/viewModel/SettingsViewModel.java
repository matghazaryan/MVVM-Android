package android.mvvm.mg.com.mvvm_android.ui.fragments.settings.viewModel;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.core.models.error.RequestError;
import android.mvvm.mg.com.mvvm_android.core.repository.DataRepository;
import android.mvvm.mg.com.mvvm_android.core.utils.MVVMFileUtils;
import android.mvvm.mg.com.mvvm_android.core.utils.MVVMUtils;
import android.mvvm.mg.com.mvvm_android.ui.fragments.base.BaseViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dm.camera.constants.IDMCameraConstants;
import com.dm.camera.models.CapturePhoto;
import com.dm.camera.models.MediaData;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;
import com.dm.dmnetworking.api_client.base.model.progress.FileProgress;

public class SettingsViewModel extends BaseViewModel {

    private String newPath;

    public final ObservableField<String> imagePath = new ObservableField<>();
    public final ObservableField<Integer> progress = new ObservableField<>();
    public final ObservableField<String> language = new ObservableField<>();
    public final ObservableField<Boolean> isSaveButtonVisible = new ObservableField<>();

    public SettingsViewModel(final @NonNull Application application) {
        super(application);
    }

    @Override
    public void initialize() {
        imagePath.set(newPath == null ? DataRepository.preference().getProfilePhoto() : newPath);
        language.set(MVVMUtils.getLanguageName(getApplication().getApplicationContext(),
                DataRepository.preference().getLanguageCode()));
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = null;
            if (data != null) {
                bundle = data.getExtras();
            }

            switch (requestCode) {
                case IMVVMConstants.RequestCode.CAMERA:
                    setImage(bundle);
                    break;
            }
        }
    }

    private void setImage(final Bundle bundle) {
        if (bundle != null) {
            final MediaData mediaData = bundle.getParcelable(IDMCameraConstants.BundleKey.MEDIA_DATA);
            if (mediaData != null) {
                String path = null;
                final CapturePhoto capturePhoto = mediaData.getCapturePhoto();
                if (capturePhoto != null) {
                    path = MVVMFileUtils.onSavePicture(capturePhoto.getCapturedPhoto());
                } else if (mediaData.getSelectedImagesOrVideosPathList() != null && mediaData.getSelectedImagesOrVideosPathList().size() == 1) {
                    path = mediaData.getSelectedImagesOrVideosPathList().get(0);
                }

                if (path != null) {
                    displayNewImage(path);
                } else {
                    doAction(Action.OPEN_ERROR_DIALOG, getApplication().getApplicationContext().getString(R.string.error_file_not_found));
                }
            }
        }
    }

    public DMLiveDataBag<String, RequestError> uploadImage() {
        return DataRepository.api().sendImage(getApplication().getApplicationContext(), newPath);
    }

    public void updateImagePath() {
        DataRepository.preference().saveProfilePhoto(newPath);
        isSaveButtonVisible.set(false);
        newPath = null;
    }

    private void displayNewImage(final String newPath) {
        this.newPath = newPath;
        imagePath.set(newPath);
        isSaveButtonVisible.set(true);
    }

    public void updateProgress(final FileProgress percent) {
        if (percent != null) {
            progress.set(percent.getPercent());
        }
    }
}
