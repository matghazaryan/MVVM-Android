package android.mvvm.mg.com.mvvm_android.core.base;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

public abstract class BaseViewModelEmptyView<Empty> extends BaseViewModel implements IBaseEmptyViewListener, IBaseEmpty<Empty> {

    public final ObservableField<IBaseEmptyViewListener> baseEmptyViewListener = new ObservableField<>();
    public final ObservableField<Boolean> isBaseEmptyViewVisible = new ObservableField<>(false);
    public final ObservableField<Empty> baseEmpty = new ObservableField<>();

    private boolean isEnableEmptyView = true;
    private boolean isEnableEmptyFromNetwork = true;

    protected BaseViewModelEmptyView(final @NonNull Application application) {
        super(application);
        baseEmptyViewListener.set(this);
        baseEmpty.set(getEmptyObject());
    }

    @Override
    public void onEmptyVisible(final boolean isEmptyVisible) {
        if (isEnableEmptyFromNetwork && isEnableEmptyView) {
            isBaseEmptyViewVisible.set(isEmptyVisible);
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
