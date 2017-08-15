package com.shanlin.android.autostore.di.component;

import android.app.Activity;

import com.shanlin.android.autostore.di.PerActivity;
import com.shanlin.android.autostore.di.module.ActivityModule;
import com.shanlin.android.autostore.ui.act.MainActivity;
import com.shanlin.android.autostore.ui.act.SplashActivity;
import com.shanlin.autostore.activity.LoginActivity;

import dagger.Component;

/**
 * Created by cuieney on 14/08/2017.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity getActivity();

    void inject(SplashActivity activity);
    void inject(MainActivity activity);

    void inject(LoginActivity loginActivity);
}
