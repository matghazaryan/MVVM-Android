package android.mvvm.mg.com.mvvm_android.core.base;

import java.util.List;

/**
 * IBaseOnPermissionSuccessListener is the interface for handle permission request
 */
public interface IBaseOnPermissionSuccessListener {

    default void onPermissionsGranted() {

    }

    default void onPermissionsGranted(final int requestCode, final String permission) {

    }

    default void onPermissionsGranted(final int requestCode, final List<String> permissionList) {

    }

    default void onPermissionsDenied() {

    }

    default void onPermissionsDenied(final int requestCode, final String permission) {

    }

    default void onPermissionsDenied(final int requestCode, final List<String> permissionList) {

    }
}
