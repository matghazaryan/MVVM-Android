package android.mvvm.mg.com.mvvm_android.core.models;

import android.os.Parcel;
import android.os.Parcelable;

public final class User implements Parcelable {

    private String email;
    private String password;
    private String token;
    private boolean twoStepEnabled;

    public User() {
    }

    public User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public boolean isTwoStepEnabled() {
        return twoStepEnabled;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.token);
        dest.writeByte(this.twoStepEnabled ? (byte) 1 : (byte) 0);
    }

    protected User(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
        this.token = in.readString();
        this.twoStepEnabled = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
