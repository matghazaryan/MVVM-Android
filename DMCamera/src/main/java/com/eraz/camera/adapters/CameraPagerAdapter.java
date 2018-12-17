package com.eraz.camera.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaActionSound;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursion.freakycoder.mediapicker.galleries.Gallery;
import com.eraz.camera.R;
import com.eraz.camera.activities.ErazCameraActivity;
import com.eraz.camera.activities.PlayVideoActivity;
import com.eraz.camera.camera.CameraActionListener;
import com.eraz.camera.camera.ImageViewUtils;
import com.eraz.camera.camera.camera1.CameraService;
import com.eraz.camera.constants.IConstants;
import com.eraz.camera.viewPagers.CustomViewPager;

import java.util.concurrent.TimeUnit;

public class CameraPagerAdapter extends PagerAdapter {

    private ErazCameraActivity mActivity;

    private IConstants.Camera mLimitCameraType;

    private CustomViewPager mViewPager;

    private CameraService mPhotoCameraService;
    private CameraService mVideoCameraService;

    private View mPhotoInfoLayout;
    private View mVideoInfoLayout;

    private CameraActionListener mListener;

    private ImageButton mPhotoDoneButton;
    private ImageButton mPhotoReplayButton;
    private ImageButton mTakePhotoButton;
    private ImageView mPhotoImageView;
    private ImageView mPhotoFlashOnImageView;
    private ImageView mPhotoFlashOffImageView;
    private ImageButton mPhotoGalleryButton;
    private ImageButton mPhotoSwitchCameraButton;

    private ImageButton mRecordVideoButton;
    private ImageButton mRecordStopVideoButton;
    private ImageView mVideoFlashOnImageView;
    private ImageView mVideoFlashOffImageView;
    private TextView mVideoTimer;

    private byte[] mPhotoData;
    private int mOrientation;

    private CameraActionListener mLocalListener = new CameraActionListener() {
        @Override
        public void onPictureTaken(final byte[] data, final int orientation) {
            mPhotoImageView.setVisibility(View.VISIBLE);
            mPhotoImageView.setImageBitmap(ImageViewUtils.getPictureBitmap(data, orientation));

            mPhotoData = data;
            mOrientation = orientation;

            mTakePhotoButton.setVisibility(View.INVISIBLE);
            mPhotoDoneButton.setVisibility(View.VISIBLE);
            mPhotoReplayButton.setVisibility(View.VISIBLE);

            mPhotoSwitchCameraButton.setVisibility(View.GONE);
            mPhotoGalleryButton.setVisibility(View.GONE);
        }

        @Override
        public void onStartRecording() {
            mListener.onStartRecording();
            mRecordVideoButton.setVisibility(View.INVISIBLE);
            mRecordStopVideoButton.setVisibility(View.VISIBLE);
            initTimer();
        }

        @Override
        public void onVideoTaken(final String videoPath) {
            final Intent intent = new Intent(mActivity, PlayVideoActivity.class);
            intent.putExtra(IConstants.BundleKey.PATH, videoPath);
            mActivity.startActivityForResult(intent, IConstants.RequestCode.VIDEO_PLAY);

            mRecordStopVideoButton.setVisibility(View.GONE);
        }

        @Override
        public void onErase() {
            mListener.onErase();
        }
    };


    public CameraPagerAdapter(final ErazCameraActivity activity, final IConstants.Camera limitCameraType, final CustomViewPager viewPager) {
        mActivity = activity;
        mViewPager = viewPager;
        mLimitCameraType = limitCameraType;
    }

