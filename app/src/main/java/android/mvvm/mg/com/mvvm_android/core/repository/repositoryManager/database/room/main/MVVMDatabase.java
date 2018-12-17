package android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.database.room.main;

import android.arch.persistence.room.Room;
import android.content.Context;

public final class MVVMDatabase {

    private static MVVMDatabase instance;

    private final MVVMRoomDatabase db;

    private MVVMDatabase(final Context context) {
        db = Room.databaseBuilder(context,
                MVVMRoomDatabase.class, context.getPackageName())
                .build();
    }

    public static MVVMDatabase getInstance(final Context context) {

        if (instance == null) {
            instance = new MVVMDatabase(context);
        }

        return instance;
    }

    public MVVMRoomDatabase getDB() {
        return db;
    }
}
