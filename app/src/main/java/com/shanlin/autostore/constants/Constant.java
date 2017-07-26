package com.shanlin.autostore.constants;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public class Constant {

    public static final String TOKEN            = "TOKEN";
    public static final String LOGIN_USER_PHONE = "LOGIN_USER_PHONE";

    /**
     * 微信
     */
    public static final String APP_ID = "wxb51b89cba83263de";
    public static final String APP_SECRET = "1e73ced172f384ef6305e2276d2a9b96";

    /**
     * 接口base url
     */
    public static final String BASE_URL = "https://apimock.shanlinjinrong.online/";

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