    @Override
    public int getCount() {
        switch (mLimitCameraType) {
            case PHOTO_ONLY:
            case VIDEO_ONLY:
                return 1;
            default:
                return 2;
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(final @NonNull ViewGroup collection, final int position) {
        final View view;

        switch (mLimitCameraType) {
            case PHOTO_ONLY:
                view = initPhotoView();
                break;
            case VIDEO_ONLY:
                view = initVideoView();
                break;
            default:
                if (position == 0) {
                    view = initPhotoView();
                } else {
                    view = initVideoView();
                }
        }

        collection.addView(view, 0);

        return view;
    }

    @Override
    public boolean isViewFromObject(final @NonNull View view, final @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(final @NonNull ViewGroup collection, final int position, final @NonNull Object view) {
        collection.removeView((View) view);
    }

    private View initPhotoView() {
        final LayoutInflater inflater = LayoutInflater.from(mActivity);
        @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.camera_photo_layout, null, false);
        final FrameLayout layout = view.findViewById(R.id.fl_photo);
        mTakePhotoButton = view.findViewById(R.id.btn_take_photo);
        mPhotoInfoLayout = view.findViewById(R.id.rl_info);
        mPhotoDoneButton = view.findViewById(R.id.btn_done);
        mPhotoReplayButton = view.findViewById(R.id.btn_replay);
        mPhotoImageView = view.findViewById(R.id.iv_photo);
        mPhotoFlashOnImageView = view.findViewById(R.id.iv_flash_on);
        mPhotoFlashOffImageView = view.findViewById(R.id.iv_flash_off);
        mPhotoGalleryButton = view.findViewById(R.id.btn_gallery);
        mPhotoSwitchCameraButton = view.findViewById(R.id.btn_switch_camera);

        mPhotoCameraService = new CameraService(mActivity, layout);

        if (mActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            mPhotoFlashOffImageView.setVisibility(View.VISIBLE);
        }

        mPhotoGalleryButton.setOnClickListener(v -> {
            switch (mActivity.getPickerType()){
                case DEFAULT:
                    final Intent intentDefault = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intentDefault.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, mActivity.isMultiplyPhoto());
                    mActivity.startActivityForResult(intentDefault, IConstants.RequestCode.PICK_PHOTO);
                    break;
                case CUSTOM:
                    final  Intent intentCustom = new Intent(mActivity, Gallery.class);
                    intentCustom.putExtra(IConstants.BundleKey.ACTIONBAR_TITLE, mActivity.getActionBarTitle());
                    intentCustom.putExtra(IConstants.BundleKey.MODE, IConstants.CustomGalleryMode.PHOTO);
                    intentCustom.putExtra(IConstants.BundleKey.MAX_SELECTION, mActivity.getMaxSelection());
                    mActivity.startActivityForResult(intentCustom, IConstants.RequestCode.OPEN_MEDIA_PICKER_FOR_PHOTO);
                    break;
            }
        });

        mPhotoSwitchCameraButton.setOnClickListener(v -> {
            mPhotoCameraService.switchCamera();
            if (mVideoCameraService != null) {
                mVideoCameraService.setCurrentCamera(mPhotoCameraService.getCurrentCamera());
            }
            if (mPhotoCameraService.isFrontCurrentCamera()) {
                mPhotoFlashOffImageView.setEnabled(false);
                mPhotoFlashOnImageView.setEnabled(false);
            } else {
                mPhotoFlashOffImageView.setEnabled(true);
                mPhotoFlashOnImageView.setEnabled(true);

                initFlash();
            }
        });

        mPhotoFlashOnImageView.setOnClickListener(v -> {
            mPhotoCameraService.turnOffFlash();
            mPhotoFlashOnImageView.setVisibility(View.GONE);
            mPhotoFlashOffImageView.setVisibility(View.VISIBLE);

            if (mVideoCameraService != null) {
                mVideoCameraService.turnOffFlash();
            }
        });

        mPhotoFlashOffImageView.setOnClickListener(v -> {
            mPhotoCameraService.turnOnFlash();
            mPhotoFlashOnImageView.setVisibility(View.VISIBLE);
            mPhotoFlashOffImageView.setVisibility(View.GONE);

            if (mVideoCameraService != null) {
                mVideoCameraService.turnOnFlash();
            }
        });

        mTakePhotoButton.setOnClickListener(v -> {
            if (mPhotoCameraService != null) {
                mPhotoCameraService.takePicture();
                final MediaActionSound sound = new MediaActionSound();
                sound.play(MediaActionSound.SHUTTER_CLICK);
            }
        });

        mPhotoDoneButton.setOnClickListener(v -> mListener.onPictureTaken(mPhotoData, mOrientation));

        mPhotoReplayButton.setOnClickListener(v -> {
            mTakePhotoButton.setVisibility(View.VISIBLE);
            mPhotoSwitchCameraButton.setVisibility(View.VISIBLE);
            mPhotoGalleryButton.setVisibility(View.VISIBLE);

            mPhotoDoneButton.setVisibility(View.GONE);
            mPhotoReplayButton.setVisibility(View.GONE);
            mPhotoImageView.setVisibility(View.GONE);

            mPhotoCameraService.reOpenCamera();

            initFlash();
        });


        mPhotoCameraService.setActionListener(mLocalListener);

        mPhotoCameraService.reOpenCamera();

        return view;
    }

    private View initVideoView() {
        final LayoutInflater inflater = LayoutInflater.from(mActivity);
        @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.camera_video_layout, null, false);

        final FrameLayout layout = view.findViewById(R.id.fl_video);

        mRecordVideoButton = view.findViewById(R.id.btn_record);
        mRecordStopVideoButton = view.findViewById(R.id.btn_record_stop);
        mVideoInfoLayout = view.findViewById(R.id.rl_info);
        mVideoFlashOnImageView = view.findViewById(R.id.iv_flash_on);
        mVideoFlashOffImageView = view.findViewById(R.id.iv_flash_off);
        mVideoTimer = view.findViewById(R.id.tv_timer);
        final ImageButton videoGalleryButton = view.findViewById(R.id.btn_gallery);
        final ImageButton videoSwitchCameraButton = view.findViewById(R.id.btn_switch_video);

        mVideoCameraService = new CameraService(mActivity, layout);

        if (mActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            mVideoFlashOffImageView.setVisibility(View.VISIBLE);
        }

        videoGalleryButton.setOnClickListener(v -> {
            switch (mActivity.getPickerType()){
                case DEFAULT:
                    final Intent intentDefault = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intentDefault.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, mActivity.isMultiplyVideo());
                    mActivity.startActivityForResult(intentDefault, IConstants.RequestCode.PICK_VIDEO);
                    break;
                case CUSTOM:
                    final  Intent intentCustom = new Intent(mActivity, Gallery.class);
                    intentCustom.putExtra(IConstants.BundleKey.ACTIONBAR_TITLE, mActivity.getActionBarTitle());
                    intentCustom.putExtra(IConstants.BundleKey.MODE, IConstants.CustomGalleryMode.VIDEO);
                    intentCustom.putExtra(IConstants.BundleKey.MAX_SELECTION, mActivity.getMaxSelection());
                    mActivity.startActivityForResult(intentCustom, IConstants.RequestCode.OPEN_MEDIA_PICKER_FOR_VIDEO);
                    break;
            }
        });

