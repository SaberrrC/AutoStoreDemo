package com.shanlin.autostore.net;

import android.content.Intent;

import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.activity.GateActivity;
import com.shanlin.autostore.bean.CaptureResponse;
import com.shanlin.autostore.bean.CodeResponse;
import com.shanlin.autostore.bean.NumberLoginResponse;
import com.shanlin.autostore.utils.ToastUtils;

/**
 * Created by shanlin on 2017-7-21.
 */

public class NetCallBack {

    private static NetCallBack sNetCallBack = new NetCallBack();

    private NetCallBack() {
    }

    public static NetCallBack getInstance() {
        return sNetCallBack;
    }

    /**
     * 手机验证码登录
     */
    public NumberLoginResponseCustomCallBack getNumberLoginResponseCustomCallBack() {
        return new NumberLoginResponseCustomCallBack();
    }

    public class NumberLoginResponseCustomCallBack extends CustomCallBack<NumberLoginResponse> {
        @Override
        public void success(String code, NumberLoginResponse data, String msg) {
            ToastUtils.showToast(data.getMessage());
        }

        @Override
        public void error(Throwable ex, String code, String msg) {
            ToastUtils.showToast(msg);
        }
    }

    /**
     * 设备管理--设备控制（二维码扫描开闸机）
     */
    public CaptureResponseCustomCallBack getCaptureResponseCustomCallBack() {
        return new CaptureResponseCustomCallBack();
    }

    public class CaptureResponseCustomCallBack extends CustomCallBack<CaptureResponse> {
        @Override
        public void success(String code, CaptureResponse data, String msg) {
            Intent intent = new Intent(AutoStoreApplication.getApp(), GateActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AutoStoreApplication.getApp().startActivity(intent);
            // TODO: 2017-7-21 判断用户是否录入人脸



        }

        @Override
        public void error(Throwable ex, String code, String msg) {
            ToastUtils.showToast("连接失败");
        }
    }

    /**
     * 获取短信验证码
     */
    public CodeResponseCustomCallBack getCodeResponseCustomCallBack() {
        return new CodeResponseCustomCallBack();
    }

    public class CodeResponseCustomCallBack extends CustomCallBack<CodeResponse> {

        @Override
        public void success(String code, CodeResponse data, String msg) {
            ToastUtils.showToast(msg);
        }

        @Override
        public void error(Throwable ex, String code, String msg) {
            ToastUtils.showToast("网络异常，请稍后再试");
        }
    }

}
