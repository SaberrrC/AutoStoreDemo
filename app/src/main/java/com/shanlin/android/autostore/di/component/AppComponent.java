package com.shanlin.android.autostore.di.component;

import android.content.Context;

import com.shanlin.android.autostore.App;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.di.module.AppModule;
import com.shanlin.android.autostore.di.module.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by cuieney on 14/08/2017.
 */

@Singleton
@Component(modules = {AppModule.class, RetrofitModule.class})
public interface AppComponent {
    Context getContext();
    Api getApi();
}
