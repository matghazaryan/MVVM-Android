package android.mvvm.mg.com.mvvm_android.core.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.mvvm.mg.com.mvvm_android.BuildConfig;
import android.mvvm.mg.com.mvvm_android.core.repository.DataRepository;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import dmutils.com.dmutils.language.DMLanguageContextWrapper;

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
            Log.d(applicationConfigs.getTag(), "-----------------------------------------------------------------------------------------------------> Page is " + this.getClass().getSimpleName());
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

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(DMLanguageContextWrapper.wrap(newBase, DataRepository.preference().getLanguageCode()));
    }
}
