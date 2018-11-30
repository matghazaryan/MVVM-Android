package android.mvvm.mg.com.mvvm_android.repository.preference;

import android.mvvm.mg.com.mvvm_android.models.Configs;
import org.json.JSONException;

public interface IPreferenceHelper {

    void prefSaveConfigs(final String json);

    Configs prefGetConfigs() throws JSONException;

    void prefSaveToken(final String token);

    String prefGetToken();

    boolean prefIsCheckedRemember();

    void prefSetRemember(final boolean isChecked);


    void prefSaveProfilePhoto(final String path);

    String prefGetProfilePhoto();
}
