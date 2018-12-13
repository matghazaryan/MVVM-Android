package android.mvvm.mg.com.mvvm_android.repository;

import android.mvvm.mg.com.mvvm_android.repository.api.Api;
import android.mvvm.mg.com.mvvm_android.repository.api.IAPIHelper;
import android.mvvm.mg.com.mvvm_android.repository.db.Database;
import android.mvvm.mg.com.mvvm_android.repository.db.IDBHelper;
import android.mvvm.mg.com.mvvm_android.repository.preference.IPreferenceHelper;
import android.mvvm.mg.com.mvvm_android.repository.preference.Preference;

public final class DataRepository {

    public static IAPIHelper api() {
        return Api.getInstance();
    }

    public static IPreferenceHelper preference() {
        return Preference.getInstance();
    }

    public static IDBHelper database() {
        return Database.getInstance();
    }
}
