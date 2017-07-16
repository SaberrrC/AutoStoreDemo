package com.example.shanlin.facedemo;


public class Constant {

    public static final String TOKEN                = "x-sljr-session-token";
    public static final String SHOPPINGLISTITEMBEAN = "SHOPPINGLISTITEMBEAN";
    public static final String USER_PHONE_HISTORY   = "USER_PHONE_HISTORY";
    public static final String ANDROID              = "android";
    public static final String USER_PHONE           = "USER_PHONE";
    public static final String USER_PHONE_UNREGEST  = "USER_PHONE_UNREGEST";

    public static final String FACE_APP_KEY = "wZXD-Gi2g6Z0AC8qasqa0rldR34MD3CM";
    public static final String API_SECRET   = "-kYRm5b1SkpfPsnHtMUaa57k5HO3G9Kv";
    //活体照片
    public static final String FACE_PHOTO   = "photo";
    /**
     * 身份证认证
     */
    public static class CardAuth {
        public static final String BACK_PHOTO     = "backPhoto";
        public static final String POSITIVE_PHOTO = "positivePhoto";
        public static final String CARD_NO        = "idCardNo";
        public static final String ADDRESS        = "userAddress";
        public static final String BIRTHDAY       = "userBirthday";
        public static final String DEPART         = "userDepart";
        public static final String NAME           = "userName";
        public static final String NATION         = "userNation";
        public static final String SEX            = "userSex";
        public static final String VALIDDATE      = "userValidityTime";
    }

    /**
     * 正式的face++的key
     */
    public static final String RELEASE_FACE_APP_KEY    = "wZXD-Gi2g6Z0AC8qasqa0rldR34MD3CM";
    public static final String RELEASE_FACE_API_SECRET = "-kYRm5b1SkpfPsnHtMUaa57k5HO3G9Kv";

    public static final String FACE_HOSTNAME = "api-cn.faceplusplus.com";

    public static final class PagerName {
        public static final String PRODUCTRECOMMENDFRAGMENT = "PRODUCTRECOMMENDFRAGMENT";//主页
        public static final String LIFE                     = "LIFE";//周边服务
    }

    private Constant() {
    }

    public static class OptionUrl {

        private static final String VERIFICATION_CODE = "/shortcode";//获取验证码
        private static final String VERSION           = "/version";
        private static final String LOGIN             = "/login";//登录
        private static final String DEBITQUERY        = "/debitquery";//获取借款基本数据
        private static final String DEBIT             = "/debit";//获取借款产品接口
        private static final String SUBMITSUGGEST     = "/contact"; //提交联系我们信息
        private static final String BILLQUERY         = "/bill/query"; //查询账单
        private static final String BILL_DETAIL       = "/bill/detail"; //查询账单
        private static final String VERSIONUPDATE     = "/version/update";  //检查版本更新
        private static final String GEO               = "/geo"; //查询账单
        private static final String LOGIN_OUT         = "/logout"; //查询账单
        private static final String CHECK_REGIST      = "/judgeRegister"; //检查是否注册
        private static final String CHECK_USER_TYPE   = "/querysalesman"; //检查是否是业务员 post
        private static final String MY_INCOME         = "/queryTerminal"; //我的收入
        private static final String MY_INCOME_DETAIL  = "/queryOrder";//我的收入明细
        private static final String CARD_AUTH         = "/register/idcard";//身份认证
        private static final String SMS_CODE          = "/getBankCard/code";//获取银行预留手机号短信验证码
        private static final String BIND_BANK_CARD    = "/bind/bankCard";//绑定银行卡

        private static final String LOGIN_LOCATION = "/maidian/login-location";//上传登陆及位置信息
        private static final String DEVICE_INFO    = "/maidian/device-info";//上传设备信息

        private static final String BANK_CARD_LIST  = "/bind/bankCardList";//银行卡列表
        private static final String CHECK_USER_AUTH = "/register/step";//验证用户是否认证过
        //人脸与身份证比对
        public static final  String FACE_COMPARE    = "https:/api-cn.faceplusplus.com/facepp/v3/compare";
        private static final String UPLOAD_FACE_IMG = "/maidian/face-photo";//上传人脸照片


        /**
         * 获取终端机位置
         */
        public static class TERMINAL {
            public static String DISTANCE = "distance";
            public static String LAT      = "lat";
            public static String LNG      = "lng";
        }

