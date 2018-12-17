package biometric.dm.com.dmbiometric.prepare;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import biometric.dm.com.dmbiometric.constants.IBIOConstants;
import biometric.dm.com.dmbiometric.listeners.IBIOPrepareListener;
import biometric.dm.com.dmbiometric.utils.DMBasePrefUtils;


@RequiresApi(api = Build.VERSION_CODES.M)
public abstract class BIOPrepareBiometric extends BIOBaseBiometric {

    private KeyStore keyStore;

    private void generateKey(final IBIOPrepareListener listener) {
        try {

            keyStore = KeyStore.getInstance(IBIOConstants.DefaultValue.ANDROID_KEY_STORE);
            keyStore.load(null);

            if (!keyStore.containsAlias(IBIOConstants.DefaultValue.KEY_ALIAS)) {
                final KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, IBIOConstants.DefaultValue.ANDROID_KEY_STORE);
                keyGenerator.init(new
                        KeyGenParameterSpec.Builder(IBIOConstants.DefaultValue.KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());

                keyGenerator.generateKey();
            }
            listener.onSuccess();
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            listener.onFailed();
            exc.printStackTrace();
        }
    }

    private void initCipher(final DMBiometricConfigs configs, final IBIOPrepareListener listener) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            e.printStackTrace();
            listener.onFailed();
            return;
        }

        try {
            keyStore.load(null);
            final SecretKey key = (SecretKey) keyStore.getKey(IBIOConstants.DefaultValue.KEY_ALIAS, null);

            switch (configs.getEncryptionMode()) {
                case ENCRYPT:
                    cipher.init(Cipher.ENCRYPT_MODE, key);
                    DMBasePrefUtils.setIV(Base64.encodeToString(cipher.getIV(), Base64.NO_WRAP));
                    break;
                case DECRYPT:
                    final byte[] iv = Base64.decode(DMBasePrefUtils.getIV(), Base64.NO_WRAP);
                    final IvParameterSpec ivSpec = new IvParameterSpec(iv);

                    try {
                        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                        listener.onFailed();
                        return;
                    }
                    break;
            }

            configs.setCipher(cipher);

            listener.onSuccess();

        } catch (KeyStoreException
                | CertificateException
                | UnrecoverableKeyException
                | IOException
                | NoSuchAlgorithmException
                | InvalidKeyException e) {
            e.printStackTrace();
            listener.onFailed();
        }
    }

    protected void prepare(final DMBiometricConfigs configs, final IBIOPrepareListener listener) {
        generateKey(new IBIOPrepareListener() {
            @Override
            public void onSuccess() {
                initCipher(configs, listener);
            }

            @Override
            public void onFailed() {
                listener.onFailed();
            }
        });
    }
}
