package android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.database.room.helpers.card;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.mvvm.mg.com.mvvm_android.core.models.room.card.Card;
import android.mvvm.mg.com.mvvm_android.core.models.room.card.CardDao;
import android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.database.room.helpers.*;
import android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.database.room.main.MVVMDatabase;

import java.util.List;
import java.util.concurrent.Executors;

public final class CardHelper {

    private static CardHelper instance;

    private final CardDao cardDao;

    private CardHelper(final Context context) {
        cardDao = MVVMDatabase.getInstance(context).getDB().cardDao();
    }

    public static CardHelper getInstance(final Context context) {
        if (instance == null) {
            synchronized (CardHelper.class) {
                if (instance == null) {
                    instance = new CardHelper(context);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Card>> getAllCard() {
        return cardDao.getAll();
    }

    public void insertCard(final Card card, final IOnInsertListener listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            final long id = cardDao.insert(card);
            listener.onInsert(id);
        });
    }

    public void insertCardList(final List<Card> cardList, final IOnInsertAllListener listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            final long[] ids = cardDao.insertAll(cardList);
            listener.onInsertAll(ids);
        });
    }

    public void update(final Card card, final IOnUpdateListener listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            cardDao.update(card);
            listener.onUpdate();
        });
    }

    public void delete(final Card card, final IOnDeleteListener listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            cardDao.delete(card);
            listener.onDelete();
        });
    }

    public void clearTable(final IOnClearTableListener listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            cardDao.cleanTable();
            listener.onClearTable();
        });
    }
}
