package com.shanlin.autostore;

import android.app.Application;

import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.utils.LogUtils;
import com.shanlin.autostore.utils.SpUtils;
import com.zhy.autolayout.config.AutoLayoutConifg;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by DELL on 2017/7/16 0016.
 */

public class AutoStoreApplication extends Application {

    private static AutoStoreApplication app     = null;
    public static  boolean              isLogin = false;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        AutoLayoutConifg.getInstance().useDeviceSize();
        //保存闸机的DevicedID
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        if (!rid.isEmpty()) {
            SpUtils.saveString(this, Constant.DEVICEID, rid);
        }

        LogUtils.d("devicedid  " + rid);
        //        CrashHandler.getInstance().setCustomCrashHanler(app);
    }

    public static AutoStoreApplication getApp() {
        return app;
    }

}
