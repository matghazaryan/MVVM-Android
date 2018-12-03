package android.mvvm.mg.com.mvvm_android.fragments.paymentHistory.view;

import android.arch.lifecycle.ViewModelProviders;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentPaymentHistoryBinding;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.base.IBaseRequestListener;
import android.mvvm.mg.com.mvvm_android.fragments.paymentHistory.viewModel.PaymentHistoryViewModel;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.Transaction;
import android.mvvm.mg.com.mvvm_android.utils.MVVMEndlessRecyclerViewScrollListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

public class PaymentHistoryFragment extends BaseFragment<PaymentHistoryViewModel> {

    private FragmentPaymentHistoryBinding mBinding;

    public PaymentHistoryFragment() {
    }

    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        mBinding = FragmentPaymentHistoryBinding.inflate(inflater, container, false);

        init();

        return mBinding.getRoot();
    }

    private void init() {
        setTitle(R.string.payment_history_title);

        mViewModel = ViewModelProviders.of(this).get(PaymentHistoryViewModel.class);
        mBinding.setViewModel(mViewModel);

        subscribes();

        initLoadMore();
    }

    private void subscribes() {
        subscribeTransactionLoad(mViewModel.onLoadTransactions(0));
    }

    private void initLoadMore() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mBinding.rvTransaction.setLayoutManager(linearLayoutManager);
        mBinding.rvTransaction.addOnScrollListener(new MVVMEndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(final int page, final int totalItemsCount, final RecyclerView view) {
                subscribeTransactionLoad(mViewModel.onLoadTransactions(page));
            }
        });
    }

    private void subscribeTransactionLoad(final DMLiveDataBag<Transaction, RequestError> liveDataBag) {
        handleRequest(liveDataBag, new IBaseRequestListener<Transaction>() {
            @Override
            public void onSuccess(final Transaction transaction) {
                mViewModel.onLoad(transaction);
            }
        });
    }
}
