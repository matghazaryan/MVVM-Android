package com.eraz.camera.camera.camera1;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.eraz.camera.camera.CameraActionListener;
import com.eraz.camera.camera.CameraPreview;
import com.eraz.camera.camera.CameraServiceInterface;
import com.eraz.camera.camera.ImageViewUtils;

import java.io.IOException;

/**
 * Created by Tahmazyan on 1/22/2018.
 */

public class CameraService implements CameraServiceInterface, Camera.PictureCallback {

    private final String LOG_TAG = "TAAAAAG";
    private final int CAPTURING = 10;
    private final int RECORDING = 20;


    private final int VIDEO_LENGTH = 30000;

    private AppCompatActivity activity;
    private Camera mCamera = null;
    private CameraPreview mCameraPreview;
    private MediaRecorder mediaRecorder;
    private String videoPath = null;
    private String imagePath = null;
    private int mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private boolean inPreview;
    private FrameLayout mCameraLayout;
    private int cameraMode;
    private boolean recording;
    private boolean isVideoTaken;
    private CameraActionListener actionListener;

    private boolean isFlashOn = false;

    public CameraService(AppCompatActivity activity, FrameLayout mCameraLayout) {
        this.activity = activity;
        this.mCameraLayout = mCameraLayout;
        cameraMode = CAPTURING;
    }

    @Override
    public void setActionListener(CameraActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public void start() {
        Log.d(LOG_TAG, "start()");

        initCamera();
    }

    @Override
    public void reOpenCamera() {
        if (mCamera != null) {
            Log.d(LOG_TAG, "reopenCamera");

            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        mCameraLayout.removeAllViews();
        initCamera();
    }

    @Override
    public void stop() {
        Log.d(LOG_TAG, "stop");
        releaseMediaRecorder();
        stopCamera();
    }

    @Override
    public void takePicture() {
        if (mCamera != null) {
            mCamera.takePicture(null, null, this);
        }
    }

    @Override
    public void takeVideo() {
        if (!recording) {
            if (!prepareMediaRecorder()) {
                activity.finish();
            }
            mediaRecorder.start();
            actionListener.onStartRecording();
            recording = true;
        } else {
            throw new RuntimeException("Camera in recording mode first stop last recorder");
        }
    }

    @Override
    public void stopVideoRecording() {

        if (recording) {
            Log.d(LOG_TAG, "stopVideoRecorder");

            try {
                if (mediaRecorder != null) {
                    mediaRecorder.stop();// stop the recording
                }
                isVideoTaken = true;
                actionListener.onVideoTaken(videoPath);
            } catch (Exception e) {
                e.printStackTrace();

            }
            // release the MediaRecorder object
            new Handler().postDelayed(this::releaseMediaRecorder, 100);
            recording = false;
        }
    }

    @Override
    public String getVideoFile() {
        return videoPath;
    }

    @Override
    public void erase() {
        Log.d(LOG_TAG, "erase");

        actionListener.onErase();
        recording = false;
    }

    private void initCamera() {
        Log.d(LOG_TAG, "initCamera");
        inPreview = true;
        try {
            mCamera = Camera.open(mCurrentCameraId);
            mCameraPreview = new CameraPreview(activity, mCamera, mCurrentCameraId);
            mCameraLayout.addView(mCameraPreview);
        } catch (Exception e) {
            e.printStackTrace();
            //// TODO: 9/25/16 add dialog error
        }
    }

    private void stopCamera() {
        if (mCamera != null) {
            if (inPreview) {
                mCamera.stopPreview();
            }
            inPreview = false;
            mCamera.release();
            mCamera = null;
        }
        if (mCameraPreview != null) {
            mCameraLayout.removeView(mCameraPreview);
            mCameraPreview = null;
        }
    }

    private void releaseMediaRecorder() {

        if (mediaRecorder != null) {

            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock();
        } else {

            Log.d(LOG_TAG, "media recorder is null");
        }
    }

    private boolean prepareMediaRecorder() {
        Log.d(LOG_TAG, "prepareMediaRecorder");

        mediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setMaxDuration(VIDEO_LENGTH);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setVideoEncodingBitRate(300000000);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setOrientationHint(mCameraPreview.getCameraOrientation());
        mediaRecorder.setVideoSize(mCameraPreview.getPreviewSize().width, mCameraPreview.getPreviewSize().height);

        videoPath = ImageViewUtils.getOutputVideoFile(activity);
        mediaRecorder.setOutputFile(videoPath);

        mediaRecorder.setPreviewDisplay(mCameraPreview.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(LOG_TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        actionListener.onPictureTaken(bytes, mCameraPreview.getCameraOrientation());
        mCamera.stopPreview();
    }

    public void turnOnFlash() {
        if (activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) && !isFrontCurrentCamera()) {
            if (mCamera != null) {
                final Camera.Parameters parameters = mCamera.getParameters();
                if (parameters != null) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(parameters);
                }
            }
            isFlashOn = true;
        }
    }

    public void turnOffFlash() {
        if (activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) && !isFrontCurrentCamera()) {
            if (mCamera != null) {
                final Camera.Parameters parameters = mCamera.getParameters();
                if (parameters != null) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(parameters);
                }
            }
            isFlashOn = false;
        }
    }

    public void switchCamera() {
        if (mCurrentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        reOpenCamera();
    }

    public boolean isFrontCurrentCamera() {
        return mCurrentCameraId != Camera.CameraInfo.CAMERA_FACING_BACK;
    }

    public void setCurrentCamera(final int camera) {
        mCurrentCameraId = camera;
    }

    public int getCurrentCamera() {
        return mCurrentCameraId;
    }

    public void showFrontCamera() {
        mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        reOpenCamera();
    }

    public void showBackCamera() {
        mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        reOpenCamera();
    }

    public boolean isFlashOn() {
        return isFlashOn;
    }
}
