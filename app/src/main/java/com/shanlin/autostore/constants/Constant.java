package com.shanlin.autostore.constants;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public class Constant {

    public static final String TOKEN              = "TOKEN";
    public static final String USER_PHONE_LOGINED = "USER_PHONE_LOGINED";
    public static final String DEVICEID           = "deviceId";
    public static final String NO_FACE_VERIFY     = "NO_FACE_VERIFY";//用户没有人脸认证
    /**
     * 微信
     */
    public static final String APP_ID             = "wxb51b89cba83263de";

    public static final String APP_SECRET = "1e73ced172f384ef6305e2276d2a9b96";
    /**
     * 接口base url
     */
    public static final String BASE_URL   = "http://116.62.116.235:8080/linstore/v1/";

    public static class MainActivityArgument {
        public static final String MAIN_ACTIVITY = "MAIN_ACTIVITY";
        public static final String GATE          = "GATE";
        public static final String LOGIN         = "LOGIN";
        public static final String UNREGEST_USER = "UNREGEST_USER";
        public static final String REGESTED_USER = "REGESTED_USER";
    }

    public static final class SaveFaceActivity {
        public static final String IMAGE_PATH   = "IMAGE_PATH";
        public static final String IMAGE_BASE64 = "IMAGE_BASE64";
    }

}
