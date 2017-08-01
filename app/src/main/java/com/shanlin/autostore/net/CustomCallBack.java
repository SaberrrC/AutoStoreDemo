package com.shanlin.autostore.net;


import android.text.TextUtils;

import com.shanlin.autostore.base.BaseBean;
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
        if (!TextUtils.equals(code, "401")) {//token失效

            return;
        }
        if (!TextUtils.equals(code, "200")) {
            error(null, code, msg);
            return;
        }
        success(code, baseBean, msg);
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
        LogUtils.d(ERR_NETWORK_MSG);
        error(ex, ERR_NETWORK_CODE, ERR_NETWORK_MSG);
    }

    public abstract void success(String code, T data, String msg);

    public abstract void error(Throwable ex, String code, String msg);


}
