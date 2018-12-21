package android.mvvm.mg.com.mvvm_android.core.base;

import android.content.Context;

public abstract class BaseApplicationConfigs implements IBaseConstants {

    public abstract void showErrorDialog(final Context context, final String message);

    public abstract void showNoInternetDialog(final Context context);

    public abstract int getGeneralErrorMessage();

    public abstract int getNoInternetMessage();

    public abstract String getTag();

}
