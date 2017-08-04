package com.shanlin.autostore;

import android.app.Application;

import com.shanlin.autostore.crash.CrashHandler;
import com.shanlin.autostore.utils.CommonUtils;
import com.zhy.autolayout.config.AutoLayoutConifg;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by DELL on 2017/7/16 0016.
 */

public class AutoStoreApplication extends Application {

    private static AutoStoreApplication app     = null;
    public static  boolean              isLogin = false;
    public static  boolean              FACE    = false;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        AutoLayoutConifg.getInstance().useDeviceSize();
        //保存闸机的DevicedID
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        CommonUtils.netWorkWarranty();
                CrashHandler.getInstance().setCustomCrashHanler(app);
    }

    public static AutoStoreApplication getApp() {
        return app;
    }

}
