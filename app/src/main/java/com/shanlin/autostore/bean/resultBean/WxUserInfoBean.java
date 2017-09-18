package com.shanlin.autostore.bean.resultBean;

import java.io.Serializable;
import java.util.List;

public class WxUserInfoBean implements Serializable {

    private String              city;
    private String              country;
    private String              headimgurl;
    private String              language;
    private String              nickname;
    private String              openid;
    private String              province;
    private int                 sex;
    private String              unionid;
    private List<privilegeBean> privilege;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<privilegeBean> privilege) {
        this.privilege = privilege;
    }

    public class privilegeBean implements Serializable {
        public String PRIVILEGE1;
        public String PRIVILEGE2;

        public String getPRIVILEGE1() {
            return PRIVILEGE1;
        }

        public void setPRIVILEGE1(String PRIVILEGE1) {
            this.PRIVILEGE1 = PRIVILEGE1;
        }

        public String getPRIVILEGE2() {
            return PRIVILEGE2;
        }

        public void setPRIVILEGE2(String PRIVILEGE2) {
            this.PRIVILEGE2 = PRIVILEGE2;
        }
    }

    @Override
    public String toString() {
        return "WxUserInfoBean{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", language='" + language + '\'' +
                ", nickname='" + nickname + '\'' +
                ", openid='" + openid + '\'' +
                ", province='" + province + '\'' +
                ", sex=" + sex +
                ", unionid='" + unionid + '\'' +
                ", privilege=" + privilege +
                '}';
    }
}

