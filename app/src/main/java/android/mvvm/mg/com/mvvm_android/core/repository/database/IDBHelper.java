package android.mvvm.mg.com.mvvm_android.core.repository.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.mvvm.mg.com.mvvm_android.core.models.room.card.Card;
import android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.database.room.helpers.IOnClearTableListener;
import android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.database.room.helpers.IOnInsertAllListener;

import java.util.List;

public interface IDBHelper {

    LiveData<List<Card>> getCardList(final Context context);

    void insertCardList(final Context context, final List<Card> cardList, final IOnInsertAllListener listener);

    void clearCardTable(final Context context, final IOnClearTableListener listener);
}
