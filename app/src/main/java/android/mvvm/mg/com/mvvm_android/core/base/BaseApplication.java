package android.mvvm.mg.com.mvvm_android.core.base;

import android.app.Application;
import dmutils.com.dmutils.general.DMMemory;
import dmutils.com.dmutils.pref.DMPrefsCacheManager;

/**
 * BaseApplication abstract class for initialize Preference cache manager, free memory when memory is low and
 * get application configs
 */

public abstract class BaseApplication extends Application {

    /**
     * Get application configs for use in the fragments, viewModels and activities
     *
     * @return Object of class which extends BaseApplicationConfigs abstract class
     */
    public abstract BaseApplicationConfigs getApplicationConfigs();

    @Override
    public void onCreate() {
        super.onCreate();

        initialize();
    }

    @Override
    public void onLowMemory() {
        DMMemory.freeMemory();
        super.onLowMemory();
    }

    private void initialize() {
        DMPrefsCacheManager.getInstance().initialize(getApplicationContext());
    }
}
