package android.mvvm.mg.com.mvvm_android.repository.preference;

import android.mvvm.mg.com.mvvm_android.models.Configs;

import org.json.JSONException;

public interface IPreferenceHelper {

    void saveConfigs(final String json);

    Configs getConfigs() throws JSONException;

    void saveToken(final String token);

    String getToken();

    boolean isCheckedRemember();

    void setRemember(final boolean isChecked);

    void saveProfilePhoto(final String path);

    String getProfilePhoto();

    void languageCode(final String code);

    String getLanguageCode();
}
