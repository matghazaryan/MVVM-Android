package biometric.dm.com.dmbiometric.utils;

import com.google.gson.Gson;

public final class DMJsonUtils {

    public static <T> String objectToJson(final T t) {

        if (t == null) {
            try {
                throw new Exception("Object for encryption can't be null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Gson().toJson(t);
    }

    public static <T> T jsonToObject(final String json, final Class<T> aClass) {
        if (json == null) {
            try {
                throw new Exception("Object for decryption can't be null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Gson().fromJson(json, aClass);
    }
}
