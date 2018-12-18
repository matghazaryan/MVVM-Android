package android.mvvm.mg.com.mvvm_android.ui.fragments.paymentHistory.view;

import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.listeners.IEmptyViewHandler;
import android.mvvm.mg.com.mvvm_android.core.models.RequestError;
import android.mvvm.mg.com.mvvm_android.core.models.TransactionData;
import android.mvvm.mg.com.mvvm_android.core.utils.MVVMEndlessRecyclerViewScrollListener;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentTransactionsBinding;
import android.mvvm.mg.com.mvvm_android.ui.fragments.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.ui.fragments.base.IBaseRequestListener;
import android.mvvm.mg.com.mvvm_android.ui.fragments.paymentHistory.viewModel.TransactionViewModel;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

public class TransactionsFragment extends BaseFragment<TransactionViewModel, FragmentTransactionsBinding> implements IEmptyViewHandler {

    @Override
    protected int getLayout() {
        return R.layout.fragment_transactions;
    }

    @Override
    protected Class<TransactionViewModel> getViewModelClass() {
        return TransactionViewModel.class;
    }

    @Override
    protected void initBinding(final FragmentTransactionsBinding binding, final TransactionViewModel viewModel) {
        binding.setViewModel(viewModel);
        binding.setHandler(this);
    }

    @Override
    public int getTitleRes() {
        return R.string.transactions_title;
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

    private void subscribeTransactionLoad(final DMLiveDataBag<TransactionData, RequestError> liveDataBag) {
        makeRequest(liveDataBag, new IBaseRequestListener<TransactionData>() {
            @Override
            public void onSuccess(final TransactionData transactionData) {
                mViewModel.onLoad(transactionData);
            }
        });
    }

    @Override
    public void onClick(final View view) {
        Toast.makeText(getContext(), "Transaction Empty view click", Toast.LENGTH_SHORT).show();
    }
}
