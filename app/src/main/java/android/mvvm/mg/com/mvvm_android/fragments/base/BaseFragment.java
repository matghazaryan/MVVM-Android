package android.mvvm.mg.com.mvvm_android.fragments.base;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.activities.BaseActivity;
import android.mvvm.mg.com.mvvm_android.constants.IConstants;
import android.mvvm.mg.com.mvvm_android.dialog.MVVMDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment<T extends BaseViewModel> extends Fragment implements IConstants {

    protected BaseActivity mActivity;

    protected T t;

    public BaseFragment() {
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        mActivity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        baseSubscribes();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void baseSubscribes() {
        t.<String>getAction(Action.OPEN_ERROR_DIALOG).observe(this, s -> MVVMDialog.showErrorDialog(mActivity, s));
    }

    protected void setTitle(final String title) {
        mActivity.setTitle(title);
    }

    protected void setTitle(final int title) {
        mActivity.setTitle(title);
    }

    protected void hideActionBar() {
        final ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    protected void showActionBar() {
        final ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }
}
