package android.mvvm.mg.com.mvvm_android.fragments.base;

import com.dm.dmnetworking.api_client.base.model.progress.FileProgress;
import org.json.JSONObject;

import java.util.List;

public interface IBaseRequestListener<O> {

    default void onSuccess(O o) {
    }

    default void onSuccessList(List<O> oList) {
    }

    default void onSuccessJsonObject(JSONObject jsonObject) {
    }

    default void onSuccessFileProgress(FileProgress fileProgress) {
    }
}
