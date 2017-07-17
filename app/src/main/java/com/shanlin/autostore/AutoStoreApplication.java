package com.shanlin.autostore;

import android.app.Application;

import com.shanlin.autostore.crash.CrashHandler;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by DELL on 2017/7/16 0016.
 */

public class AutoStoreApplication extends Application {
    private static AutoStoreApplication app = null;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        AutoLayoutConifg.getInstance().useDeviceSize();
        CrashHandler.getInstance().setCustomCrashHanler(app);
    }

    public static AutoStoreApplication getApp() {
        return app;
    }

}
