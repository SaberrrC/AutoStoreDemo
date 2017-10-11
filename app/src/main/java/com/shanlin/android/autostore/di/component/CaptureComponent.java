package com.shanlin.android.autostore.di.component;

import com.shanlin.android.autostore.di.PerActivity;
import com.shanlin.android.autostore.di.module.CaptureModule;
import com.shanlin.android.autostore.ui.act.CaptureActivity;

import dagger.Component;

/**
 * Created by cuieney on 16/08/2017.
 */

@PerActivity
@Component(dependencies = AppComponent.class,modules = {CaptureModule.class})
public interface CaptureComponent {
    void inject(CaptureActivity activity);
}
