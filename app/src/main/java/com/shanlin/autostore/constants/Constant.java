package com.shanlin.autostore.constants;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public class Constant {
    /**
     * 接口base url
     */
    public static final String BASE_URL = "http://116.62.116.235:8080/linstore/v1/";

    public static final String TOKEN              = "TOKEN";
    public static final String USER_PHONE_LOGINED = "USER_PHONE_LOGINED";
    public static final String DEVICEID           = "deviceId";
    public static final String FACE_VERIFY        = "FACE_VERIFY";
    public static final String FACE_VERIFY_NO     = "0";//用户没有人脸认证
    public static final String FACE_VERIFY_OK     = "1";//用户已经人脸认证
    public static final String USER_PHONE_HISTORY = "USER_PHONE_HISTORY";//用于登录界面数据回显
    public static final String USER_INFO          = "USER_INFO";//传递用户数据

    public static final String FACE_REGESTED_OK = "FACE_REGESTED_OK";

    /**
     * 微信
     */
    public static final String APP_ID     = "wxb51b89cba83263de";
    public static final String APP_SECRET = "1e73ced172f384ef6305e2276d2a9b96";


    public static class MainActivityArgument {
        public static final String MAIN_ACTIVITY = "MAIN_ACTIVITY";
        public static final String GATE          = "GATE";
    }

    public static final class SaveFaceActivity {
        public static final String IMAGE_PATH   = "IMAGE_PATH";
        public static final String IMAGE_BASE64 = "IMAGE_BASE64";
    }

}
