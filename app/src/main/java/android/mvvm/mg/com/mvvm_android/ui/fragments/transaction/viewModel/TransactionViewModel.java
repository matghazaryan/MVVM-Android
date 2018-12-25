package android.mvvm.mg.com.mvvm_android.ui.fragments.transaction.viewModel;

import android.app.Application;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseViewModelEmptyView;
import android.mvvm.mg.com.mvvm_android.core.models.empty.Empty;
import android.mvvm.mg.com.mvvm_android.core.models.error.RequestError;
import android.mvvm.mg.com.mvvm_android.core.models.transaction.TransactionData;
import android.mvvm.mg.com.mvvm_android.core.repository.DataRepository;
import android.support.annotation.NonNull;
import com.dm.dmnetworking.DMNetworkLiveDataBag;

public class TransactionViewModel extends DMBaseViewModelEmptyView<Empty> {

    public final ObservableField<TransactionData> transactionData = new ObservableField<>();


    public TransactionViewModel(final @NonNull Application application) {
        super(application);
    }

    public DMNetworkLiveDataBag<TransactionData, RequestError> apiTransactions(final int page) {
        return DataRepository.api().getTransactionList(getApplication().getApplicationContext(), page);
    }

    public void onLoad(final TransactionData transactionData) {
        if (transactionData != null) {
            this.transactionData.set(transactionData);
        }
    }

    @Override
    public Empty getEmptyObject() {
        return new Empty("Transaction baseEmpty view");
    }
}
