package android.mvvm.mg.com.mvvm_android.room.models.card;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;
import android.mvvm.mg.com.mvvm_android.room.main.MVVMGeneralDao;

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
