package android.mvvm.mg.com.mvvm_android.application;

import android.mvvm.mg.com.mvvm_android.core.base.DMBaseApplication;
import android.mvvm.mg.com.mvvm_android.core.base.DMBaseApplicationConfigs;


public class MVVMApplication extends DMBaseApplication {

    @Override
    public DMBaseApplicationConfigs getApplicationConfigs() {
        return MVVMApplicationConfigs.getInstance();
    }
}
