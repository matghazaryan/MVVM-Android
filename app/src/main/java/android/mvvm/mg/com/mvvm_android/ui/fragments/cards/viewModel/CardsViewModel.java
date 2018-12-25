package android.mvvm.mg.com.mvvm_android.ui.fragments.cards.viewModel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseViewModelItemClick;
import android.mvvm.mg.com.mvvm_android.core.models.empty.Empty;
import android.mvvm.mg.com.mvvm_android.core.models.error.RequestError;
import android.mvvm.mg.com.mvvm_android.core.models.room.card.Card;
import android.mvvm.mg.com.mvvm_android.core.repository.DataRepository;
import android.support.annotation.NonNull;
import com.dm.dmnetworking.DMNetworkLiveDataBag;

import java.util.List;

public class CardsViewModel extends DMBaseViewModelItemClick<Card, Empty> {

    public final ObservableField<List<Card>> cardList = new ObservableField<>();

    public CardsViewModel(final @NonNull Application application) {
        super(application);
    }

    public LiveData<List<Card>> dbCards() {
        return DataRepository.database().getCardList(getApplication().getApplicationContext());
    }

    public DMNetworkLiveDataBag<Card, RequestError> apiCards() {
        return DataRepository.api().getCardListFromNetwork(getApplication().getApplicationContext());
    }

    public void setRecycleViewData(final List<Card> cardList) {
        this.cardList.set(cardList);
    }

    public void insertAll(final List<Card> cardList) {
        if (cardList != null) {
            setEnableEmptyView(false);
            DataRepository.database().clearCardTable(getApplication().getApplicationContext(), ()
                    -> DataRepository.database().insertCardList(getApplication().getApplicationContext(), cardList, ids -> {
                setEnableEmptyView(true);
            }));
        }
    }

    @Override
    public void onItemClick(final Card card) {
        doAction(Action.SHOW_TOAST, card.getCardNumber());
    }

    @Override
    public Empty getEmptyObject() {
        return new Empty("Card baseEmpty");
    }
}
