package android.mvvm.mg.com.mvvm_android.core.repository.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.mvvm.mg.com.mvvm_android.core.models.room.card.Card;
import android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.database.room.helpers.IOnClearTableListener;
import android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.database.room.helpers.IOnInsertAllListener;
import android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.database.room.helpers.card.CardHelper;

import java.util.List;

public final class Database implements IDBHelper {

    private static Database instance;

    public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    @Override
    public LiveData<List<Card>> getCardList(final Context context) {
        return CardHelper.getInstance(context).getAllCard();
    }

    @Override
    public void insertCardList(final Context context, final List<Card> cardList, final IOnInsertAllListener listener) {
        CardHelper.getInstance(context).insertCardList(cardList, listener);
    }

    @Override
    public void clearCardTable(final Context context, final IOnClearTableListener listener) {
        CardHelper.getInstance(context).clearTable(listener);
    }
}