        //埋点信息
        public static class UserMsg {
            public static String IP           = "ip";//IP
            public static String LAT          = "latitude"; //所在纬度
            public static String LNG          = "longitude"; //所在经度
            public static String LOGINTIME    = "loginDuration"; //登录时长
            public static String PROVINCECITY = "provinceCity"; //登陆城市

            public static String DEVICE_NUMBER = "deviceNumber";  //设备号
            public static String WIFI_MAC      = "wifiMac";// wifi_mac
        }

        /**
         * 判断是否是业务员
         */
        public static class CheckUserType {
            public static final String MOBILENO = "mobileNo";
        }

        /**
         * 登录
         */
        public static class Login {
            public final static String MOBILENO = "mobileNo";
            public final static String CODE     = "code";
        }


        public static class Submitsuggest {
            public final static String MESSAGE = "message";
        }

        public static class CheckVersion {
            public final static String OSTYPE         = "ostype";
            public final static String OSTVERSION     = "osversion";
            public final static String CURRENTVERSION = "version";
        }

        public static class InComeMsg {
            public final static String MYINCOMEHEADBEAN    = "MYINCOMEHEADBEAN";
            public final static String REQ_PAGENO          = "pageno";
            public final static String InCome_PHOME_NUMBER = "mobileNo";
            public final static String DeviceId            = "deviceId";
            public final static String EARNINGS            = "EARNINGS";
        }

        /**
         * 数据返回的 code
         */
        public static class RESPONSE_CODE {
            public static final String SUCCESS_CODE    = "000000";//成功
            public static final String UNREGEST        = "010005";
            public static final String TOKEN_ERROR     = "010006";
            public static final String SMS_FAST        = "000001";
            public static final String THREE_TIMES     = "010011";
            public static final String TWENTY_TIMES    = "010008";
            public static final String UNREGISTER_CODE = "010003";
        }

        public static class HomeFragmentCustomBorrow {
            public static String CUSTOMBORROWBEAN = "CUSTOMBORROWBEAN";
        }

        public static class ProductRecommend {
            public static final String BORROWDEADLINE     = "borrowDeadline";
            public static final String LOANAMOUNT         = "loanAmount";
            public static final String PROFESSIONIDENTITY = "professionIdentity";
        }

        public static class BillRuery {
            public final static String KEYWORK = "keyword";
            public final static String PAGENO  = "pageno";
        }

        public static class ORDER_DETAIL {
            public static final String ORDER_NUMBER = "billid";
        }

        //-------------------------------------------------------------------------------------
        public static class IDCARDCODE {
            public static final int START_FAILED     = 30001;
            public static final int OPEN_FAILED      = 30002;
            public static final int CLOSE_FAILED     = 30003;
            public static final int RESET_FAILED     = 30004;
            public static final int SELECT_FAILED    = 30005;
            public static final int FIND_FAILED      = 30006;
            public static final int READ_FAILED      = 30007;
            public static final int SAVEPHOTO_FAILED = 30008;
        }

        /**
         * 身份证认证
         */
        public static class CardAuth {
            public static final String BACK_PHOTO     = "backPhoto";
            public static final String POSITIVE_PHOTO = "positivePhoto";
            public static final String CARD_NO        = "idCardNo";
            public static final String ADDRESS        = "userAddress";
            public static final String BIRTHDAY       = "userBirthday";
            public static final String DEPART         = "userDepart";
            public static final String NAME           = "userName";
            public static final String NATION         = "userNation";
            public static final String SEX            = "userSex";
            public static final String VALIDDATE      = "userValidityTime";
        }

        /**
         * 添加银行卡
         */
        public static class AddBankCard {
            public static final String BANK_CARD_NUM = "bankCardNumber";
            public static final String CAPTCHA       = "captcha";
            public static final String PHONE_NUM     = "reservedPhoneNumber";
        }

        /**
         * 人脸与身份证头像比对
         */
        public static class FaceCardCompare {
            public static final String API_KEY        = "api_key";
            public static final String API_SECRET     = "api_secret";
            public static final String IMAGE_BASE64_1 = "image_base64_1";
            public static final String IMAGE_BASE64_2 = "image_base64_2";
        }
    }
    /**
     * 人脸与身份证头像比对
     */
    public static class FaceCardCompare {
        public static final String API_KEY        = "api_key";
        public static final String API_SECRET     = "api_secret";
        public static final String IMAGE_BASE64_1 = "image_base64_1";
        public static final String IMAGE_BASE64_2 = "image_base64_2";
    }
}
