package android.mvvm.mg.com.mvvm_android.room.models;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

public class MVVMRoomBase {

    @PrimaryKey(autoGenerate = true)
    public int autoId;

    public final int getAutoId() {
        return autoId;
    }

    public final void setAutoId(int autoId) {
        this.autoId = autoId;
    }

    @Ignore
    public MVVMRoomBase() {
    }
}
