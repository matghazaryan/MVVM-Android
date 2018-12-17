package biometric.dm.com.dmbiometric.listeners;

public interface IBIODecryptListener {

    void onSuccess(String decryptedText);

    void onFailed();
}
