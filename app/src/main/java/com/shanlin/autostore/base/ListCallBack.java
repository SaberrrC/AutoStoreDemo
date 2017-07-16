package com.shanlin.autostore.base;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JR on 2017/7/15.
 */

public abstract class ListCallBack<T> implements Callback<ListBaseBean> {

    private static final String TAG = "wr";

    @Override
    public void onResponse(Call<ListBaseBean> call, Response<ListBaseBean> response) {
        ListBaseBean body = response.body();
        String message = response.message();
        if (body == null){
            Log.d("wr","数据异常");
            return;
        }

        List<T> data = body.getData();
        onTransData(data,message);
    }

    @Override
    public void onFailure(Call<ListBaseBean> call, Throwable t) {
        String message = t.getMessage();
        Log.d(TAG, "onFailure: ");
        onFailureData(message);
    }

    public abstract void onTransData (List<T> data,String msg);

    public abstract void onFailureData (String msg);

}
