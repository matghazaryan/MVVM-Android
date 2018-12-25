package android.mvvm.mg.com.mvvm_android.core.base;


import com.dm.dmnetworking.model.error.ErrorResponse;
import com.dm.dmnetworking.model.progress.FileProgress;
import com.dm.dmnetworking.model.success.SuccessResponse;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * IBaseRequestListener is the interface for handle network request
 *
 * @param <O> Object type which will parse from response json
 */
public interface DMBaseIRequestListener<O> {

    default void onSuccess(final O o) {
    }

    default void onSuccessList(final List<O> oList) {
    }

    default void onSuccessJsonObject(final JSONObject successJSONObject) {
    }

    default void onSuccessFileProgress(final FileProgress fileProgress) {
    }

    default void onSuccessFile(final File file) {
    }

    default void onSuccessResponse(final SuccessResponse successResponse) {
    }

    default void onErrorJsonResponse(final JSONObject errorJSONObject) {
    }

    default void onErrorResponse(final ErrorResponse errorResponse) {
    }
}
