package android.mvvm.mg.com.mvvm_android.core.base;

public interface IBaseEmpty<Empty> {

    default Empty getEmptyObject() {
        return null;
    }
}
