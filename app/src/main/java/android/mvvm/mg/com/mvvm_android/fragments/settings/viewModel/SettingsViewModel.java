package android.mvvm.mg.com.mvvm_android.fragments.settings.viewModel;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.constants.IConstants;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.repository.DataRepository;
import android.mvvm.mg.com.mvvm_android.utils.MVVMFileUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;
import com.dm.dmnetworking.api_client.base.model.progress.FileProgress;
import com.eraz.camera.models.CapturePhoto;
import com.eraz.camera.models.MediaData;

public class SettingsViewModel extends AndroidViewModel {

    private MutableLiveData<String> newImagePath = new MutableLiveData<>();
    private MutableLiveData<String> newImageNotFound = new MutableLiveData<>();

    public ObservableField<String> imagePath = new ObservableField<>();
    public ObservableField<Boolean> isProgressDialogVisible = new ObservableField<>();
    public ObservableField<Integer> progress = new ObservableField<>();

    public SettingsViewModel(final @NonNull Application application) {
        super(application);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = null;
            if (data != null) {
                bundle = data.getExtras();
            }

            switch (requestCode) {
                case IConstants.RequestCode.CAMERA:
                    setImage(bundle);
                    break;
            }
        }
    }

    public void setImage(final Bundle bundle) {
        if (bundle != null) {
            final MediaData mediaData = bundle.getParcelable(com.eraz.camera.constants.IConstants.BundleKey.MEDIA_DATA);
            if (mediaData != null) {
                String path = null;
                final CapturePhoto capturePhoto = mediaData.getCapturePhoto();
                if (capturePhoto != null) {
                    path = MVVMFileUtils.onSavePicture(capturePhoto.getCapturedPhoto());
                } else if (mediaData.getSelectedImagesOrVideosPathList() != null && mediaData.getSelectedImagesOrVideosPathList().size() == 1) {
                    path = mediaData.getSelectedImagesOrVideosPathList().get(0);
                }

                if (path != null) {
                    newImagePath.setValue(path);
                } else {
                    newImageNotFound.setValue(null);
                }
            }
        }
    }

    public DMLiveDataBag<String, RequestError> sendImage(final String path) {
        isProgressDialogVisible.set(true);
        return DataRepository.getInstance().sendImage(getApplication().getApplicationContext(), path);
    }

    public MutableLiveData<String> getNewImagePath() {
        return newImagePath;
    }

    public void updateImagePath(final String path) {
        isProgressDialogVisible.set(false);
        DataRepository.getInstance().saveProfilePhoto(path);
        imagePath.set(path);
    }

    public void updateProgress(final FileProgress percent) {
        if (percent != null) {
            progress.set(percent.getPercent());
        }
    }

    public void showProfilePhoto() {
        final String path = DataRepository.getInstance().getProfilePhoto();
        imagePath.set(path);
    }

    public MutableLiveData<String> getNewImageNotFound() {
        return newImageNotFound;
    }
}
