package android.mvvm.mg.com.mvvm_android.core.repository;

import android.mvvm.mg.com.mvvm_android.core.repository.api.Api;
import android.mvvm.mg.com.mvvm_android.core.repository.api.IAPIHelper;
import android.mvvm.mg.com.mvvm_android.core.repository.database.Database;
import android.mvvm.mg.com.mvvm_android.core.repository.database.IDBHelper;
import android.mvvm.mg.com.mvvm_android.core.repository.preference.IPreferenceHelper;
import android.mvvm.mg.com.mvvm_android.core.repository.preference.Preference;

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
