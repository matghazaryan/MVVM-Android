package android.mvvm.mg.com.mvvm_android.fragments.fileUpload.view;

import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.fragments.BaseFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FileUploadFragment extends BaseFragment {

    public FileUploadFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        init();

        return inflater.inflate(R.layout.fragment_file_upload, container, false);
    }

    private void init() {
        setTitle(R.string.file_upload_title);
    }
}
