package android.mvvm.mg.com.mvvm_android.repository.API;

import android.content.Context;
import android.mvvm.mg.com.mvvm_android.models.Configs;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.mvvm.mg.com.mvvm_android.room.models.card.Card;

import com.dm.dmnetworking.api_client.base.DMLiveDataBag;

public interface IAPIHelper {

    DMLiveDataBag<Configs, RequestError> getConfigs(Context context);

    DMLiveDataBag<User, RequestError> login(Context context, User user);

    DMLiveDataBag<Card, RequestError> getCardListFromNetwork(Context context);

    DMLiveDataBag<String, RequestError> sendImage(Context context, String path);
}
