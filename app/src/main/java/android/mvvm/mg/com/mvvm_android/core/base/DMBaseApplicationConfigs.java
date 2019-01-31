package android.mvvm.mg.com.mvvm_android.core.base;

import android.content.Context;


/**
 * BaseApplicationConfigs us in the application , for get and use this functions in fragments, viewModels and activities
 */
public abstract class DMBaseApplicationConfigs implements DMBaseIApplicationMethods {

    /**
     * Show error dialog for current project
     *
     * @param context fragment or activity context
     * @param message error message for dialog
     */
    public abstract void showErrorDialog(final Context context, final String message);

    /**
     * Sho no internet dialog for current project
     *
     * @param context fragment or activity context
     */
    public abstract void showNoInternetDialog(final Context context);

    /**
     * General error message , which show when can not find another error message for current error case , then show general message
     *
     * @return general error message resource id
     */
    public abstract int getGeneralErrorMessage();

    /**
     * No internet error message
     *
     * @return no internet error message resource id
     */
    public abstract int getNoInternetMessage();

    /**
     * Tag for show information in logs
     *
     * @return Tag name
     */
    public abstract String getTag();
}
