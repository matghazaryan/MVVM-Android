package android.mvvm.mg.com.mvvm_android.ui.fragments.base;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

public abstract class BaseViewModelEmptyView<Empty> extends BaseViewModel implements IBaseEmptyViewListener, IBaseEmptyMethods<Empty> {

    public final ObservableField<IBaseEmptyViewListener> emptyViewListener = new ObservableField<>();
    public final ObservableField<Boolean> isEmptyViewVisible = new ObservableField<>(false);
    public final ObservableField<Empty> empty = new ObservableField<>();

    private boolean isVisible;

    protected BaseViewModelEmptyView(final @NonNull Application application) {
        super(application);
        emptyViewListener.set(this);
        empty.set(getEmptyObject());
    }

    @Override
    public void onVisible(final boolean isVisible) {
        Log.d("aaaaaa",isVisible + "");

//        final Boolean isProgressDialogVisible = this.isProgressDialogVisible.get();
//        if (isProgressDialogVisible != null && isProgressDialogVisible) {
////            this.isVisible = isVisible;
//        } else {
//            isEmptyViewVisible.set(isVisible);
//        }

            isEmptyViewVisible.set(isVisible);
    }
}
