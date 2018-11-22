package com.eraz.camera.constants;

public interface IConstants {

    enum Camera {
        PHOTO,
        PHOTO_ONLY,
        VIDEO,
        VIDEO_ONLY,
        GALLERY_DEFAULT_PHOTO,
        GALLERY_DEFAULT_VIDEO,
        GALLERY_CUSTOM_PHOTO,
        GALLERY_CUSTOM_VIDEO,
    }

    enum Picker {
        DEFAULT,
        CUSTOM
    }

    interface CustomGalleryMode {
        int BOTH = 1;
        int PHOTO = 2;
        int VIDEO = 3;
    }


    interface Delay {
        int MILLISECONDS_600 = 600;
        int MILLISECONDS_500 = 500;
        int MILLISECONDS_300 = 300;
    }

    interface BundleKey {
        String PATH = "path";
        String POSITION = "position";
        String CAMERA_TYPE = "camera_type";
        String VIDEO_DURATION_IN_SECONDS = "video_duration_in_seconds";
        String IS_MULTIPLY_GALLERY_IMAGE = "is_multiply_gallery_image";
        String IS_MULTIPLY_GALLERY_VIDEO = "is_multiply_gallery_video";
        String PICKER_TYPE = "picker_type";
        String MAX_COUNT = "max_count";
        String MODE = "mode";
        String MAX_SELECTION = "maxSelection";
        String ACTIONBAR_TITLE = "title";
        String MEDIA_DATA = "media_data";
    }

    interface RequestCode {
        int VIDEO_PLAY = 1000;
        int PICK_PHOTO = 1001;
        int PICK_VIDEO = 1002;
        int OPEN_MEDIA_PICKER_FOR_VIDEO = 1003;
        int OPEN_MEDIA_PICKER_FOR_PHOTO = 1004;

    }

    interface DefaultValue {
        int VIDEO_MAX_DURATION_IN_SECONDS = 60 * 60;
        int CIRCLE_COUNT = 2;
    }
}
