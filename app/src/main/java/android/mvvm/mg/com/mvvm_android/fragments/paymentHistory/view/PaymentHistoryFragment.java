package android.mvvm.mg.com.mvvm_android.fragments.paymentHistory.view;

import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentPaymentHistoryBinding;
import android.mvvm.mg.com.mvvm_android.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.fragments.base.IBaseRequestListener;
import android.mvvm.mg.com.mvvm_android.fragments.paymentHistory.viewModel.PaymentHistoryViewModel;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.Transaction;
import android.mvvm.mg.com.mvvm_android.utils.MVVMEndlessRecyclerViewScrollListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

public class PaymentHistoryFragment extends BaseFragment<PaymentHistoryViewModel, FragmentPaymentHistoryBinding> {

    @Override
    protected int getLayout() {
        return R.layout.fragment_payment_history;
    }

    @Override
    protected Class<PaymentHistoryViewModel> getViewModelClass() {
        return PaymentHistoryViewModel.class;
    }

    @Override
    protected void initBinding(final FragmentPaymentHistoryBinding binding, final PaymentHistoryViewModel viewModel) {
        binding.setViewModel(viewModel);
    }

    @Override
    public int getTitleRes() {
        return R.string.payment_history_title;
    }

    @Override
    public void initialize() {
        initLoadMore();
    }

    @Override
    public void subscribes(final LifecycleOwner owner) {
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
