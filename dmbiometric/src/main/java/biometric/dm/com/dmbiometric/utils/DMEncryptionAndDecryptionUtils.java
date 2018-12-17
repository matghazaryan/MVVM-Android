package biometric.dm.com.dmbiometric.utils;

import android.util.Base64;

import javax.crypto.Cipher;

import biometric.dm.com.dmbiometric.listeners.IBIODecryptListener;
import biometric.dm.com.dmbiometric.listeners.IBIOEncryptListener;

public final class DMEncryptionAndDecryptionUtils {

    public static void encryptString(final Cipher cipher, final String json, final IBIOEncryptListener listener) {
        try {
            final byte[] bytes = cipher.doFinal(json.getBytes());
            final String encryptedText = Base64.encodeToString(bytes, Base64.NO_WRAP);
            listener.onSuccess(encryptedText);
        } catch (final Exception e) {
            e.printStackTrace();
            listener.onFailed();
        }
    }

    public static void decryptString(final Cipher cipher, final String textForDecrypt, final IBIODecryptListener listener) {
        try {
            final byte[] bytes = Base64.decode(textForDecrypt, Base64.NO_WRAP);
            final String decryptedText = new String(cipher.doFinal(bytes));
            listener.onSuccess(decryptedText);
        } catch (final Exception e) {
            e.printStackTrace();
            listener.onFailed();
        }
    }
}
