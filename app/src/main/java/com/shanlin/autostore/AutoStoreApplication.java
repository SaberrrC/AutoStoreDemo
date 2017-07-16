package com.shanlin.autostore;

import android.app.Application;

import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by DELL on 2017/7/16 0016.
 */

public class AutoStoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
    }
}
