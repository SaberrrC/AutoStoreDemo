package com.shanlin.autostore.net;


import android.content.Intent;
import android.text.TextUtils;

import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.activity.LoginActivity;
import com.shanlin.autostore.base.BaseBean;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.LogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by shanlin on 2017-7-21.
 */

public abstract class CustomCallBack<T extends BaseBean> implements Callback<T> {

    public static final String ERR_NETWORK_MSG  = "网络异常,请重试!";
    public static       String ERR_NETWORK_CODE = "0";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!response.isSuccessful()) {
            error(null, String.valueOf(response.code()), response.message());
            return;
        }
        T baseBean = response.body();
        String code = baseBean.getCode();
        String msg = baseBean.getMessage();
        if (TextUtils.equals(code, "401")) {//token失效
            AutoStoreApplication.isLogin = false;
            if (!jumpLogin) {
                return;
            }
            toLoginActivity();
            return;
        }
        if (!TextUtils.equals(code, "200")) {
            error(null, code, msg);
            return;
        }
        success(code, baseBean, msg);
    }

    private boolean jumpLogin = true;

    public void setJumpLogin(boolean isjumpLogin) {
        jumpLogin = isjumpLogin;
    }


    @Override
    public void onFailure(Call<T> call, Throwable ex) {
        ex.printStackTrace();
        if (ex instanceof HttpException) { // 网络错误
            HttpException httpEx = (HttpException) ex;
            ERR_NETWORK_CODE = httpEx.code() + "";
        }
        if (ex instanceof HttpException) {
            HttpException httpEx = (HttpException) ex;
            ERR_NETWORK_CODE = httpEx.code() + "";
        }
        if (!CommonUtils.checkNet()) {
            error(ex, ERR_NETWORK_CODE, "无网络");
            return;
        }
        LogUtils.d(ERR_NETWORK_MSG);
        error(ex, ERR_NETWORK_CODE, ERR_NETWORK_MSG);
    }

    public abstract void success(String code, T data, String msg);

    public abstract void error(Throwable ex, String code, String msg);

    private void toLoginActivity() {
        Intent toLoginActivity = new Intent(AutoStoreApplication.getApp(), LoginActivity.class);
        if (TextUtils.isEmpty(key) && TextUtils.isEmpty(value)) {
            toLoginActivity.putExtra(key, value);
        }
        toLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AutoStoreApplication.getApp().startActivity(toLoginActivity);
    }

    private String key;
    private String value;

    public void setKeyAndValue(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
