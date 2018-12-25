package android.mvvm.mg.com.mvvm_android.core.repository.api;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.core.models.Configs;
import android.mvvm.mg.com.mvvm_android.core.models.User;
import android.mvvm.mg.com.mvvm_android.core.models.error.RequestError;
import android.mvvm.mg.com.mvvm_android.core.models.room.card.Card;
import android.mvvm.mg.com.mvvm_android.core.models.transaction.TransactionData;
import com.dm.dmnetworking.DMNetworkLiveDataBag;


public interface IAPIHelper {

    DMNetworkLiveDataBag<Configs, RequestError> getConfigs(Context context);

    DMNetworkLiveDataBag<User, RequestError> login(Context context, User user);

    DMNetworkLiveDataBag<String, RequestError> logout(final Context context);

    DMNetworkLiveDataBag<Card, RequestError> getCardListFromNetwork(Context context);

    DMNetworkLiveDataBag<String, RequestError> sendImage(Context context, String path);

    DMNetworkLiveDataBag<TransactionData, RequestError> getTransactionList(final Context context, final int page);
}
