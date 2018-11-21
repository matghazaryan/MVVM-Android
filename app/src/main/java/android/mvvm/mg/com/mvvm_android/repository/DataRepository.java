package android.mvvm.mg.com.mvvm_android.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.mvvm.mg.com.mvvm_android.constants.IUrls;
import android.mvvm.mg.com.mvvm_android.constants.RequestKeys;
import android.mvvm.mg.com.mvvm_android.models.Configs;
import android.mvvm.mg.com.mvvm_android.models.RequestError;
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
import com.dm.dmnetworking.api_client.base.DMBaseRequestConfig;
import com.dm.dmnetworking.api_client.base.DMLiveDataBag;
import com.dm.dmnetworking.parser.DMJsonParser;
import com.dm.dmnetworking.parser.DMParserConfigs;
import org.json.JSONException;
import org.json.JSONObject;

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
    public DMLiveDataBag<Card, RequestError> getCardListFromNetwork(final Context context) {
        final DMBaseRequestConfig<Card, RequestError> config = new DMBaseRequestConfig<Card, RequestError>(context)
                .setUrl(IUrls.Method.CARDS)
                .setParserConfigs(new DMParserConfigs<>(Card.class, "data", "cards_list"))
                .setErrorParserConfigs(new DMParserConfigs<>(RequestError.class));

        return MVVMNetworking.getInstance().request(config);
    }

    @Override
    public void saveConfigs(final String json) {
        MVVMPrefUtils.saveConfigsJson(json);
    }

    @Override
    public Configs getConfigs() throws JSONException {
        return DMJsonParser.parseObject(new JSONObject(MVVMPrefUtils.getConfigsJson()), Configs.class, "data");
    }

    @Override
    public void saveToken(final String token) {
        MVVMPrefUtils.setToken(token);
    }

    @Override
    public String getToken() {
        return MVVMPrefUtils.getToken();
    }

    @Override
    public boolean isCheckedRemember() {
        return MVVMPrefUtils.isCheckedRemember();
    }

    @Override
    public void setRemember(final boolean isChecked) {
        MVVMPrefUtils.setRemember(isChecked);
    }

    @Override
    public void saveEmail(final String email) {
        MVVMPrefUtils.saveEmail(email);
    }

    @Override
    public String getEmail() {
        return MVVMPrefUtils.getEmail();
    }

    @Override
    public void saveProfilePhoto(final String path) {
        MVVMPrefUtils.saveProfilePhoto(path);
    }

    @Override
    public String getProfilePhoto() {
        return MVVMPrefUtils.getProfilePhoto();
    }

    @Override
    public void savePassword(final String password) {
        MVVMPrefUtils.savePassword(password);
    }

    @Override
    public String getPassword() {
        return MVVMPrefUtils.getPassword();
    }

    @Override
    public LiveData<List<Card>> getCardListFromDB(final Context context) {
        return CardHelper.getAllCard(context);
    }

    @Override
    public void insertCardList(final Context context, final List<Card> cardList, final IOnInsertAllListener listener) {
        CardHelper.insertCardList(context, cardList, listener);
    }

    @Override
    public void clearCardTable(final Context context, final IOnClearTableListener listener) {
        CardHelper.clearTable(context, listener);
    }
}
