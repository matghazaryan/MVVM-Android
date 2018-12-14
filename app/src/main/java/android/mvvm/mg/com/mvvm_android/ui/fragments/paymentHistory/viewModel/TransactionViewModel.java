package android.mvvm.mg.com.mvvm_android.ui.fragments.paymentHistory.viewModel;

import android.app.Application;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.core.models.RequestError;
import android.mvvm.mg.com.mvvm_android.core.models.TransactionData;
import android.mvvm.mg.com.mvvm_android.core.repository.DataRepository;
import android.mvvm.mg.com.mvvm_android.ui.fragments.base.BaseViewModel;
import android.support.annotation.NonNull;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

public class TransactionViewModel extends BaseViewModel {

    public final ObservableField<TransactionData> transactionData = new ObservableField<>();

    public TransactionViewModel(final @NonNull Application application) {
        super(application);
    }

    public DMLiveDataBag<TransactionData, RequestError> onLoadTransactions(final int page) {
        return DataRepository.api().getTransactionList(getApplication().getApplicationContext(), page);
    }

    public void onLoad(final TransactionData transactionData) {
        if (transactionData != null) {
            this.transactionData.set(transactionData);
        }
    }
}
