package android.mvvm.mg.com.mvvm_android.ui.fragments.base;

import java.util.List;

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
