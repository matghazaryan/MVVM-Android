package android.mvvm.mg.com.mvvm_android.fragments.cards.viewModel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseViewModelItemClick;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.repository.DataRepository;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.models.card.Card;
import android.support.annotation.NonNull;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

import java.util.List;

public class CardsViewModel extends BaseViewModelItemClick<Card> {

    public final ObservableField<List<Card>> cardList = new ObservableField<>();

    public CardsViewModel(final @NonNull Application application) {
        super(application);
    }

    public LiveData<List<Card>> loadData() {
        return DataRepository.database().getCardList(getApplication().getApplicationContext());
    }

    public DMLiveDataBag<Card, RequestError> getCards() {
        return DataRepository.api().getCardListFromNetwork(getApplication().getApplicationContext());
    }

    public void initRecycleViewData(final List<Card> cardList) {
        this.cardList.set(cardList);
    }

    public void insertAll(final List<Card> cardList) {
        if (cardList != null) {
            DataRepository.database().clearCardTable(getApplication().getApplicationContext(), ()
                    -> DataRepository.database().insertCardList(getApplication().getApplicationContext(), cardList, ids -> {
            }));
        }
    }

    @Override
    public void onItemClick(final Card card) {
        doAction(Action.SHOW_TOAST, card.getCardNumber());
    }
}
