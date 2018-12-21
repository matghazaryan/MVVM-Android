package android.mvvm.mg.com.mvvm_android.core.base;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

public abstract class BaseViewModelItemClick<O, Empty> extends BaseViewModelEmptyView<Empty> implements IBaseOnItemClickListener<O> {

    public final ObservableField<IBaseOnItemClickListener<O>> baseOnItemClickListener = new ObservableField<>();

    protected BaseViewModelItemClick(final @NonNull Application application) {
        super(application);
        baseOnItemClickListener.set(this);
    }
}
