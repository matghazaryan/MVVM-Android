package biometric.dm.com.dmbiometric.listeners;

public interface IBIOEncryptListener {

    void onSuccess(String encryptedText);

    void onFailed();
}
