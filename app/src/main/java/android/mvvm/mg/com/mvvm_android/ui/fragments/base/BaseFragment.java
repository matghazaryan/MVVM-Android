package android.mvvm.mg.com.mvvm_android.ui.fragments.base;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.mvvm.mg.com.mvvm_android.core.constants.IMVVMConstants;
import android.mvvm.mg.com.mvvm_android.core.dialog.MVVMDialog;
import android.mvvm.mg.com.mvvm_android.ui.activities.base.BaseActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

import java.util.Objects;

public abstract class BaseFragment<ViewModel extends BaseViewModel, Binding extends ViewDataBinding> extends Fragment implements IBaseMethod, IMVVMConstants {

    protected BaseActivity mActivity;

    protected ViewModel mViewModel;

    protected Binding mBinding;

    protected BaseFragment() {
    }

    protected abstract int getLayoutRes();

    protected abstract Class<ViewModel> getViewModelClass();

    protected abstract void setBinding(final Binding binding, final ViewModel viewModel);

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        mActivity = (BaseActivity) context;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initActionBar();
        initialize();
        mViewModel.initialize();
        mViewModel.initialize(getArguments());
        subscribers(getViewLifecycleOwner());
    }

    @Nullable
    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false, DataBindingUtil.getDefaultComponent());
        mViewModel = ViewModelProviders.of(this).get(getViewModelClass());

        setBinding(mBinding, mViewModel);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        baseSubscribes();
    }

    private void initActionBar() {
        setTitle(getTitle() != null ? getTitle() : getString(getTitleRes()));
        if (isShowActionBar()) {
            showActionBar();
        } else {
            hideActionBar();
        }
    }

    private void baseSubscribes() {
        mViewModel.<String>getAction(Action.OPEN_ERROR_DIALOG).observe(getViewLifecycleOwner(), s -> MVVMDialog.showErrorDialog(mActivity, s));
        mViewModel.<String>getAction(Action.SHOW_NO_INTERNET).observe(getViewLifecycleOwner(), s -> MVVMDialog.showNoInternetDialog(mActivity));
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

    protected <O, ErrorRequest extends IBaseError> void handleRequestFor(final DMLiveDataBag<O, ErrorRequest> liveDataBug, final IBaseRequestListener<O> listener) {
        mViewModel.showProgress();

        liveDataBug.getSuccessT().observe(getViewLifecycleOwner(), oSuccessT -> {
            listener.onSuccess(Objects.requireNonNull(oSuccessT).getT());
            mViewModel.hideProgress();
        });
        liveDataBug.getSuccessListT().observe(getViewLifecycleOwner(), oSuccessListT -> {
            listener.onSuccessList(Objects.requireNonNull(oSuccessListT).getList());
            mViewModel.hideProgress();
        });
        liveDataBug.getSuccessJsonResponse().observe(getViewLifecycleOwner(), successJSONObject -> {
            listener.onSuccessJsonObject(successJSONObject);
            mViewModel.hideProgress();
        });
        liveDataBug.getSuccessResponse().observe(getViewLifecycleOwner(), successResponse -> {
            listener.onSuccessResponse(successResponse);
            mViewModel.hideProgress();
        });
        liveDataBug.getFileProgress().observe(getViewLifecycleOwner(), fileProgress -> {
            listener.onSuccessFileProgress(fileProgress);
            mViewModel.hideProgress();
        });
        liveDataBug.getSuccessFile().observe(getViewLifecycleOwner(), file -> {
            listener.onSuccessFile(file);
            mViewModel.hideProgress();
        });
        liveDataBug.getErrorJsonResponse().observe(getViewLifecycleOwner(), errorJSONObject -> {
            listener.onErrorJsonResponse(errorJSONObject);
            mViewModel.hideProgress();
        });
        liveDataBug.getErrorResponse().observe(getViewLifecycleOwner(), errorResponse -> {
            listener.onErrorResponse(errorResponse);
            mViewModel.hideProgress();
        });
        liveDataBug.getErrorE().observe(getViewLifecycleOwner(), error -> {
            mViewModel.handleErrors(error);
            mViewModel.hideProgress();
        });
        liveDataBug.getNoInternetConnection().observe(getViewLifecycleOwner(), s -> {
            mViewModel.noInternetConnection();
            mViewModel.hideProgress();
        });
    }
}
