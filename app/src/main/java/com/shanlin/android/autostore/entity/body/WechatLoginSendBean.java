package com.shanlin.android.autostore.entity.body;

import java.io.Serializable;

/**
 * Created by shanlin on 2017-7-28.
 */

public class WechatLoginSendBean implements Serializable {

    private ExtraBean extra;
    private String    type;
    private String    unionid;

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public static class ExtraBean implements Serializable {

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
