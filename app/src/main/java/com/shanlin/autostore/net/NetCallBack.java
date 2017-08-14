package com.shanlin.autostore.net;

import com.shanlin.autostore.bean.LoginBean;
import com.shanlin.autostore.bean.resultBean.CodeBean;
import com.shanlin.autostore.bean.resultBean.NumberLoginRsponseBean;
import com.shanlin.android.autostore.common.utils.ToastUtils;

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

    public class FaceLoginCallBack extends CustomCallBack<LoginBean> {

        @Override
        public void success(String code, LoginBean data, String msg) {
            ToastUtils.showToast(msg);
        }

        @Override
        public void error(Throwable ex, String code, String msg) {
            ToastUtils.showToast("网络异常，请稍后再试");
        }
    }


}
