package android.mvvm.mg.com.mvvm_android.core.repository.api;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.core.constants.IRequestKeys;
import android.mvvm.mg.com.mvvm_android.core.constants.IUrls;
import android.mvvm.mg.com.mvvm_android.core.models.Configs;
import android.mvvm.mg.com.mvvm_android.core.models.User;
import android.mvvm.mg.com.mvvm_android.core.models.error.RequestError;
import android.mvvm.mg.com.mvvm_android.core.models.room.card.Card;
import android.mvvm.mg.com.mvvm_android.core.models.transaction.TransactionData;
import android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.api.MVVMNetworking;
import android.mvvm.mg.com.mvvm_android.core.utils.MVVMUtils;
import com.dm.dmnetworking.DMNetworkBaseRequestConfig;
import com.dm.dmnetworking.DMNetworkLiveDataBag;
import com.dm.dmnetworking.DMNetworkParserConfigs;

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
    public DMNetworkLiveDataBag<Configs, RequestError> getConfigs(final Context context) {
        final DMNetworkBaseRequestConfig<Configs, RequestError> config = new DMNetworkBaseRequestConfig<Configs, RequestError>(context)
                .setUrl(IUrls.Method.CONFIGS)
                .setParserConfigs(new DMNetworkParserConfigs<>(Configs.class, "data"))
                .setErrorParserConfigs(new DMNetworkParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMNetworkLiveDataBag<User, RequestError> login(final Context context, final User user) {
        final Map<String, Object> params = new HashMap<>();
        params.put(IRequestKeys.EMAIL, user.getEmail());
        params.put(IRequestKeys.PASSWORD, user.getPassword());

        final DMNetworkBaseRequestConfig<User, RequestError> config = new DMNetworkBaseRequestConfig<User, RequestError>(context)
                .setUrl(IUrls.Method.LOGIN)
                .setParams(params)
                .setParserConfigs(new DMNetworkParserConfigs<>(User.class, "data"))
                .setErrorParserConfigs(new DMNetworkParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMNetworkLiveDataBag<String, RequestError> logout(final Context context) {
        final DMNetworkBaseRequestConfig<String, RequestError> config = new DMNetworkBaseRequestConfig<String, RequestError>(context)
                .setUrl(IUrls.Method.LOGOUT)
                .setErrorParserConfigs(new DMNetworkParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMNetworkLiveDataBag<Card, RequestError> getCardListFromNetwork(final Context context) {
        final DMNetworkBaseRequestConfig<Card, RequestError> config = new DMNetworkBaseRequestConfig<Card, RequestError>(context)
                .setUrl(IUrls.Method.CARDS)
                .setParserConfigs(new DMNetworkParserConfigs<>(Card.class, "data", "cards_list"))
                .setErrorParserConfigs(new DMNetworkParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMNetworkLiveDataBag<String, RequestError> sendImage(final Context context, final String path) {
        final Map<String, Object> params = new HashMap<>();
        params.put(IRequestKeys.IMAGE, new File(path));

        final DMNetworkBaseRequestConfig<String, RequestError> config = new DMNetworkBaseRequestConfig<String, RequestError>(context)
                .setUrl(IUrls.Method.SAVE_IMAGE)
                .setParams(params)
                .setErrorParserConfigs(new DMNetworkParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMNetworkLiveDataBag<TransactionData, RequestError> getTransactionList(final Context context, final int page) {

        final Map<String, Object> params = new HashMap<>();
        params.put(IRequestKeys.PAGE, page);

        final DMNetworkBaseRequestConfig<TransactionData, RequestError> config = new DMNetworkBaseRequestConfig<TransactionData, RequestError>(context)
                .setUrl(MVVMUtils.getTransactionUrl(page))
                .setParams(params)
                .setParserConfigs(new DMNetworkParserConfigs<>(TransactionData.class, "data"))
                .setErrorParserConfigs(new DMNetworkParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }
}
