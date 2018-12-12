package android.mvvm.mg.com.mvvm_android.repository.api;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.models.Configs;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.TransactionData;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.mvvm.mg.com.mvvm_android.repository.repositoryManager.db.models.card.Card;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

public interface IAPIHelper {

    DMLiveDataBag<Configs, RequestError> apiGetConfigs(Context context);

    DMLiveDataBag<User, RequestError> apiLogin(Context context, User user);

    DMLiveDataBag<String, RequestError> apiLogout(final Context contex);

    DMLiveDataBag<Card, RequestError> apiGetCardListFromNetwork(Context context);

    DMLiveDataBag<String, RequestError> apiSendImage(Context context, String path);

    DMLiveDataBag<TransactionData, RequestError> apiGetTransactionList(final Context context, final int page);
}
