package com.example.shanlin.facedemo.net;

import org.xutils.common.Callback;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import java.io.File;


public class Connector {

    private static Connector connector = null;
    private HttpConnector httpConn = new HttpConnectorImpl();

    private Connector() {
    }

    public static Connector getInstance() {
        if (connector == null)
            connector = new Connector();
        return connector;
    }

    public void doHttpPost(String url ,String params) {

    }

    public void doHttpPost(RequestParams params, CommonCallback<?> callBack) {
        httpConn.post(params, callBack);
    }

    public void doHttpGet(RequestParams params, CommonCallback<?> callBack) {
        httpConn.get(params, callBack);
    }

    public void doDownLoad(RequestParams params, File savePath, boolean autoResume, CommonCallback<?> callback) {
        httpConn.download(params, savePath, autoResume, callback);
    }

    public void doUpload(RequestParams params, File diskFile, CommonCallback<?> callback) {
        httpConn.upload(params, diskFile, callback);
    }

    public void doHttpGet(RequestParams params) {
        httpConn.get(params);
    }

    /**
     * <pre>方法返回Callback.Cancelable cancelable  </pre>
     * <pre>用法：</pre>
     * <pre>if(!cancelable.isCancelled())  </pre>
     * <pre>  cancelable.cancel();</pre>
     *
     * @param params
     * @param callBack
     * @return Callback.Cancelable cancelable
     */

    public Callback.Cancelable doHttpsPostCancelable(RequestParams params, CommonCallback<?> callBack) {
        return httpConn.postCancelable(params, callBack);

    }

    /**
     * <pre>方法返回Callback.Cancelable cancelable  </pre>
     * <pre>用法：</pre>
     * <pre>if(!cancelable.isCancelled())  </pre>
     * <pre>  cancelable.cancel();</pre>
     *
     * @param params
     * @param callBack
     * @return Callback.Cancelable cancelable
     */

    public Callback.Cancelable doHttpGetCancelable(RequestParams params, CommonCallback<?> callBack) {
        return httpConn.getCancelable(params, callBack);
    }
}
