package android.mvvm.mg.com.mvvm_android.core.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseAdapter<T, Binding extends ViewDataBinding> extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder<Binding>> {

    protected List<T> tList;
    protected LayoutInflater inflater;
    protected IBaseOnItemClickListener<T> listener;

    protected abstract int getItemLayout();

    protected abstract View onBaseBindViewHolder(final @NonNull BaseViewHolder<Binding> holder, final int position, final T t);

    public BaseAdapter(final List<T> tList) {
        this.tList = tList;
    }

    public BaseAdapter(final List<T> tList, final IBaseOnItemClickListener<T> listener) {
        this.tList = tList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder<Binding> onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new BaseViewHolder<>(DataBindingUtil.inflate(inflater, getItemLayout(), parent, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull BaseViewHolder<Binding> holder, final int position) {
        final T t = tList.get(position);
        final View view = onBaseBindViewHolder(holder, position, t);

        if (listener != null && view != null) {
            view.setOnClickListener(v -> listener.onItemClick(t));
        }
    }

    public void replaceList(final List<T> tList) {
        this.tList = tList;
        notifyDataSetChanged();
    }

    public void addList(final List<T> tList) {
        if (this.tList == null) {
            this.tList = tList;
        } else {
            this.tList.addAll(tList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tList.size();
    }

    protected static class BaseViewHolder<BindingViewHolder extends ViewDataBinding> extends RecyclerView.ViewHolder {

        public final BindingViewHolder binding;

        BaseViewHolder(final BindingViewHolder itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
