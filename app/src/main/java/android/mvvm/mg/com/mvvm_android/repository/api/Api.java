package android.mvvm.mg.com.mvvm_android.repository.api;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.constants.IRequestKeys;
import android.mvvm.mg.com.mvvm_android.constants.IUrls;
import android.mvvm.mg.com.mvvm_android.models.Configs;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.TransactionData;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.api.MVVMNetworking;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.models.card.Card;
import android.mvvm.mg.com.mvvm_android.utils.MVVMUtils;

import com.dm.dmnetworking.api_client.base.DMBaseRequestConfig;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;
import com.dm.dmnetworking.parser.DMParserConfigs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class Api implements IAPIHelper {

    private static Api instance;

    public static Api getInstance() {
        if (instance == null) {
            synchronized (Api.class) {
                if (instance == null) {
                    instance = new Api();
                }
            }
        }
        return instance;
    }

    @Override
    public DMLiveDataBag<Configs, RequestError> getConfigs(final Context context) {
        final DMBaseRequestConfig<Configs, RequestError> config = new DMBaseRequestConfig<Configs, RequestError>(context)
                .setUrl(IUrls.Method.CONFIGS)
                .setParserConfigs(new DMParserConfigs<>(Configs.class, "data"))
                .setErrorParserConfigs(new DMParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMLiveDataBag<User, RequestError> login(final Context context, final User user) {
        final Map<String, Object> params = new HashMap<>();
        params.put(IRequestKeys.EMAIL, user.getEmail());
        params.put(IRequestKeys.PASSWORD, user.getPassword());

        final DMBaseRequestConfig<User, RequestError> config = new DMBaseRequestConfig<User, RequestError>(context)
                .setUrl(IUrls.Method.LOGIN)
                .setParams(params)
                .setParserConfigs(new DMParserConfigs<>(User.class, "data"))
                .setErrorParserConfigs(new DMParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMLiveDataBag<String, RequestError> logout(final Context context) {
        final DMBaseRequestConfig<String, RequestError> config = new DMBaseRequestConfig<String, RequestError>(context)
                .setUrl(IUrls.Method.LOGOUT)
                .setErrorParserConfigs(new DMParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMLiveDataBag<Card, RequestError> getCardListFromNetwork(final Context context) {
        final DMBaseRequestConfig<Card, RequestError> config = new DMBaseRequestConfig<Card, RequestError>(context)
                .setUrl(IUrls.Method.CARDS)
                .setParserConfigs(new DMParserConfigs<>(Card.class, "data", "cards_list"))
                .setErrorParserConfigs(new DMParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMLiveDataBag<String, RequestError> sendImage(final Context context, final String path) {
        final Map<String, Object> params = new HashMap<>();
        params.put(IRequestKeys.IMAGE, new File(path));

        final DMBaseRequestConfig<String, RequestError> config = new DMBaseRequestConfig<String, RequestError>(context)
                .setUrl(IUrls.Method.SAVE_IMAGE)
                .setParams(params)
                .setErrorParserConfigs(new DMParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMLiveDataBag<TransactionData, RequestError> getTransactionList(final Context context, final int page) {

        final Map<String, Object> params = new HashMap<>();
        params.put(IRequestKeys.PAGE, page);

        final DMBaseRequestConfig<TransactionData, RequestError> config = new DMBaseRequestConfig<TransactionData, RequestError>(context)
                .setUrl(MVVMUtils.getTransactionUrl(page))
                .setParams(params)
                .setParserConfigs(new DMParserConfigs<>(TransactionData.class, "data"))
                .setErrorParserConfigs(new DMParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }
}
