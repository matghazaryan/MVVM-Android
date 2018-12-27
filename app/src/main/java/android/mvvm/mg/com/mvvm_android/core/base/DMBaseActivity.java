package android.mvvm.mg.com.mvvm_android.core.base;

import android.annotation.SuppressLint;
import android.mvvm.mg.com.mvvm_android.BuildConfig;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * BaseActivity has few functions for work with action bar, set title , show/hide and display current class name
 */
@SuppressLint("Registered")
public abstract class DMBaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        final DMBaseApplicationConfigs applicationConfigs = ((DMBaseApplication) getApplication()).getApplicationConfigs();

        if (BuildConfig.DEBUG) {
            Log.d(applicationConfigs.getTag(), "-----------------------------------------------------------------------------------------------------> " + this.getClass().getSimpleName());
        }
    }

    protected void hideActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void setTitle(final String title) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void setTitle(final int title) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void showBackButton() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void hideBackButton() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }
}
