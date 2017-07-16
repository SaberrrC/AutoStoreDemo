package com.shanlin.autostore.base;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JR on 2017/7/15.
 */

/**
 * jason对象回调
 * @param <T>
 */
public abstract class ObjCallBack<T> implements Callback<ObjBaseBean> {

    private static final String TAG = "wr";

    @Override
    public void onResponse(Call<ObjBaseBean> call, Response<ObjBaseBean> response) {
        ObjBaseBean body = response.body();
        String message = response.message();
        if (body == null){
            Log.d("wr","数据异常");
            return;
        }
        T t = (T) body.getData();
//        Log.d(TAG, "onResponse:="+t);
        onTransData(t,message);
    }

    @Override
    public void onFailure(Call<ObjBaseBean> call, Throwable t) {
        String message = t.getMessage();
        Log.d(TAG, "onFailure: "+message);
        onFailureData(message);
    }

    public abstract void onTransData(T data, String message);
    public abstract void onFailureData(String message);
}
