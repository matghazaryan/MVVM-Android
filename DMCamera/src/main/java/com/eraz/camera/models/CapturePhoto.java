package com.eraz.camera.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CapturePhoto implements Parcelable {

    private byte[] capturedPhoto;
    private int orientation;


    public CapturePhoto(final byte[] capturedPhoto, final int orientation) {
        this.capturedPhoto = capturedPhoto;
        this.orientation = orientation;
    }

    public byte[] getCapturedPhoto() {
        return capturedPhoto;
    }

    public int getOrientation() {
        return orientation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.capturedPhoto);
        dest.writeInt(this.orientation);
    }

    public CapturePhoto() {
    }

    protected CapturePhoto(Parcel in) {
        this.capturedPhoto = in.createByteArray();
        this.orientation = in.readInt();
    }

    public static final Parcelable.Creator<CapturePhoto> CREATOR = new Parcelable.Creator<CapturePhoto>() {
        @Override
        public CapturePhoto createFromParcel(Parcel source) {
            return new CapturePhoto(source);
        }

        @Override
        public CapturePhoto[] newArray(int size) {
            return new CapturePhoto[size];
        }
    };
}
