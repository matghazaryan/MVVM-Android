package android.mvvm.mg.com.mvvm_android.core.base;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import dmutils.com.dmutils.permission.DMEasyPermissions;

import java.util.Arrays;
import java.util.List;

/**
 * BasePermissionFragment is the abstract class which make easy work with permissions
 *
 * @param <ViewModel> ViewModel extends BaseViewModel , this is main viewModel for fragment
 * @param <Binding>   this is auto generated class for binding view and use in the viewModel
 */
public abstract class BasePermissionFragment<ViewModel extends BaseViewModel, Binding extends ViewDataBinding> extends BaseFragment<ViewModel, Binding> implements DMEasyPermissions.PermissionCallbacks {

    private IBaseOnPermissionSuccessListener mListener;

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mViewModel.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Function for easy request permission(s)
     *
     * @param listener    interface for handle permission request result
     * @param requestCode Request code for permission(s)
     * @param perms       permission(s)
     */
    protected void accessToPermission(final IBaseOnPermissionSuccessListener listener, final int requestCode, final String... perms) {
        mListener = listener;
        if (DMEasyPermissions.hasPermissions(getContext(), perms)) {
            listener.onPermissionsGranted();
            listener.onPermissionsGranted(requestCode, perms[0]);
            listener.onPermissionsGranted(requestCode, Arrays.asList(perms));
        } else {
            DMEasyPermissions.requestPermissions(this, null, requestCode, false, perms);
        }
    }

    @Override
    public void onPermissionsGranted(final int requestCode, final List<String> grantedPermissionList) {
        if (mListener != null) {
            mListener.onPermissionsGranted();
            mListener.onPermissionsGranted(requestCode, grantedPermissionList.size() != 0 ? grantedPermissionList.get(0) : "");
            mListener.onPermissionsGranted(requestCode, grantedPermissionList);
        }
    }

    @Override
    public void onPermissionsDenied(final int requestCode, final List<String> deniedPermissionList) {
        if (mListener != null) {
            mListener.onPermissionsDenied();
            mListener.onPermissionsDenied(requestCode, deniedPermissionList.size() != 0 ? deniedPermissionList.get(0) : "");
            mListener.onPermissionsDenied(requestCode, deniedPermissionList);
        }
    }
}
