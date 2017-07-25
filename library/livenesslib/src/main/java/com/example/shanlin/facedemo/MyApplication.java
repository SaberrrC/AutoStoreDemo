package com.example.shanlin.facedemo;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.slfinance.facesdk.service.Manager;
import com.slfinance.facesdk.util.ConUtil;

import java.util.Map;

/**
 * Created by shanlin on 2017-7-16.
 */

public class MyApplication extends Application {

    public static Context app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //初始化注册管理器
            Manager manager = new Manager(this);
            requestLicense();
        }

    }


    private void requestLicense() {
        final Manager manager = new Manager(this);
        //如果要使用身份证OCR功能，请生成身份证功能管理器
        IDCardQualityLicenseManager idCardManager = new IDCardQualityLicenseManager(this);
        //将身份证功能管理器，注入到注册管理器中
        boolean idCardIsRegisted = manager.registerLicenseManager(idCardManager);

        //如果要使用活体检测抓人脸，请生成活体检测功能管理器
        LivenessLicenseManager livenessManager = new LivenessLicenseManager(this);
        //将活体检测功能管理器，注入到注册管理器中
        boolean livenessIsRegisted = manager.registerLicenseManager(livenessManager);

        //可以选择给自己设备打标，以方便对应设备监控
        String uuid = ConUtil.getUUIDString(this);

        //异步进行网络授权请求
        final String finalUuid = uuid;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //得到各种功能所对应的联网授权结果
                Map<String, Long> registResult = manager.takeLicenseFromNetwork(finalUuid);
                handler.sendMessage(handler.obtainMessage(1, registResult));
            }
        }).start();
    }

    //监听授权结果
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            Map<String, Long> registResult = (Map<String, Long>) msg.obj;
            //当isIDCardLicenseGet 为true，表示身份证OCR功能授权成功
            // isIDCardLicenseGet = registResult.get(idCardManager.getVersion()) > 0;
            // 当isLivenessLicenseGet 为true，表示活体检测功能授权成功
            // isLivenessLicenseGet = registResult.get(livenessManager.getVersion()) > 0;
            return true;
        }
    });
}
