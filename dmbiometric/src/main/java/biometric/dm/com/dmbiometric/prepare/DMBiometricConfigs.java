package biometric.dm.com.dmbiometric.prepare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;

import javax.crypto.Cipher;

import biometric.dm.com.dmbiometric.R;
import biometric.dm.com.dmbiometric.constants.IBIOConstants;
import biometric.dm.com.dmbiometric.listeners.IBIOUpdateStatusListener;
import biometric.dm.com.dmbiometric.listeners.IDMBiometricListener;


public final class DMBiometricConfigs<T> {

    private Context context;

    private String title;
    private String subtitle;
    private String description;
    private String negativeButtonText;

    private IBIOConstants.EncryptionMode encryptionMode;

    private Cipher cipher;

    private IDMBiometricListener<T> biometricListener;

    private View dialogViewV23;

    private IBIOUpdateStatusListener updateStatusV23Listener;

    private int themeDialogV23 = R.style.BottomSheetDialogTheme;

    private T objectForEncrypt;

    private Class<T> tClass;

    public DMBiometricConfigs(final Context context) {
        this.context = context;
    }

    public DMBiometricConfigs<T> setTitle(@NonNull final String title) {
        this.title = title;
        return this;
    }

    public DMBiometricConfigs<T> setSubtitle(@NonNull final String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public DMBiometricConfigs<T> setDescription(@NonNull final String description) {
        this.description = description;
        return this;
    }

    public DMBiometricConfigs<T> setNegativeButtonText(@NonNull final String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
        return this;
    }

    public DMBiometricConfigs<T> setEncrypt(final IBIOConstants.EncryptionMode encryptionMode) {
        this.encryptionMode = encryptionMode;
        return this;
    }

    void setCipher(final Cipher cipher) {
        this.cipher = cipher;
    }

    public DMBiometricConfigs<T> setBiometricListener(final IDMBiometricListener<T> biometricListener) {
        this.biometricListener = biometricListener;
        return this;
    }

    public DMBiometricConfigs<T> setDialogViewV23(final View dialogViewV23) {
        this.dialogViewV23 = dialogViewV23;
        return this;
    }

    public DMBiometricConfigs<T> setUpdateStatusV23Listener(final IBIOUpdateStatusListener updateStatusV23Listener) {
        this.updateStatusV23Listener = updateStatusV23Listener;
        return this;
    }

    public DMBiometricConfigs<T> setThemeDialogV23(@StyleRes int themeDialogV23) {
        this.themeDialogV23 = themeDialogV23;
        return this;
    }

    public DMBiometricConfigs<T> setObjectForEncrypt(final T objectForEncrypt) {
        this.objectForEncrypt = objectForEncrypt;
        return this;
    }

    public DMBiometricConfigs<T> setClass(final Class<T> tClass) {
        this.tClass = tClass;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getNegativeButtonText() {
        return negativeButtonText;
    }

    public IBIOConstants.EncryptionMode getEncryptionMode() {
        return encryptionMode;
    }

    Cipher getCipher() {
        return cipher;
    }

    public Context getContext() {
        return context;
    }

    public IDMBiometricListener<T> getBiometricListener() {
        return biometricListener;
    }

    public View getDialogViewV23() {
        return dialogViewV23;
    }

    public IBIOUpdateStatusListener getUpdateStatusV23Listener() {
        return updateStatusV23Listener;
    }

    public int getThemeDialogV23() {
        return themeDialogV23;
    }

    public T getObjectForEncrypt() {
        return objectForEncrypt;
    }

    public Class<T> getTClass() {
        return tClass;
    }
}