        videoSwitchCameraButton.setOnClickListener(v -> {
            mVideoCameraService.switchCamera();
            if (mPhotoCameraService != null) {
                mPhotoCameraService.setCurrentCamera(mVideoCameraService.getCurrentCamera());
            }
            if (mVideoCameraService.isFrontCurrentCamera()) {
                mVideoFlashOffImageView.setEnabled(false);
                mVideoFlashOnImageView.setEnabled(false);
            } else {
                mVideoFlashOffImageView.setEnabled(true);
                mVideoFlashOnImageView.setEnabled(true);

                initFlash();
            }
        });

        mVideoFlashOnImageView.setOnClickListener(v -> {
            mVideoCameraService.turnOffFlash();
            mVideoFlashOnImageView.setVisibility(View.GONE);
            mVideoFlashOffImageView.setVisibility(View.VISIBLE);

            if (mPhotoCameraService != null) {
                mPhotoCameraService.turnOffFlash();
            }
        });

        mVideoFlashOffImageView.setOnClickListener(v -> {
            mVideoCameraService.turnOnFlash();
            mVideoFlashOnImageView.setVisibility(View.VISIBLE);
            mVideoFlashOffImageView.setVisibility(View.GONE);

            if (mPhotoCameraService != null) {
                mPhotoCameraService.turnOnFlash();
            }
        });

        mRecordVideoButton.setOnClickListener(v -> {
            if (mVideoCameraService != null) {
                mVideoCameraService.takeVideo();
            }
        });

        mRecordStopVideoButton.setOnClickListener(v -> {
            if (mVideoCameraService != null) {
                mVideoCameraService.stopVideoRecording();
            }
        });

        mVideoCameraService.setActionListener(mLocalListener);

