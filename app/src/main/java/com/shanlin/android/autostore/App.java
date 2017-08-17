package com.shanlin.android.autostore;

import android.app.Application;

import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.LogUtil;
import com.shanlin.android.autostore.di.component.AppComponent;
import com.shanlin.android.autostore.di.component.DaggerAppComponent;
import com.shanlin.android.autostore.di.module.AppModule;
import com.shanlin.android.autostore.di.module.RetrofitModule;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by cuieney on 14/08/2017.
 */

public class App extends Application {
    private static App app;
    public static boolean isLogin = false;
    public static boolean FACE = false;

    private AppComponent appComponent;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initAppComponent();
        initSDK();
    }

    private void initSDK() {
        com.zhy.autolayout.config.AutoLayoutConifg.getInstance().useDeviceSize();
        //保存闸机的DevicedID
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        CommonUtils.netWorkWarranty();
//        CrashHandler.getInstance().setCustomCrashHanler(app);

        new LogUtil.Builder()
                .setLogSwitch(true)// 设置log总开关，默认开
                .setGlobalTag("oye")// 设置log全局标签，默认为空
                .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
                .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
                .setLogFilter(LogUtil.V);// log过滤器，和logcat过滤器同理，默认Verbose

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .retrofitModule(new RetrofitModule(this))
                .build();
    }
}
