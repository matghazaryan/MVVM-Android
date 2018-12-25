package android.mvvm.mg.com.mvvm_android.core.base;

import android.app.Application;
import dmutils.com.dmutils.general.DMUtilMemory;
import dmutils.com.dmutils.pref.DMUtilPrefsCacheManager;

/**
 * BaseApplication abstract class for initialize Preference cache manager, free memory when memory is low and
 * get application configs
 */

public abstract class DMBaseApplication extends Application {

    /**
     * Get application configs for use in the fragments, viewModels and activities
     *
     * @return Object of class which extends BaseApplicationConfigs abstract class
     */
    public abstract DMBaseApplicationConfigs getApplicationConfigs();

    @Override
    public void onCreate() {
        super.onCreate();

        initialize();
    }

    @Override
    public void onLowMemory() {
        DMUtilMemory.freeMemory();
        super.onLowMemory();
    }

    private void initialize() {
        DMUtilPrefsCacheManager.getInstance().initialize(getApplicationContext());
    }
}
