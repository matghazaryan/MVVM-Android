package android.mvvm.mg.com.mvvm_android.core.base;

/**
 * IBaseOnItemClickListener is the interface for handle click in BaseAdapter
 *
 * @param <T> Object type which will return, when click on item view
 */
public interface DMBaseIOnItemClickListener<T> extends DmBaseIEmptyViewListener {

    void onItemClick(final T t);
}
