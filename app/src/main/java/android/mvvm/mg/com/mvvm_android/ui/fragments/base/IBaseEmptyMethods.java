package android.mvvm.mg.com.mvvm_android.ui.fragments.base;

public interface IBaseEmptyMethods<Empty> {

    default Empty getEmptyObject() {
        return null;
    }
}
