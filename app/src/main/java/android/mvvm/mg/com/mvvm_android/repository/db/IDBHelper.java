package android.mvvm.mg.com.mvvm_android.repository.db;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.helpers.IOnClearTableListener;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.helpers.IOnInsertAllListener;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.models.card.Card;

import java.util.List;

public interface IDBHelper {

    LiveData<List<Card>> dbGetCardList(final Context context);

    void dbInsertCardList(final Context context, final List<Card> cardList, final IOnInsertAllListener listener);

    void dbClearCardTable(final Context context, final IOnClearTableListener listener);
}
