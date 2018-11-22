package com.eraz.camera.camera;

import java.util.List;

/**
 * Created by Tahmazyan on 1/22/2018.
 */

public interface CameraActionListener {

    void onPictureTaken(byte[] data, int orientation);

    void onStartRecording();

    void onVideoTaken(String videoPath);

    default void onGalleryPhotos(List<String> pathList) {

    }

    default void onGalleryVideos(List<String> pathList) {

    }

    void onErase();
}
