package android.mvvm.mg.com.mvvm_android.room.main;

import android.arch.lifecycle.LiveData;

import java.util.List;

public interface MVVMGeneralDao<T> {

    LiveData<List<T>> getAll();

    long[] insertAll(List<T> list);

    long insert(T t);

    void update(T t);

    void delete(T t);

    void cleanTable();
}
