package android.mvvm.mg.com.mvvm_android.fragments.base;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.activities.BaseActivity;
import android.mvvm.mg.com.mvvm_android.constants.IConstants;
import android.mvvm.mg.com.mvvm_android.dialog.MVVMDialog;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

import java.util.Objects;


public abstract class BaseFragment<T extends BaseViewModel> extends Fragment implements IBaseMethod, IConstants {

    protected BaseActivity mActivity;

    protected T mViewModel;

    private boolean isSubscribed;

    public BaseFragment() {
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        mActivity = (BaseActivity) context;
        isSubscribed = false;
    }

    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        if (!isSubscribed) {
            isSubscribed = true;
            subscribes();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        baseSubscribes();
    }

    private void baseSubscribes() {
        mViewModel.<String>getAction(Action.OPEN_ERROR_DIALOG).observe(this, s -> MVVMDialog.showErrorDialog(mActivity, s));
        mViewModel.<String>getAction(Action.SHOW_NO_INTERNET).observe(this, s -> MVVMDialog.showNoInternetDialog(mActivity));
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

    protected <O> void handleRequest(final DMLiveDataBag<O, RequestError> liveDataBug, final IBaseRequestListener<O> listener) {
        mViewModel.showProgress();

        liveDataBug.getSuccessT().observe(this, oSuccessT -> {
            mViewModel.hideProgress();
            listener.onSuccess(Objects.requireNonNull(oSuccessT).getT());
        });

        liveDataBug.getSuccessListT().observe(this, oSuccessListT -> {
            mViewModel.hideProgress();
            listener.onSuccessList(Objects.requireNonNull(oSuccessListT).getList());
        });
        liveDataBug.getSuccessJsonResponse().observe(this, jsonObject -> {
            mViewModel.hideProgress();
            listener.onSuccessJsonObject(jsonObject);
        });
        liveDataBug.getFileProgress().observe(this, fileProgress -> {
            mViewModel.hideProgress();
            listener.onSuccessFileProgress(fileProgress);
        });

        liveDataBug.getErrorE().observe(this, error -> mViewModel.handleErrors(error));
        liveDataBug.getNoInternetConnection().observe(this, s -> mViewModel.noInternetConnection());
    }
}
