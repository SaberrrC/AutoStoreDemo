package com.shanlin.autostore.net;

import android.content.Intent;

import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.activity.GateActivity;
import com.shanlin.autostore.bean.CaptureBean;
import com.shanlin.autostore.bean.CodeBean;
import com.shanlin.autostore.bean.FaceLoginBean;
import com.shanlin.autostore.bean.NumberLoginRsponseBean;
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
    public NumberLoginCallBack getNumberLoginCallBack() {
        return new NumberLoginCallBack();
    }

    public class NumberLoginCallBack extends CustomCallBack<NumberLoginRsponseBean> {
        @Override
        public void success(String code, NumberLoginRsponseBean data, String msg) {
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
    public CaptureCallBack getCaptureCallBack() {
        return new CaptureCallBack();
    }

    public class CaptureCallBack extends CustomCallBack<CaptureBean> {
        @Override
        public void success(String code, CaptureBean data, String msg) {
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
    public CodeCallBack getCodeCallBack() {
        return new CodeCallBack();
    }

    public class CodeCallBack extends CustomCallBack<CodeBean> {

        @Override
        public void success(String code, CodeBean data, String msg) {
            ToastUtils.showToast(msg);
        }

        @Override
        public void error(Throwable ex, String code, String msg) {
            ToastUtils.showToast("网络异常，请稍后再试");
        }
    }

    /**
     * 脸部识别登陆
     */
    public FaceLoginCallBack getFaceLoginCallBackCallBack() {
        return new FaceLoginCallBack();
    }

    public class FaceLoginCallBack extends CustomCallBack<FaceLoginBean> {

        @Override
        public void success(String code, FaceLoginBean data, String msg) {
            ToastUtils.showToast(msg);
        }

        @Override
        public void error(Throwable ex, String code, String msg) {
            ToastUtils.showToast("网络异常，请稍后再试");
        }
    }


}
