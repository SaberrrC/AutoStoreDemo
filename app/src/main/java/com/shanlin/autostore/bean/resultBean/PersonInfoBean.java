package com.shanlin.autostore.bean.resultBean;

import com.shanlin.autostore.base.BaseBean;

import java.io.Serializable;

/**
 * Created by shanlin on 2017-8-4.
 */

public class PersonInfoBean extends BaseBean {

    private DataBean data;
    private String version;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class DataBean {

        private DateBean date;

        public DateBean getDate() {
            return date;
        }

        public void setDate(DateBean date) {
            this.date = date;
        }

        public static class DateBean implements Serializable{

            private int score;
            private int    creditLevel;
            private String gender;
            private long   createTime;
            private String level;
            private String idCard;
            private String nickName;
            private String mobile;
            private String avetorUrl;
            private String userName;
            private String faceToken;

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public int getCreditLevel() {
                return creditLevel;
            }

            public void setCreditLevel(int creditLevel) {
                this.creditLevel = creditLevel;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getIdCard() {
                return idCard;
            }

            public void setIdCard(String idCard) {
                this.idCard = idCard;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getAvetorUrl() {
                return avetorUrl;
            }

            public void setAvetorUrl(String avetorUrl) {
                this.avetorUrl = avetorUrl;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getFaceToken() {
                return faceToken;
            }

            public void setFaceToken(String faceToken) {
                this.faceToken = faceToken;
            }
        }
    }
}
