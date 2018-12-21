package android.mvvm.mg.com.mvvm_android.ui.fragments.base;

import android.databinding.ViewDataBinding;
import android.mvvm.mg.com.mvvm_android.core.base.BaseOfflineFragment;
import android.mvvm.mg.com.mvvm_android.core.base.BaseViewModel;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;

public abstract class MVVMBaseOfflineFragment<ViewModel extends BaseViewModel, Binding extends ViewDataBinding> extends BaseOfflineFragment<ViewModel, Binding> implements IMVVMConstants {
}
