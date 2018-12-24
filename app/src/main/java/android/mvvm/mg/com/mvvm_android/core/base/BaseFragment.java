package android.mvvm.mg.com.mvvm_android.core.base;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.mvvm.mg.com.mvvm_android.BuildConfig;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

import java.util.Objects;


/**
 * BaseFragment is the abstract class where declared functions which must have each fragment for make work more easy and fast
 * contains functions for initialize ,initialize in viewModel, subscribers, initActionBar
 * <p>
 * initActionBar();
 * initialize();
 * mViewModel.initialize();
 * mViewModel.initialize(getArguments());
 * subscribers(getViewLifecycleOwner());
 *
 * @param <ViewModel> ViewModel extends BaseViewModel , this is main viewModel for fragment
 * @param <Binding>   this is auto generated class for binding view and use in the viewModel
 */
public abstract class BaseFragment<ViewModel extends BaseViewModel, Binding extends ViewDataBinding> extends Fragment implements IBaseFragment, IBaseConstants {

    protected BaseActivity mActivity;

    protected ViewModel mViewModel;

    protected Binding mBinding;

    protected BaseApplicationConfigs mApplicationConfigs;

    protected BaseFragment() {
    }

    /**
     * Give layout resource id at fragment which extends BaseFragment
     *
     * @return Layout resource id
     */
    protected abstract int getLayoutRes();

    /**
     * Give viewModel class for current fragment which extends BaseFragment
     *
     * @return ViewModel class for current fragment, like Object.class
     */
    protected abstract Class<ViewModel> getViewModelClass();

    /**
     * Functions where we must set viewModel to binding and if necessary set handler for click and other things what we need to
     * setup before fragment showing
     *
     * @param binding   Binding view for current fragment
     * @param viewModel ViewModel for current fragment
     */
    protected abstract void setBinding(final Binding binding, final ViewModel viewModel);

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        mActivity = (BaseActivity) context;
        mApplicationConfigs = ((BaseApplication) Objects.requireNonNull(getActivity()).getApplication()).getApplicationConfigs();

        if (BuildConfig.DEBUG) {
            Log.d(mApplicationConfigs.getTag(), "-----------------------------------------------------------------------------------------------------> " + this.getClass().getSimpleName());
        }
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

    /**
     * Base subscribes for show error dialog and no internet dialog
     */
    private void baseSubscribes() {
        mViewModel.<String>getAction(BaseAction.SHOW_ERROR_DIALOG).observe(getViewLifecycleOwner(), s -> mApplicationConfigs.showErrorDialog(mActivity, s));
        mViewModel.<String>getAction(BaseAction.SHOW_NO_INTERNET_DIALOG).observe(getViewLifecycleOwner(), s -> mApplicationConfigs.showNoInternetDialog(mActivity));
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

    /**
     * Function for handle liveDataBug make easy to use network handling
     *
     * @param liveDataBug    this is bag which contain live data which give us way to subscribe and get parsed object
     * @param listener       interface for get result which we need, success response , error response object, error object, json atc
     * @param <O>            Parse object which build from success json parse
     * @param <ErrorRequest> Error object which build form error json parse
     */
    protected <O, ErrorRequest extends IBaseError> void handleRequestFor(final DMLiveDataBag<O, ErrorRequest> liveDataBug, final IBaseRequestListener<O> listener) {
        mViewModel.showProgress();

        liveDataBug.getSuccessT().observe(getViewLifecycleOwner(), oSuccessT -> {
            mViewModel.hideProgress();
            listener.onSuccess(Objects.requireNonNull(oSuccessT).getT());
        });
        liveDataBug.getSuccessListT().observe(getViewLifecycleOwner(), oSuccessListT -> {
            mViewModel.hideProgress();
            listener.onSuccessList(Objects.requireNonNull(oSuccessListT).getList());
        });
        liveDataBug.getSuccessJsonResponse().observe(getViewLifecycleOwner(), successJSONObject -> {
            mViewModel.hideProgress();
            listener.onSuccessJsonObject(successJSONObject);
        });
        liveDataBug.getSuccessResponse().observe(getViewLifecycleOwner(), successResponse -> {
            mViewModel.hideProgress();
            listener.onSuccessResponse(successResponse);
        });
        liveDataBug.getFileProgress().observe(getViewLifecycleOwner(), fileProgress -> {
            mViewModel.hideProgress();
            listener.onSuccessFileProgress(fileProgress);
        });
        liveDataBug.getSuccessFile().observe(getViewLifecycleOwner(), file -> {
            mViewModel.hideProgress();
            listener.onSuccessFile(file);
        });
        liveDataBug.getErrorJsonResponse().observe(getViewLifecycleOwner(), errorJSONObject -> {
            mViewModel.hideProgress();
            listener.onErrorJsonResponse(errorJSONObject);
        });
        liveDataBug.getErrorResponse().observe(getViewLifecycleOwner(), errorResponse -> {
            mViewModel.hideProgress();
            listener.onErrorResponse(errorResponse);
        });
        liveDataBug.getErrorE().observe(getViewLifecycleOwner(), error -> {
            mViewModel.hideProgress();
            mViewModel.handleErrors(error);
        });
        liveDataBug.getNoInternetConnection().observe(getViewLifecycleOwner(), s -> {
            mViewModel.hideProgress();
            mViewModel.noInternetConnection();
        });
    }
}
