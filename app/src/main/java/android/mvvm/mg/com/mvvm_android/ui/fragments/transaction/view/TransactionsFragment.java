package android.mvvm.mg.com.mvvm_android.ui.fragments.transaction.view;

import android.arch.lifecycle.LifecycleOwner;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseFragment;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseIOnSharedDataListener;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseIRequestListener;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.core.listeners.IEmptyViewHandler;
import android.mvvm.mg.com.mvvm_android.core.models.error.RequestError;
import android.mvvm.mg.com.mvvm_android.core.models.transaction.TransactionData;
import android.mvvm.mg.com.mvvm_android.core.utils.MVVMEndlessRecyclerViewScrollListener;
import android.mvvm.mg.com.mvvm_android.databinding.FragmentTransactionsBinding;
import android.mvvm.mg.com.mvvm_android.ui.fragments.transaction.viewModel.TransactionViewModel;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.dm.dmnetworking.DMNetworkLiveDataBag;

public class TransactionsFragment extends DMBaseFragment<TransactionViewModel, FragmentTransactionsBinding> implements IEmptyViewHandler {

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_transactions;
    }

    @Override
    protected Class<TransactionViewModel> getViewModelClass() {
        return TransactionViewModel.class;
    }

    @Override
    protected void setBinding(final FragmentTransactionsBinding binding, final TransactionViewModel viewModel) {
        binding.setViewModel(viewModel);
        binding.setHandler(this);
    }

    @Override
    public int getTitleRes() {
        return R.string.transactions_title;
    }

    @Override
    public void initialize() {
        setupLoadMore();
    }

    @Override
    public void subscribers(final LifecycleOwner owner) {
        subscribeToTransactionLoad(mViewModel.apiTransactions(IMVVMConstants.DefaultValue.FIRST_PAGE));

        //Receive data from AccountViewModel
        getSharedData(IMVVMConstants.SendCode.ACCOUNT_TO_TRANSACTION, (DMBaseIOnSharedDataListener<String>) s -> Log.d("myLogs", s));
    }

    private void setupLoadMore() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mBinding.rvTransaction.setLayoutManager(linearLayoutManager);
        mBinding.rvTransaction.addOnScrollListener(new MVVMEndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(final int page, final int totalItemsCount, final RecyclerView view) {
                subscribeToTransactionLoad(mViewModel.apiTransactions(page));
            }
        });
    }

    private void subscribeToTransactionLoad(final DMNetworkLiveDataBag<TransactionData, RequestError> apiTransactionLiveDataBag) {
        handleRequestFor(apiTransactionLiveDataBag, new DMBaseIRequestListener<TransactionData>() {
            @Override
            public void onSuccess(final TransactionData transactionData) {
                mViewModel.onLoad(transactionData);
            }
        });
    }

    @Override
    public void onEmptyViewClick(final View view) {
        Toast.makeText(getContext(), getString(R.string.transactions_on_empty_click), Toast.LENGTH_SHORT).show();
    }
}
