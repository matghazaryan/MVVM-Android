package android.mvvm.mg.com.mvvm_android.ui.fragments.transaction.view;

import android.databinding.DataBindingUtil;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.core.base.BaseAdapter;
import android.mvvm.mg.com.mvvm_android.core.models.transaction.Transaction;
import android.mvvm.mg.com.mvvm_android.core.models.transaction.TransactionGroup;
import android.mvvm.mg.com.mvvm_android.databinding.TransactionInnerItemBinding;
import android.mvvm.mg.com.mvvm_android.databinding.TransactionItemBinding;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

public class TransactionAdapter extends BaseAdapter<TransactionGroup, TransactionItemBinding> {

    public TransactionAdapter(final List<TransactionGroup> transactionGroups) {
        super(transactionGroups);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.transaction_item;
    }

    @Override
    protected View onBaseBindViewHolder(final @NonNull BaseViewHolder<TransactionItemBinding> holder, final int position, final TransactionGroup transactionGroup) {
        final List<Transaction> transactionList = transactionGroup.getTransactionList();
        holder.binding.llContainer.removeAllViews();
        if (transactionList != null) {
            for (final Transaction t : transactionList) {
                final TransactionInnerItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.transaction_inner_item, null, false);
                binding.setTransaction(t);
                holder.binding.llContainer.addView(binding.getRoot());
            }
        }

        return null;
    }
}
