package android.mvvm.mg.com.mvvm_android.fragments.paymentHistory.view;

import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.fragments.BaseFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PaymentHistoryFragment extends BaseFragment {

    public PaymentHistoryFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        init();

        return inflater.inflate(R.layout.fragment_payment_history, container, false);
    }

    private void init() {
        setTitle(R.string.payment_history_title);
    }

}
