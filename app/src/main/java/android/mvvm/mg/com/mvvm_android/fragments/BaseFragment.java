package android.mvvm.mg.com.mvvm_android.fragments;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.activities.BaseActivity;
import android.mvvm.mg.com.mvvm_android.constants.IConstants;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;


public class BaseFragment extends Fragment implements IConstants {

    protected BaseActivity mActivity;

    public BaseFragment() {
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        mActivity = (BaseActivity) context;
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
