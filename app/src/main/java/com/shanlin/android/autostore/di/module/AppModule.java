package com.shanlin.android.autostore.di.module;

import android.content.Context;

import com.shanlin.android.autostore.App;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.autostore.constants.Constant;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
