package android.mvvm.mg.com.mvvm_android.core.repository.preference;

import android.mvvm.mg.com.mvvm_android.core.models.Configs;
import android.mvvm.mg.com.mvvm_android.core.repository.repositoryManager.preference.MVVMPrefUtils;

import com.dm.dmnetworking.parser.DMJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public final class Preference implements IPreferenceHelper {

    private static Preference instance;

    public static Preference getInstance() {
        if (instance == null) {
            synchronized (Preference.class) {
                if (instance == null) {
                    instance = new Preference();
                }
            }
        }
        return instance;
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
    public void saveProfilePhoto(final String path) {
        MVVMPrefUtils.saveProfilePhoto(path);
    }

    @Override
    public String getProfilePhoto() {
        return MVVMPrefUtils.getProfilePhoto();
    }

    @Override
    public void languageCode(final String code) {
        MVVMPrefUtils.setLanguageCode(code);
    }

    @Override
    public String getLanguageCode() {
        return MVVMPrefUtils.getLanguageCode();
    }
}
