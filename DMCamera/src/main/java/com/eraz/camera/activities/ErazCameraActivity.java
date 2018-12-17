package com.eraz.camera.activities;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.coursion.freakycoder.mediapicker.galleries.Gallery;
import com.eraz.camera.R;
import com.eraz.camera.adapters.CameraPagerAdapter;
import com.eraz.camera.camera.CameraActionListener;
import com.eraz.camera.constants.IConstants;
import com.eraz.camera.models.CapturePhoto;
import com.eraz.camera.models.MediaData;
import com.eraz.camera.permissions.AfterPermissionGranted;
import com.eraz.camera.permissions.EasyPermissions;
import com.eraz.camera.permissions.PermissionRequestCode;
import com.eraz.camera.utils.Utils;
import com.eraz.camera.viewPagers.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

public class ErazCameraActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private IConstants.Camera mCameraType;
    private IConstants.Camera mLimitCameraType;

    private int mVideoDurationSeconds;
    private int mPickImageCount;
    private int mPickVideoCount;

    private CameraPagerAdapter mCameraAdapter;
    private IConstants.Picker mPickerType;
    private int mMaxSelection = 1;
    private String mActionBarTitle;

    private boolean mIsRedirected = false;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IConstants.RequestCode.VIDEO_PLAY) {
                if (data != null) {
                    final Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        final String path = bundle.getString(IConstants.BundleKey.PATH);
                        if (mCameraAdapter != null) {
                            mCameraAdapter.getListener().onVideoTaken(path);
                        }
                    }
                }
            } else if (requestCode == IConstants.RequestCode.PICK_PHOTO || requestCode == IConstants.RequestCode.PICK_VIDEO) {
                if (data != null) {
                    final String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    final List<String> imagesEncodedList = new ArrayList<>();
                    String imageEncoded;
                    if (data.getClipData() != null) {
                        final ClipData mClipData = data.getClipData();
//                        final List<Uri> uriList = new ArrayList<>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            final ClipData.Item item = mClipData.getItemAt(i);
                            final Uri uri = item.getUri();
//                            uriList.add(uri);

                            // Get the cursor
                            final Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            if (cursor != null) {
                                cursor.moveToFirst();

                                final int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                imageEncoded = cursor.getString(columnIndex);
                                imagesEncodedList.add(imageEncoded);
                                cursor.close();
                            }
                        }

                        switch (requestCode) {
                            case IConstants.RequestCode.PICK_PHOTO:
                                mCameraAdapter.getListener().onGalleryPhotos(imagesEncodedList);
                                break;
                            case IConstants.RequestCode.PICK_VIDEO:
                                mCameraAdapter.getListener().onGalleryVideos(imagesEncodedList);
                                break;
                        }
                    }
                }
            } else if (requestCode == IConstants.RequestCode.OPEN_MEDIA_PICKER_FOR_PHOTO || requestCode == IConstants.RequestCode.OPEN_MEDIA_PICKER_FOR_VIDEO) {
                if (data != null) {
                    final List<String> selectionResult = data.getStringArrayListExtra("result");

                    switch (requestCode) {
                        case IConstants.RequestCode.OPEN_MEDIA_PICKER_FOR_PHOTO:
                            mCameraAdapter.getListener().onGalleryPhotos(selectionResult);
                            break;
                        case IConstants.RequestCode.OPEN_MEDIA_PICKER_FOR_VIDEO:
                            mCameraAdapter.getListener().onGalleryVideos(selectionResult);
                            break;
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        init();
    }

    private void init() {
        initIntentData();
        accessToCameraAndStorage();
        initViewPager();
    }

    private void initIntentData() {
        mCameraType = mLimitCameraType = IConstants.Camera.PHOTO;
        mPickImageCount = 1;
        mVideoDurationSeconds = 10;

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mLimitCameraType = (IConstants.Camera) bundle.getSerializable(IConstants.BundleKey.CAMERA_TYPE);
            mPickImageCount = bundle.getBoolean(IConstants.BundleKey.IS_MULTIPLY_GALLERY_IMAGE, false) ? 2 : 1;
            mPickVideoCount = bundle.getBoolean(IConstants.BundleKey.IS_MULTIPLY_GALLERY_VIDEO, false) ? 2 : 1;
            mVideoDurationSeconds = bundle.getInt(IConstants.BundleKey.VIDEO_DURATION_IN_SECONDS, 0);
            mPickerType = (IConstants.Picker) bundle.getSerializable(IConstants.BundleKey.PICKER_TYPE);
            mMaxSelection = bundle.getInt(IConstants.BundleKey.MAX_COUNT);
            mActionBarTitle = bundle.getString(IConstants.BundleKey.ACTIONBAR_TITLE);

            switch (mLimitCameraType) {
                case PHOTO:
                case PHOTO_ONLY:
                    mCameraType = IConstants.Camera.PHOTO;
                    findViewById(R.id.rl_content).setVisibility(View.VISIBLE);
                    break;
                case VIDEO:
                case VIDEO_ONLY:
                    mCameraType = IConstants.Camera.VIDEO;
                    findViewById(R.id.rl_content).setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void openGallery() {
        switch (mLimitCameraType) {
            case GALLERY_DEFAULT_PHOTO:
                final Intent intentDefaultPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intentDefaultPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiplyPhoto());
                startActivityForResult(intentDefaultPhoto, IConstants.RequestCode.PICK_PHOTO);
                break;
            case GALLERY_DEFAULT_VIDEO:
                final Intent intentDefaultVideo = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intentDefaultVideo.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiplyVideo());
                startActivityForResult(intentDefaultVideo, IConstants.RequestCode.PICK_VIDEO);
                break;
            case GALLERY_CUSTOM_PHOTO:
                final Intent intentCustomPhoto = new Intent(this, Gallery.class);
                intentCustomPhoto.putExtra(IConstants.BundleKey.ACTIONBAR_TITLE, getActionBarTitle());
                intentCustomPhoto.putExtra(IConstants.BundleKey.MODE, IConstants.CustomGalleryMode.PHOTO);
                intentCustomPhoto.putExtra(IConstants.BundleKey.MAX_SELECTION, getMaxSelection());
                startActivityForResult(intentCustomPhoto, IConstants.RequestCode.OPEN_MEDIA_PICKER_FOR_PHOTO);
                break;
            case GALLERY_CUSTOM_VIDEO:
                final Intent intentCustomVideo = new Intent(this, Gallery.class);
                intentCustomVideo.putExtra(IConstants.BundleKey.ACTIONBAR_TITLE, getActionBarTitle());
                intentCustomVideo.putExtra(IConstants.BundleKey.MODE, IConstants.CustomGalleryMode.VIDEO);
                intentCustomVideo.putExtra(IConstants.BundleKey.MAX_SELECTION, getMaxSelection());
                startActivityForResult(intentCustomVideo, IConstants.RequestCode.OPEN_MEDIA_PICKER_FOR_VIDEO);
                break;
        }
    }

    private void initViewPager() {
        final CustomViewPager viewPager = findViewById(R.id.viewpager);
        mCameraAdapter = new CameraPagerAdapter(this, mLimitCameraType, viewPager);

        viewPager.setAdapter(mCameraAdapter);
        viewPager.setPagingEnabled(false);

        switch (mLimitCameraType) {
            case PHOTO:
                viewPager.setCurrentItem(0);
                Utils.drawPaginationCircles(ErazCameraActivity.this, findViewById(R.id.ll_pagination_layout), IConstants.DefaultValue.CIRCLE_COUNT, 0);
                break;
            case VIDEO:
                viewPager.setCurrentItem(1);
                Utils.drawPaginationCircles(ErazCameraActivity.this, findViewById(R.id.ll_pagination_layout), IConstants.DefaultValue.CIRCLE_COUNT, 1);
                break;
            default:
                viewPager.setCurrentItem(0);
                Utils.drawPaginationCircles(ErazCameraActivity.this, findViewById(R.id.ll_pagination_layout), IConstants.DefaultValue.CIRCLE_COUNT, 0);
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                if (position == 0) {
                    mCameraType = IConstants.Camera.PHOTO;
                } else if (position == 1) {
                    mCameraType = IConstants.Camera.VIDEO;
                }

                if (mCameraAdapter != null) {
                    mCameraAdapter.initCamera(mCameraType);
                }

                Utils.drawPaginationCircles(ErazCameraActivity.this, findViewById(R.id.ll_pagination_layout), IConstants.DefaultValue.CIRCLE_COUNT, position);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        switch (mLimitCameraType) {
            case VIDEO_ONLY:
            case PHOTO_ONLY:
                findViewById(R.id.ll_pagination_layout).setVisibility(View.GONE);
                break;
        }


        mCameraAdapter.setOnCameraActionListener(new CameraActionListener() {
            @Override
            public void onPictureTaken(final byte[] data, final int orientation) {
                final Intent intent = new Intent();
                intent.putExtra(IConstants.BundleKey.MEDIA_DATA, new MediaData(new CapturePhoto(data, orientation)));
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onStartRecording() {

            }

            @Override
            public void onVideoTaken(final String videoPath) {
                final Intent intent = new Intent();
                intent.putExtra(IConstants.BundleKey.MEDIA_DATA, new MediaData(videoPath));
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onErase() {

            }

            @Override
            public void onGalleryPhotos(final List<String> pathList) {
                final Intent intent = new Intent();
                intent.putExtra(IConstants.BundleKey.MEDIA_DATA, new MediaData(pathList));
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onGalleryVideos(final List<String> pathList) {
                final Intent intent = new Intent();
                intent.putExtra(IConstants.BundleKey.MEDIA_DATA, new MediaData(pathList));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (mLimitCameraType) {
            case GALLERY_DEFAULT_PHOTO:
            case GALLERY_DEFAULT_VIDEO:
            case GALLERY_CUSTOM_PHOTO:
            case GALLERY_CUSTOM_VIDEO:
                if (mIsRedirected) {
                    finish();
                } else {
                    mIsRedirected = true;
                }
                break;
            default:
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
                    if (mCameraAdapter != null) {
                        mCameraAdapter.onResume(mCameraType);
                    }
                }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        switch (mLimitCameraType) {
            case GALLERY_DEFAULT_PHOTO:
            case GALLERY_DEFAULT_VIDEO:
            case GALLERY_CUSTOM_PHOTO:
            case GALLERY_CUSTOM_VIDEO:
                break;
            default:
                if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
                    if (mCameraAdapter != null) {
                        mCameraAdapter.onStop(mCameraType);
                    }
                }
        }
    }

    public IConstants.Camera getCameraType() {
        return mCameraType;
    }

    public int getVideoDurationSeconds() {
        return mVideoDurationSeconds;
    }

    public boolean isMultiplyPhoto() {
        return mPickImageCount > 1;
    }

    public boolean isMultiplyVideo() {
        return mPickVideoCount > 1;
    }

    public IConstants.Picker getPickerType() {
        return mPickerType;
    }

    public int getMaxSelection() {
        return mMaxSelection;
    }

    public String getActionBarTitle() {
        return mActionBarTitle;
    }

    @AfterPermissionGranted(PermissionRequestCode.CAMERA_AND_STORAGE)
    private void accessToCameraAndStorage() {
        switch (mLimitCameraType) {
            case GALLERY_DEFAULT_PHOTO:
            case GALLERY_DEFAULT_VIDEO:
            case GALLERY_CUSTOM_PHOTO:
            case GALLERY_CUSTOM_VIDEO:
                if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    EasyPermissions.requestPermissions(this, null,
                            PermissionRequestCode.CAMERA_AND_STORAGE, false, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    openGallery();
                }
                break;
            default:
                if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
                    EasyPermissions.requestPermissions(this, null,
                            PermissionRequestCode.CAMERA_AND_STORAGE, false, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions,
                                           final @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(final int requestCode, final List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(final int requestCode, final List<String> perms) {
        finish();
    }
}
