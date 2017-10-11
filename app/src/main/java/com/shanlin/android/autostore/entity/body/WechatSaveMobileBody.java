package com.shanlin.android.autostore.entity.body;

/**
 * Created by shanlin on 2017-7-30.
 {
 "extra":{
 "nickname":"",
 "openid":"oFyxTxMY-",
 "sex":""
 },
 "mobile":"1",
 "validCode":"455"
 }
 */

public class WechatSaveMobileBody {


    private ExtraBean extra;
    private String mobile;
    private String validCode;

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    public static class ExtraBean {
        /**
         * nickname : 楚楚楚
         * openid : oFyxTxMY-x90VdyaJiLt9lf1S2jY
         * sex :
         */

        private String nickname;
        private String openid;
        private String sex;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
