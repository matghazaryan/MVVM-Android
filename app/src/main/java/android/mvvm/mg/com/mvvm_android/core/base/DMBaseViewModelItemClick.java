package android.mvvm.mg.com.mvvm_android.core.base;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * BaseViewModelItemClick is the abstract class for handle click in the BaseAdapter
 *
 * @param <O>     Object type which will return after click on item view
 * @param <Empty> Empty is object type for send data and show in the empty view, use in the recycler view
 */
public abstract class DMBaseViewModelItemClick<O, Empty> extends DMBaseViewModelEmptyView<Empty> implements DMBaseIOnItemClickListener<O> {

    public final ObservableField<DMBaseIOnItemClickListener<O>> baseOnItemClickListener = new ObservableField<>();

    public final ObservableField<List<O>> baseList = new ObservableField<>();

    protected DMBaseViewModelItemClick(final @NonNull Application application) {
        super(application);
        baseOnItemClickListener.set(this);
    }

    /**
     * Base function for set list in the fragment
     *
     * @param oList list which need to send to the RecycleView, must be binding witch baseList
     */
    public void setBaseList(final List<O> oList) {
        this.baseList.set(oList);
    }
}
