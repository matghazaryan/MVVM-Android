package android.mvvm.mg.com.mvvm_android.ui.fragments.base;

import android.databinding.ViewDataBinding;
import android.mvvm.mg.com.mvvm_android.core.base.BasePermissionFragment;
import android.mvvm.mg.com.mvvm_android.core.base.BaseViewModel;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;

public abstract class MVVMBasePermissionFragment<ViewModel extends BaseViewModel, Binding extends ViewDataBinding> extends BasePermissionFragment<ViewModel, Binding> implements IMVVMConstants {
}
