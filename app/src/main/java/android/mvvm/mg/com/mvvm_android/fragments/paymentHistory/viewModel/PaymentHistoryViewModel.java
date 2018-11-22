package android.mvvm.mg.com.mvvm_android.fragments.paymentHistory.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.mvvm.mg.com.mvvm_android.models.Transaction;
import android.support.annotation.NonNull;

import java.util.List;

public class PaymentHistoryViewModel extends AndroidViewModel {

    public ObservableField<List<Transaction>> transactionList = new ObservableField<>();

    public PaymentHistoryViewModel(final @NonNull Application application) {
        super(application);
    }
}
