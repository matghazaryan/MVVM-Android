package com.eraz.camera.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by matevosghazaryan on 7/14/14.
 */
@SuppressWarnings("deprecation")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraPreview";

    private Context mContext;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private List<Camera.Size> mSupportedPreviewSizes;
    private Camera.Size mPreviewSize;
    private int mCurrentCameraId;
    private int cameraOrientation;

    public CameraPreview(Context context) {
        super(context);
        mContext = context;
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public CameraPreview(Context context, Camera camera, int currentCameraId) {
        super(context);
        mContext = context;
        mCamera = camera;
        mCurrentCameraId = currentCameraId;

        // supported preview sizes
        mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        for (Camera.Size str : mSupportedPreviewSizes)
            Log.e(TAG, str.width + "/" + str.height);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // empty. surfaceChanged will take care of stuff
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.e(TAG, "surfaceChanged => w=" + w + ", h=" + h);
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or reformatting changes here
        // start preview with new settings
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
            Collections.sort(sizes, new CustomComparator());

            Camera.Size pictureSize = getPictureSize(sizes);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);

            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setJpegQuality(70);
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(getCameraDisplayOrientation(mCurrentCameraId));
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    private Camera.Size getPictureSize(List<Camera.Size> imageSizes) {
        double ratio = (double) mPreviewSize.height / mPreviewSize.width;
        List<Camera.Size> fitSizes = new ArrayList<>();
        for(Camera.Size size : imageSizes) {
            double picRatio = (double) size.height / size.width;
            if(Math.abs(picRatio - ratio) < 0.1) {
                fitSizes.add(size);
            }
        }
        if(fitSizes.isEmpty()) {
            return imageSizes.get(0);
        }
        return imageSizes.get(fitSizes.size() / 2);
    }

    public int getCameraDisplayOrientation(int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = ((Activity) mContext).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            cameraOrientation = (info.orientation + degrees) % 360;
            cameraOrientation = (360 - cameraOrientation) % 360; // compensate the mirror
        } else { // back-facing
            cameraOrientation = (info.orientation - degrees + 360) % 360;
        }
        Log.d("vazgenchik", "rotation camera: " + cameraOrientation);
        return cameraOrientation;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }

        float ratio;
        if (mPreviewSize.height >= mPreviewSize.width)
            ratio = (float) mPreviewSize.height / (float) mPreviewSize.width;
        else
            ratio = (float) mPreviewSize.width / (float) mPreviewSize.height;

        // One of these methods should be used, second method squishes preview slightly
        setMeasuredDimension(width, (int) (width * ratio));
//        setMeasuredDimension((int) (width * ratio), height);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;

        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.height / size.width;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;

            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - h);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - h);
                }
            }
        }

        return optimalSize;
    }

    public class CustomComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size o1, Camera.Size o2) {
            final int a = o1.width;
            final int b = o2.width;
            return a < b ? 1 : (a == b ? 0 : -1);
        }
    }

    public int getCameraOrientation() {
        return cameraOrientation;
    }

    public Camera.Size getPreviewSize() {
        return mPreviewSize;
    }
}