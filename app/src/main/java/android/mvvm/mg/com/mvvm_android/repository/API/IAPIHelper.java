package android.mvvm.mg.com.mvvm_android.repository.API;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.models.Configs;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.Transaction;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.mvvm.mg.com.mvvm_android.room.models.card.Card;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

public interface IAPIHelper {

    DMLiveDataBag<Configs, RequestError> apiGetConfigs(Context context);

    DMLiveDataBag<User, RequestError> apiLogin(Context context, User user);

    DMLiveDataBag<Card, RequestError> apiGetCardListFromNetwork(Context context);

    DMLiveDataBag<String, RequestError> apiSendImage(Context context, String path);

    DMLiveDataBag<Transaction, RequestError> apiGetTransactionList(final Context context, final int page);
}
