package com.example.shanlin.facedemo.params;


import com.example.shanlin.facedemo.MyApplication;
import com.example.shanlin.facedemo.MyX509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;


public class BaseParams {
    public enum HttpType {//TODO edit header here ...
        Get {
            @Override
            public void initHeader() {
//                params.addHeader("platform", "android");
//                params.addHeader("Content-Type", "application/json");
                params.setConnectTimeout(CONNECT_TIMEOUT);
            }

        }, Post {
            @Override
            public void initHeader() {
//                params.addHeader("platform", "android");
                params.addHeader("Content-Type", "application/json");
                params.setConnectTimeout(CONNECT_TIMEOUT);

            }
        }, PostHttps {
            @Override
            public void initHeader() {
                SSLContext sslContext = getSSLContext();
                if (null == sslContext) {
                    return;
                }
                params.setSslSocketFactory(sslContext.getSocketFactory()); //绑定SSL证书(https请求)
                params.addHeader("Content-Type", "multipart/form-data");
                params.setConnectTimeout(CONNECT_TIMEOUT);
            }
        },UploadFile {
            @Override
            public void initHeader() {
                params.addHeader("platform", "android");
                params.addHeader("Content-Type", "application/x-www-form-urlencoded");
                params.setConnectTimeout(CONNECT_TIMEOUT);
            }
        }, Download {
            @Override
            public void initHeader() {
                params.addHeader("platform", "android");
                params.addHeader("Content-Type", "application/json");
                params.setConnectTimeout(CONNECT_TIMEOUT);
            }
        }, UpDateImage {
            @Override
            public void initHeader() {
                params.addHeader("platform", "android");
                params.addHeader("Content-Disposition", "attachment");
                params.setConnectTimeout(CONNECT_TIMEOUT);
                params.setMultipart(true);
            }
        };

        public abstract void initHeader();
    }

    private static RequestParams params;

    private static final int CONNECT_TIMEOUT = 10 * 1000;

    public BaseParams(String uri, HttpType type) {
        params = ParamsFactory.getInstance().getParams(uri);
        type.initHeader();
    }

    private RequestParams initNameValPir(HashMap<String, Object> reqParams) {
        if (reqParams != null || reqParams.size() >= 0) {
            for (String key : reqParams.keySet()) {
                params.addBodyParameter(key, reqParams.get(key) + "");
            }
        }
        return params;
    }

    public BaseParams addJsonData(String jsonBody) {
        params.setBodyContent(jsonBody);
        return this;
    }

    public BaseParams addNameValPir(String key, String val) {
        params.addBodyParameter(key, val);
        return this;
    }

    public BaseParams addNameValPir(String key, File val) {
        params.addBodyParameter(key, val);
        return this;
    }

    public BaseParams addAllParams(HashMap<String, Object> params) {
        initNameValPir(params);
        return this;
    }

    public BaseParams addHeader(String name, String val) {
        params.addHeader(name, val);
        return this;
    }

    public BaseParams addJsonData(HashMap<String, Object> params) {
        String json = hashMapToString(params);
        addJsonData(json);
        return this;
    }

    public RequestParams getParams() {
        return params;
    }

    public void setParams(RequestParams params) {
        this.params = params;
    }

    /**
     * @param params 请求的hashtable类型参数
     * @return 返回一个json类型数据
     */
    public String hashMapToString(HashMap<String, Object> params) {
        JSONObject jsonObject = new JSONObject();
        if (params != null && !params.isEmpty()) {
            try {
                for (String key : params.keySet()) {
                    String val = params.get(key) + "";
                    jsonObject.put(key, val);
                }
                return jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static class Options {
        private static String token = "";

        public static String getToken() {
            return token;
        }

        public static void setToken(String token) {
            Options.token = token;
        }
    }

    /**
     * 获取Https的证书
     *
     * @param context Activity（fragment）的上下文
     * @return SSL的上下文对象
     */
    private static SSLContext s_sSLContext = null;


    private static SSLContext getSSLContext() {
        try {
            s_sSLContext = SSLContext.getInstance("TLS");
            //这里导入SSL证书文件
            InputStream inputStream = MyApplication.app.getAssets().open("facepp.p7b");
            //信任所有证书 （官方不推荐使用）
            s_sSLContext.init(null, new TrustManager[]{new MyX509TrustManager(inputStream)}, new SecureRandom());
            return s_sSLContext;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
