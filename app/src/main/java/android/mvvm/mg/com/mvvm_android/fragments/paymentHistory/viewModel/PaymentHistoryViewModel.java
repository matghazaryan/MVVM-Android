package android.mvvm.mg.com.mvvm_android.fragments.paymentHistory.viewModel;

import android.app.Application;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseViewModel;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.Transaction;
import android.mvvm.mg.com.mvvm_android.repository.DataRepository;
import android.support.annotation.NonNull;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;
import com.dm.dmnetworking.api_client.base.model.success.SuccessT;

public class PaymentHistoryViewModel extends BaseViewModel {

    public ObservableField<Transaction> transaction = new ObservableField<>();
    public ObservableField<Boolean> isProgressDialogVisible = new ObservableField<>();

    public PaymentHistoryViewModel(final @NonNull Application application) {
        super(application);
    }

    public DMLiveDataBag<Transaction, RequestError> onLoadTransactions(final int page) {
        isProgressDialogVisible.set(true);
        return DataRepository.getInstance().apiGetTransactionList(getApplication().getApplicationContext(), page);
    }

    public void onLoad(final SuccessT<Transaction> transactionSuccessT) {
        isProgressDialogVisible.set(false);
        if (transactionSuccessT != null) {
            this.transaction.set(transactionSuccessT.getT());
        }
    }
}