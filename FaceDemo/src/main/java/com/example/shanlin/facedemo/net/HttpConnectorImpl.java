package com.example.shanlin.facedemo.net;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.shanlin.facedemo.LogUtils;

import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

//import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

@SuppressLint("TrulyRandom")
public class HttpConnectorImpl implements HttpConnector {
    private static final String TAG = "HttpConnectorImpl";

    @Override
    public void download(RequestParams params, File savePath, boolean autoResume, CommonCallback<?> callback) {
        if (!savePath.isDirectory())
            return;
        params.setAutoResume(autoResume);
        params.setAutoRename(false);
        params.setSaveFilePath(savePath.getAbsolutePath());
        x.http().post(params, callback);
    }

    @Override
    public void upload(RequestParams params, File diskFile, CommonCallback<?> callback) {
        params.setMultipart(true);
        params.addBodyParameter("file", diskFile, null); // 如果文件没有扩展名,
        x.http().post(params, callback);
    }

    @Override
    public void post(RequestParams params, CommonCallback<?> callBack) {
        LogUtils.e("*****POST打印请求参数开始********");
        LogUtils.e("请求地址::::" + params.getUri());
        LogUtils.e("请求头信息::::" + params.getHeaders().toString());
        List<KeyValue> keyVals = params.getBodyParams();
        LogUtils.e(params.getBodyContent());
        for (KeyValue keyValue : keyVals) {
            LogUtils.e("请求内容::::" + "key:  " + keyValue.key + "   val:  " + keyValue.value);
        }
        Log.e(TAG, "*****POST打印请求参数结束********");
        x.http().post(params, callBack);
    }

    @Override
    public void get(RequestParams params, CommonCallback<?> callBack) {
        LogUtils.e(params != null ? params.toString() : "requestParams is null");
        x.http().get(params, callBack);

    }

    @Override
    public void get(RequestParams params) {
        LogUtils.e(params != null ? params.toString() : "requestParams is null");
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String arg0) {
            }
        });
    }

    @Override
    public Cancelable getCancelable(RequestParams params, CommonCallback<?> callBack) {
        LogUtils.e(params != null ? params.toString() : "requestParams is null");
        return x.http().get(params, callBack);
    }


    @Override
    public Cancelable postCancelable(RequestParams params, CommonCallback<?> callBack) {
        LogUtils.e("*****POST打印请求参数开始********");
        LogUtils.e("请求地址::::" + params.getUri());
        LogUtils.e("请求头信息::::" + params.getHeaders().toString());
        List<KeyValue> keyVals = params.getBodyParams();
        for (KeyValue keyValue : keyVals) {
            LogUtils.e("请求内容::::" + "key:  " + keyValue.key + "   val:  " + keyValue.value);
        }

        Log.e(TAG, "*****POST打印请求参数结束********");
        return x.http().post(params, callBack);
    }
}