        return view;
    }

    private void animPhotoInfoLayout() {
        mTakePhotoButton.setVisibility(View.VISIBLE);

        showVideoInfoLayout();

        mViewPager.setPagingEnabled(false);
        mPhotoInfoLayout.setVisibility(View.VISIBLE);
        mPhotoInfoLayout.setAlpha(1);

        new Handler().postDelayed(() -> {
            if (mPhotoCameraService != null) {
                mPhotoCameraService.reOpenCamera();
            }

            if (mVideoCameraService != null) {
                mVideoCameraService.stop();
                mVideoCameraService.stopVideoRecording();
                mVideoCameraService.erase();
            }

            animateInfoView(mPhotoInfoLayout);


            if (mPhotoCameraService != null && mVideoCameraService != null) {
                if (mVideoCameraService.isFlashOn()) {
                    mPhotoCameraService.turnOnFlash();

                    mPhotoFlashOnImageView.setVisibility(View.VISIBLE);
                    mPhotoFlashOffImageView.setVisibility(View.GONE);
                } else {
                    mPhotoCameraService.turnOffFlash();

                    mPhotoFlashOnImageView.setVisibility(View.GONE);
                    mPhotoFlashOffImageView.setVisibility(View.VISIBLE);
                }
            }

            initFlash();

        }, IConstants.Delay.MILLISECONDS_600);
    }

    private void animVideoInfoLayout() {
        mRecordVideoButton.setVisibility(View.VISIBLE);


        showPhotoInfoLayout();

        mViewPager.setPagingEnabled(false);
        mVideoInfoLayout.setVisibility(View.VISIBLE);
        mVideoInfoLayout.setAlpha(1);

        new Handler().postDelayed(() -> {
            if (mVideoCameraService != null) {
                mVideoCameraService.reOpenCamera();
            }

            if (mPhotoCameraService != null) {
                mPhotoCameraService.stop();
            }

            animateInfoView(mVideoInfoLayout);


            if (mVideoCameraService != null && mPhotoCameraService != null) {
                if (mPhotoCameraService.isFlashOn()) {
                    mVideoCameraService.turnOnFlash();

                    mVideoFlashOffImageView.setVisibility(View.GONE);
                    mVideoFlashOnImageView.setVisibility(View.VISIBLE);
                } else {
                    mVideoCameraService.turnOffFlash();

                    mVideoFlashOnImageView.setVisibility(View.GONE);
                    mVideoFlashOffImageView.setVisibility(View.VISIBLE);
                }
            }

            initFlash();

        }, IConstants.Delay.MILLISECONDS_600);
    }

    private void initFlash() {
        switch (mActivity.getCameraType()) {
            case PHOTO:
                if (mPhotoCameraService.isFlashOn()) {
                    mPhotoCameraService.turnOnFlash();
                } else {
                    mPhotoCameraService.turnOffFlash();
                }
                break;
            case VIDEO:
                if (mVideoCameraService.isFlashOn()) {
                    mVideoCameraService.turnOnFlash();
                } else {
                    mVideoCameraService.turnOffFlash();
                }
                break;
        }

    }

    private void animateInfoView(final View view) {
        view.animate()
                .alpha(0.0f)
                .setDuration(IConstants.Delay.MILLISECONDS_600)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                        new Handler().postDelayed(() -> mViewPager.setPagingEnabled(true), IConstants.Delay.MILLISECONDS_300);
                    }
                });
    }

    private void showPhotoInfoLayout() {
        if (mPhotoInfoLayout != null) {
            mPhotoInfoLayout.setVisibility(View.VISIBLE);
            mPhotoInfoLayout.setAlpha(1);
        }
    }

    private void showVideoInfoLayout() {
        if (mVideoInfoLayout != null) {
            mVideoInfoLayout.setVisibility(View.VISIBLE);
            mVideoInfoLayout.setAlpha(1);
        }
    }

    public void onResume(final IConstants.Camera cameraType) {
        new Handler().postDelayed(() -> {
            switch (cameraType) {
                case PHOTO:
                    if (mPhotoImageView == null || mPhotoImageView.getVisibility() == View.GONE) {
                        initCamera(cameraType);
                    }
                    break;
                case VIDEO:
                    initCamera(cameraType);
                    break;
            }
        }, IConstants.Delay.MILLISECONDS_500);
    }

    public void onStop(final IConstants.Camera cameraType) {
        stopCamera(cameraType);
    }

    public void initCamera(final IConstants.Camera cameraType) {
        switch (cameraType) {
            case PHOTO:
                if (mPhotoCameraService != null) {
                    animPhotoInfoLayout();
                }
                break;
            case VIDEO:
                if (mVideoCameraService != null) {
                    animVideoInfoLayout();
                }
                break;
        }
    }

    private void stopCamera(final IConstants.Camera cameraType) {
        switch (cameraType) {
            case PHOTO:
                mPhotoCameraService.stop();
                showPhotoInfoLayout();
                break;
            case VIDEO:
                mVideoCameraService.stop();
                mVideoCameraService.erase();
                showVideoInfoLayout();
                break;
        }
    }

    public void setOnCameraActionListener(final CameraActionListener listener) {
        mListener = listener;
    }

    public CameraActionListener getListener() {
        return mListener;
    }

    private void initTimer() {
        int duration = mActivity.getVideoDurationSeconds() + 1;
        if (duration > 1) {
            mVideoTimer.setVisibility(View.VISIBLE);

            if (duration > 3600) {
                duration = 3600;
            }

            new CountDownTimer(duration * 1000, 1000) {

                @SuppressLint({"DefaultLocale", "SetTextI18n"})
                public void onTick(final long millisUntilFinished) {
                    StringBuilder stringBuilder = new StringBuilder();

                    if (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) > 0) {
                        stringBuilder.append(String.format("%d min",
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                        stringBuilder.append(", ");
                    }

                    stringBuilder.append(String.format("%d sec",
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                    mVideoTimer.setText(stringBuilder.toString());
                }

                public void onFinish() {
                    mVideoTimer.setVisibility(View.GONE);
                    mVideoCameraService.stopVideoRecording();
                }
            }.start();
        }
    }
}

