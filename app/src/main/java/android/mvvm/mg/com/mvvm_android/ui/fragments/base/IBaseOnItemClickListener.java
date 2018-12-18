package android.mvvm.mg.com.mvvm_android.ui.fragments.base;

public interface IBaseOnItemClickListener<T> extends IBaseEmptyViewListener {

    void onItemClick(final T t);
}
