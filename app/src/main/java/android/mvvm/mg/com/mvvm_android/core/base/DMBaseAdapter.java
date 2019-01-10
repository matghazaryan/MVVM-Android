package android.mvvm.mg.com.mvvm_android.core.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * BaseAdapter abstract class for make easy and fast work with adapter
 *
 * @param <T>       is object which use in the current adapter
 * @param <Binding> binding is auto generated class on adapter view (item.xml)
 */
public abstract class DMBaseAdapter<T, Binding extends ViewDataBinding> extends RecyclerView.Adapter<DMBaseAdapter.BaseViewHolder<Binding>> {

    protected List<T> tList;
    protected LayoutInflater inflater;
    protected DMBaseIOnItemClickListener<T> listener;

    /**
     * Give layout resource id at adapter view (item.xml)
     *
     * @return Layout resource id
     */
    protected abstract int getItemLayout();

    /**
     * This function for set object to binding and set view for on click
     *
     * @param holder   view holder
     * @param position item position
     * @param t        object get by position
     * @return View which must have click, if you don't want for item has click return null
     */
    protected abstract View onBaseBindViewHolder(final @NonNull BaseViewHolder<Binding> holder, final int position, final T t);

    protected DMBaseAdapter(final List<T> tList) {
        this.tList = tList;
    }

    protected DMBaseAdapter(final List<T> tList, final DMBaseIOnItemClickListener<T> listener) {
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
