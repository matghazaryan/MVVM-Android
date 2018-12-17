package android.mvvm.mg.com.mvvm_android.core.models.room.card;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.database.room.main.MVVMGeneralDao;

import java.util.List;

@Dao
public interface CardDao extends MVVMGeneralDao<Card> {

    @Query("SELECT * FROM Card")
    LiveData<List<Card>> getAll();

    @Insert
    long[] insertAll(List<Card> list);

    @Insert
    long insert(Card card);

    @Update
    void update(Card card);

    @Delete
    void delete(Card card);

    @Query("DELETE FROM Card")
    void cleanTable();
}
