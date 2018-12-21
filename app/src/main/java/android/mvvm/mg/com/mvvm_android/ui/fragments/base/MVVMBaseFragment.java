package android.mvvm.mg.com.mvvm_android.ui.fragments.base;

import android.databinding.ViewDataBinding;
import android.mvvm.mg.com.mvvm_android.core.base.BaseFragment;
import android.mvvm.mg.com.mvvm_android.core.base.BaseViewModel;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;

public abstract class MVVMBaseFragment<ViewModel extends BaseViewModel, Binding extends ViewDataBinding> extends BaseFragment<ViewModel, Binding> implements IMVVMConstants {
}
