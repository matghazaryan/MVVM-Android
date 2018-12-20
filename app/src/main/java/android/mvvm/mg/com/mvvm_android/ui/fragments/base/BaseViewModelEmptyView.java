package android.mvvm.mg.com.mvvm_android.ui.fragments.base;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

public abstract class BaseViewModelEmptyView<Empty> extends BaseViewModel implements IBaseEmptyViewListener, IBaseEmptyMethods<Empty> {

    public final ObservableField<IBaseEmptyViewListener> emptyViewListener = new ObservableField<>();
    public final ObservableField<Boolean> isEmptyViewVisible = new ObservableField<>(false);
    public final ObservableField<Empty> empty = new ObservableField<>();

    private boolean isEnableEmptyView = true;
    private boolean isEnableEmptyFromNetwork = true;

    protected BaseViewModelEmptyView(final @NonNull Application application) {
        super(application);
        emptyViewListener.set(this);
        empty.set(getEmptyObject());
    }

    @Override
    public void onVisible(final boolean isVisible) {
        if (isEnableEmptyFromNetwork && isEnableEmptyView) {
            isEmptyViewVisible.set(isVisible);
        }
    }

    @Override
    public void setEnableEmptyView(final boolean enableEmptyView) {
        isEnableEmptyView = enableEmptyView;
    }

    @Override
    void setEnableEmptyViewFromNetwork(final boolean enableEmptyView) {
        isEnableEmptyFromNetwork = enableEmptyView;
    }
}
