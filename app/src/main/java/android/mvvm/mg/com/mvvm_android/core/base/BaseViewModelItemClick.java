package android.mvvm.mg.com.mvvm_android.core.base;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

/**
 * BaseViewModelItemClick is the abstract class for handle click in the BaseAdapter
 *
 * @param <O>     Object type which will return after click on item view
 * @param <Empty> Empty is object type for send data and show in the empty view, use in the recycler view
 */
public abstract class BaseViewModelItemClick<O, Empty> extends BaseViewModelEmptyView<Empty> implements IBaseOnItemClickListener<O> {

    public final ObservableField<IBaseOnItemClickListener<O>> baseOnItemClickListener = new ObservableField<>();

    protected BaseViewModelItemClick(final @NonNull Application application) {
        super(application);
        baseOnItemClickListener.set(this);
    }
}
