package android.mvvm.mg.com.mvvm_android.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.mvvm.mg.com.mvvm_android.constants.IUrls;
import android.mvvm.mg.com.mvvm_android.constants.RequestKeys;
import android.mvvm.mg.com.mvvm_android.models.Configs;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
import android.mvvm.mg.com.mvvm_android.models.Transaction;
import android.mvvm.mg.com.mvvm_android.models.User;
import android.mvvm.mg.com.mvvm_android.networking.MVVMNetworking;
import android.mvvm.mg.com.mvvm_android.repository.API.IAPIHelper;
import android.mvvm.mg.com.mvvm_android.repository.db.IDBHelper;
import android.mvvm.mg.com.mvvm_android.repository.preference.IPreferenceHelper;
import android.mvvm.mg.com.mvvm_android.room.helpers.IOnClearTableListener;
import android.mvvm.mg.com.mvvm_android.room.helpers.IOnInsertAllListener;
import android.mvvm.mg.com.mvvm_android.room.helpers.card.CardHelper;
import android.mvvm.mg.com.mvvm_android.room.models.card.Card;
import android.mvvm.mg.com.mvvm_android.utils.MVVMPrefUtils;
import android.mvvm.mg.com.mvvm_android.utils.MVVMUtils;

import com.dm.dmnetworking.api_client.base.DMBaseRequestConfig;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;
import com.dm.dmnetworking.parser.DMJsonParser;
import com.dm.dmnetworking.parser.DMParserConfigs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRepository implements IAPIHelper, IDBHelper, IPreferenceHelper {

    private static DataRepository instance;

    public static DataRepository getInstance() {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public DMLiveDataBag<Configs, RequestError> apiGetConfigs(final Context context) {
        final DMBaseRequestConfig<Configs, RequestError> config = new DMBaseRequestConfig<Configs, RequestError>(context)
                .setUrl(IUrls.Method.CONFIGS)
                .setParserConfigs(new DMParserConfigs<>(Configs.class, "data"))
                .setErrorParserConfigs(new DMParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMLiveDataBag<User, RequestError> apiLogin(final Context context, final User user) {
        final Map<String, Object> params = new HashMap<>();
        params.put(RequestKeys.EMAIL, user.getEmail());
        params.put(RequestKeys.PASSWORD, user.getPassword());

        final DMBaseRequestConfig<User, RequestError> config = new DMBaseRequestConfig<User, RequestError>(context)
                .setUrl(IUrls.Method.LOGIN)
                .setParams(params)
                .setParserConfigs(new DMParserConfigs<>(User.class, "data"))
                .setErrorParserConfigs(new DMParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMLiveDataBag<Card, RequestError> apiGetCardListFromNetwork(final Context context) {
        final DMBaseRequestConfig<Card, RequestError> config = new DMBaseRequestConfig<Card, RequestError>(context)
                .setUrl(IUrls.Method.CARDS)
                .setParserConfigs(new DMParserConfigs<>(Card.class, "data", "cards_list"))
                .setErrorParserConfigs(new DMParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMLiveDataBag<String, RequestError> apiSendImage(final Context context, final String path) {
        final Map<String, Object> params = new HashMap<>();
        params.put(RequestKeys.IMAGE, new File(path));

        final DMBaseRequestConfig<String, RequestError> config = new DMBaseRequestConfig<String, RequestError>(context)
                .setUrl(IUrls.Method.SAVE_IMAGE)
                .setParams(params)
                .setErrorParserConfigs(new DMParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public DMLiveDataBag<Transaction, RequestError> apiGetTransactionList(final Context context, final int page) {

        final Map<String, Object> params = new HashMap<>();
        params.put(RequestKeys.PAGE, page);

        final DMBaseRequestConfig<Transaction, RequestError> config = new DMBaseRequestConfig<Transaction, RequestError>(context)
                .setUrl(MVVMUtils.getTransactionUrl(page))
                .setParams(params)
                .setParserConfigs(new DMParserConfigs<>(Transaction.class, "data"))
                .setErrorParserConfigs(new DMParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public void prefSaveConfigs(final String json) {
        MVVMPrefUtils.saveConfigsJson(json);
    }

    @Override
    public Configs prefGetConfigs() throws JSONException {
        return DMJsonParser.parseObject(new JSONObject(MVVMPrefUtils.getConfigsJson()), Configs.class, "data");
    }

    @Override
    public void prefSaveToken(final String token) {
        MVVMPrefUtils.setToken(token);
    }

    @Override
    public String prefGetToken() {
        return MVVMPrefUtils.getToken();
    }

    @Override
    public boolean prefIsCheckedRemember() {
        return MVVMPrefUtils.isCheckedRemember();
    }

    @Override
    public void prefSetRemember(final boolean isChecked) {
        MVVMPrefUtils.setRemember(isChecked);
    }

    @Override
    public void prefSaveProfilePhoto(final String path) {
        MVVMPrefUtils.saveProfilePhoto(path);
    }

    @Override
    public String prefGetProfilePhoto() {
        return MVVMPrefUtils.getProfilePhoto();
    }

    @Override
    public void prefLanguageCode(final String code) {
        MVVMPrefUtils.setLanguageCode(code);
    }

    @Override
    public String prefGetLanguageCode() {
        return MVVMPrefUtils.getLanguageCode();
    }

    @Override
    public LiveData<List<Card>> dbGetCardList(final Context context) {
        return CardHelper.getAllCard(context);
    }

    @Override
    public void dbInsertCardList(final Context context, final List<Card> cardList, final IOnInsertAllListener listener) {
        CardHelper.insertCardList(context, cardList, listener);
    }

    @Override
    public void dbClearCardTable(final Context context, final IOnClearTableListener listener) {
        CardHelper.clearTable(context, listener);
    }
}
