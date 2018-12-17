package com.eraz.camera.camera;

/**
 * Created by Tahmazyan on 1/23/2018.
 */

public interface CameraServiceInterface {

    void setActionListener(CameraActionListener actionListener);

    void start();

    void stop();

    void takePicture();

    void takeVideo();

    void stopVideoRecording();

    void erase();

    void reOpenCamera();

    String getVideoFile();
}
    

