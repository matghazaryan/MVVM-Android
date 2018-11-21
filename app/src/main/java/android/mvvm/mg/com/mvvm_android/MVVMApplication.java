package android.mvvm.mg.com.mvvm_android;

import android.app.Application;
import dmutils.com.dmutils.general.DMMemory;
import dmutils.com.dmutils.pref.DMPrefsCacheManager;


public class MVVMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    @Override
    public void onLowMemory() {
        DMMemory.freeMemory();
        super.onLowMemory();
    }

    private void init() {
        DMPrefsCacheManager.getInstance().initialize(getApplicationContext());
    }
}
