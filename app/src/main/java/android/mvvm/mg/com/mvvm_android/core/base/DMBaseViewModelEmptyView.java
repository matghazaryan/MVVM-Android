package android.mvvm.mg.com.mvvm_android.core.base;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

/**
 * BaseViewModelEmptyView is abstract class for show empty view , prepare and set empty object for show in view
 *
 * @param <Empty> Empty is object type for send data and show in the empty view, use in the recycler view
 */
public abstract class DMBaseViewModelEmptyView<Empty> extends DMBaseViewModel implements DmBaseIEmptyViewListener, DMBaseIEmpty<Empty> {

    public final ObservableField<DmBaseIEmptyViewListener> baseEmptyViewListener = new ObservableField<>();
    public final ObservableField<Boolean> isBaseEmptyViewVisible = new ObservableField<>(false);
    public final ObservableField<Empty> baseEmpty = new ObservableField<>();

    private boolean isEnableEmptyView = true;
    private boolean isEnableEmptyFromNetwork = true;

    protected DMBaseViewModelEmptyView(final @NonNull Application application) {
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
    void setEnableEmptyViewFromNetwork(final boolean isEnableEmptyFromNetwork) {
        this.isEnableEmptyFromNetwork = isEnableEmptyFromNetwork;
    }
}
