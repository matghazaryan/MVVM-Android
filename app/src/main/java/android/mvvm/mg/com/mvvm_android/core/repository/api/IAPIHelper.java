package android.mvvm.mg.com.mvvm_android.core.repository.api;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.core.models.Configs;
import android.mvvm.mg.com.mvvm_android.core.models.RequestError;
import android.mvvm.mg.com.mvvm_android.core.models.TransactionData;
import android.mvvm.mg.com.mvvm_android.core.models.User;
import android.mvvm.mg.com.mvvm_android.core.models.room.card.Card;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

public interface IAPIHelper {

    DMLiveDataBag<Configs, RequestError> getConfigs(Context context);

    DMLiveDataBag<User, RequestError> login(Context context, User user);

    DMLiveDataBag<String, RequestError> logout(final Context context);

    DMLiveDataBag<Card, RequestError> getCardListFromNetwork(Context context);

    DMLiveDataBag<String, RequestError> sendImage(Context context, String path);

    DMLiveDataBag<TransactionData, RequestError> getTransactionList(final Context context, final int page);
}
