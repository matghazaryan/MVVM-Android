package android.mvvm.mg.com.mvvm_android.repository.db;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.helpers.IOnClearTableListener;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.helpers.IOnInsertAllListener;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.helpers.card.CardHelper;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.models.card.Card;

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
        return CardHelper.getAllCard(context);
    }

    @Override
    public void insertCardList(final Context context, final List<Card> cardList, final IOnInsertAllListener listener) {
        CardHelper.insertCardList(context, cardList, listener);
    }

    @Override
    public void clearCardTable(final Context context, final IOnClearTableListener listener) {
        CardHelper.clearTable(context, listener);
    }
}
