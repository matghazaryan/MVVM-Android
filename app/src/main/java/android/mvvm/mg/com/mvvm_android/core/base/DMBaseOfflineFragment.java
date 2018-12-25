package android.mvvm.mg.com.mvvm_android.core.base;

import android.databinding.ViewDataBinding;
import com.dm.dmnetworking.DMNetworkLiveDataBag;

import java.util.Objects;

/**
 * This class for handle request and do not get no internet connection message(dialog) when not connected to the network
 *
 * @param <ViewModel> ViewModel extends BaseViewModel , this is main viewModel for fragment
 * @param <Binding>   this is auto generated class for binding view and use in the viewModel
 */

public abstract class DMBaseOfflineFragment<ViewModel extends DMBaseViewModel, Binding extends ViewDataBinding> extends DMBaseFragment<ViewModel, Binding> {

    @Override
    protected <O, ErrorRequest extends DMBaseIError> void handleRequestFor(DMNetworkLiveDataBag<O, ErrorRequest> liveDataBug, DMBaseIRequestListener<O> listener) {
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
        });
    }
}
