package com.shanlin.android.autostore.di.module;


import android.app.Activity;
import android.support.v4.app.Fragment;

import com.shanlin.android.autostore.di.PerFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {


    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @PerFragment
    public Activity provideActivity() {
        return fragment.getActivity();
    }
}
