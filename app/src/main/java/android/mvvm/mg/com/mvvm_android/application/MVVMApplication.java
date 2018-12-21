package android.mvvm.mg.com.mvvm_android.application;

import android.mvvm.mg.com.mvvm_android.core.base.BaseApplication;
import android.mvvm.mg.com.mvvm_android.core.base.BaseApplicationConfigs;


public class MVVMApplication extends BaseApplication {

    @Override
    public BaseApplicationConfigs getApplicationConfigs() {
        return new MVVMApplicationConfigs();
    }
}
