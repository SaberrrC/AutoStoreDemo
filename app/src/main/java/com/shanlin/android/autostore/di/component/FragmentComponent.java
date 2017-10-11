package com.shanlin.android.autostore.di.component;

import android.app.Activity;

import com.shanlin.android.autostore.di.PerFragment;
import com.shanlin.android.autostore.di.module.FragmentModule;

import dagger.Component;

/**
 * Created by cuieney on 16/8/7.
 */

@PerFragment
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    Activity getActivity();
}
