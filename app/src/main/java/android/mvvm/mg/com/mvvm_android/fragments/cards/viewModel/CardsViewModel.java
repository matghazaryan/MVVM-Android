package android.mvvm.mg.com.mvvm_android.fragments.cards.viewModel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseViewModel;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.repository.DataRepository;
import android.mvvm.mg.com.mvvm_android.room.models.card.Card;
import android.support.annotation.NonNull;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;
import com.dm.dmnetworking.api_client.base.model.success.SuccessListT;

import java.util.List;

public class CardsViewModel extends BaseViewModel {

    public ObservableField<List<Card>> cardList = new ObservableField<>();
    public ObservableField<Boolean> isProgressDialogVisible = new ObservableField<>();

    public CardsViewModel(final @NonNull Application application) {
        super(application);
    }

    public LiveData<List<Card>> loadData() {
        return DataRepository.getInstance().dbGetCardList(getApplication().getApplicationContext());
    }

    public DMLiveDataBag<Card, RequestError> request() {
        isProgressDialogVisible.set(true);
        return DataRepository.getInstance().apiGetCardListFromNetwork(getApplication().getApplicationContext());
    }

    public void initRecycleViewData(final List<Card> cardList) {
        this.cardList.set(cardList);
    }

    public void insertAll(final SuccessListT<Card> cardSuccessListT) {
        isProgressDialogVisible.set(false);
        if (cardSuccessListT != null && cardSuccessListT.getList() != null) {
            DataRepository.getInstance().dbClearCardTable(getApplication().getApplicationContext(), ()
                    -> DataRepository.getInstance().dbInsertCardList(getApplication().getApplicationContext(), cardSuccessListT.getList(), ids -> {
            }));
        }
    }
}
