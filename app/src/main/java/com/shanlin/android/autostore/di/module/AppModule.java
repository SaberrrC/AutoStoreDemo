package com.shanlin.android.autostore.di.module;

import android.content.Context;

import com.shanlin.android.autostore.App;

import dagger.Module;
import dagger.Provides;

/**
 * Created by cuieney on 14/08/2017.
 */

@Module
public class AppModule {
    final App mApp;

    public AppModule(App mApp) {
        this.mApp = mApp;
    }

    @Provides
    public Context providesContext(){
        return mApp;
    }


}
