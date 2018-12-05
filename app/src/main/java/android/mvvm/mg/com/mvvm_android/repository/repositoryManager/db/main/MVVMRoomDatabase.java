package android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.main;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.models.card.Card;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.models.card.CardDao;

@Database(entities = {Card.class}, version = 1, exportSchema = false)
public abstract class MVVMRoomDatabase extends RoomDatabase {

    public abstract CardDao cardDao();
}
