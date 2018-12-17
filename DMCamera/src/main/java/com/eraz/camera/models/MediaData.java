package com.eraz.camera.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MediaData implements Parcelable {

    private CapturePhoto capturePhoto;
    private String recordedVideoPath;
    private List<String> selectedImagesOrVideosPathList;


    public MediaData(final CapturePhoto capturePhoto) {
        this.capturePhoto = capturePhoto;
    }

    public MediaData(final String recordedVideoPath) {
        this.recordedVideoPath = recordedVideoPath;
    }

    public MediaData(List<String> selectedImagesOrVideosPathList) {
        this.selectedImagesOrVideosPathList = selectedImagesOrVideosPathList;
    }

    public CapturePhoto getCapturePhoto() {
        return capturePhoto;
    }

    public String getRecordedVideoPath() {
        return recordedVideoPath;
    }

    public List<String> getSelectedImagesOrVideosPathList() {
        return selectedImagesOrVideosPathList;
    }

    private void setSelectedImagesOrVideosPathList(final List<String> selectedImagesOrVideosPathList) {
        this.selectedImagesOrVideosPathList = selectedImagesOrVideosPathList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.capturePhoto, flags);
        dest.writeString(this.recordedVideoPath);
        dest.writeStringList(this.selectedImagesOrVideosPathList);
    }

    protected MediaData(Parcel in) {
        this.capturePhoto = in.readParcelable(CapturePhoto.class.getClassLoader());
        this.recordedVideoPath = in.readString();
        this.selectedImagesOrVideosPathList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<MediaData> CREATOR = new Parcelable.Creator<MediaData>() {
        @Override
        public MediaData createFromParcel(Parcel source) {
            return new MediaData(source);
        }

        @Override
        public MediaData[] newArray(int size) {
            return new MediaData[size];
        }
    };
}
