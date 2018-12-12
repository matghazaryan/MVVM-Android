package android.mvvm.mg.com.mvvm_android.adapters;

import android.databinding.DataBindingUtil;
import android.mvvm.mg.com.mvvm_android.R;
import android.mvvm.mg.com.mvvm_android.databinding.TransactionInnerItemBinding;
import android.mvvm.mg.com.mvvm_android.databinding.TransactionItemBinding;
import android.mvvm.mg.com.mvvm_android.models.Transaction;
import android.mvvm.mg.com.mvvm_android.models.TransactionGroup;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<TransactionGroup> transactionGroupList;
    private LayoutInflater inflater;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TransactionItemBinding binding;

        MyViewHolder(final TransactionItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    public TransactionAdapter(final List<TransactionGroup> transactionGroupList) {
        this.transactionGroupList = transactionGroupList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        final TransactionItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.transaction_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, final int position) {
        final TransactionGroup transactionGroup = transactionGroupList.get(position);
        final List<Transaction> transactionList = transactionGroup.getTransactionList();
        holder.binding.llContainer.removeAllViews();
        if (transactionList != null) {
            for (final Transaction t : transactionList) {
                final TransactionInnerItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.transaction_inner_item, null, false);
                binding.setTransaction(t);
                holder.binding.llContainer.addView(binding.getRoot());
            }
        }
    }

    @Override
    public int getItemCount() {
        return transactionGroupList.size();
    }

    public void setTransactionGroupList(final List<TransactionGroup> transactionGroupList) {
        if (this.transactionGroupList == null) {
            this.transactionGroupList = transactionGroupList;
        } else {
            this.transactionGroupList.addAll(transactionGroupList);
        }
        notifyDataSetChanged();
    }
}
