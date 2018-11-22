package android.mvvm.mg.com.mvvm_android.fragments.paymentHistory.view;

import android.arch.lifecycle.ViewModelProviders;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentPaymentHistoryBinding;
import android.mvvm.mg.com.mvvm_android.fragments.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.paymentHistory.viewModel.PaymentHistoryViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PaymentHistoryFragment extends BaseFragment {

    private FragmentPaymentHistoryBinding mBinding;
    private PaymentHistoryViewModel mViewModel;

    public PaymentHistoryFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentPaymentHistoryBinding.inflate(inflater, container, false);

        init();

        return mBinding.getRoot();
    }

    private void init() {
        setTitle(R.string.payment_history_title);

        mViewModel = ViewModelProviders.of(this).get(PaymentHistoryViewModel.class);
        mBinding.setViewModel(mViewModel);
    }

}
