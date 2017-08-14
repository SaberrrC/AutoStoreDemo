package com.shanlin.android.autostore.common.net;

import android.content.Context;

import com.shanlin.autostore.constants.Constant;
import com.shanlin.android.autostore.common.utils.SpUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cuieney on 14/08/2017.
 */

public class HeadInterceptor implements Interceptor {
    private Context context;

    public HeadInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("token", SpUtils.getString(context, Constant.TOKEN, ""))
                .build();
        return chain.proceed(request);
    }
}
