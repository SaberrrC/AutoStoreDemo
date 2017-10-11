package com.shanlin.android.autostore.di.module;

import android.app.Activity;

import com.shanlin.android.autostore.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by cuieney on 14/08/2017.
 */

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }


}
