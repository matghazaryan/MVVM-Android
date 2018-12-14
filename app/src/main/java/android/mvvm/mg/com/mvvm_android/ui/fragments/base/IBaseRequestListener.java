package android.mvvm.mg.com.mvvm_android.ui.fragments.base;

import com.dm.dmnetworking.api_client.base.model.progress.FileProgress;

import org.json.JSONObject;

import java.util.List;

public interface IBaseRequestListener<O> {

    default void onSuccess(final O o) {
    }

    default void onSuccessList(final List<O> oList) {
    }

    default void onSuccessJsonObject(final JSONObject jsonObject) {
    }

    default void onSuccessFileProgress(final FileProgress fileProgress) {
    }
}
