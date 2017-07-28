package com.shanlin.autostore.bean;

import com.shanlin.autostore.base.BaseBean;

import java.io.Serializable;

/**
 * Created by shanlin on 2017-7-21.
 {
 "code": "200",
 "data": {
 "gender": "0",
 "level": "0",
 "idCard": "",
 "nickName": "",
 "mobile": "18601615173",
 "userDeviceId": "",
 "userName": "141fe1da9e9f20d854a",
 "token": "1a843d46-e75d-443f-a102-233356060817",
 "score": 0,
 "creditLevel": 0,
 "createTime": 1501134740000,
 "faceVerify": "1",
 "avetorUrl": ""
 },
 "message": "faceToken认证成功!",
 "version": "0.0.1"
 }
 */

public class LoginBean extends BaseBean implements Serializable{

    /**
     * data : {"gender":"0","level":"0","idCard":"","nickName":"","mobile":"18601615173","userDeviceId":"","userName":"141fe1da9e9f20d854a","token":"1a843d46-e75d-443f-a102-233356060817","score":0,"creditLevel":0,"createTime":1501134740000,"faceVerify":"1","avetorUrl":""}
     * version : 0.0.1
     */

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

    public static class DataBean implements Serializable{
        /**
         * gender : 0
         * level : 0
         * idCard :
         * nickName :
         * mobile : 18601615173
         * userDeviceId :
         * userName : 141fe1da9e9f20d854a
         * token : 1a843d46-e75d-443f-a102-233356060817
         * score : 0
         * creditLevel : 0
         * createTime : 1501134740000
         * faceVerify : 1
         * avetorUrl :
         */

        private String gender;
        private String level;
        private String idCard;
        private String nickName;
        private String mobile;
        private String userDeviceId;
        private String userName;
        private String token;
        private int    score;
        private int    creditLevel;
        private long   createTime;
        private String faceVerify;
        private String avetorUrl;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
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

        public String getUserDeviceId() {
            return userDeviceId;
        }

        public void setUserDeviceId(String userDeviceId) {
            this.userDeviceId = userDeviceId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

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

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getFaceVerify() {
            return faceVerify;
        }

        public void setFaceVerify(String faceVerify) {
            this.faceVerify = faceVerify;
        }

        public String getAvetorUrl() {
            return avetorUrl;
        }

        public void setAvetorUrl(String avetorUrl) {
            this.avetorUrl = avetorUrl;
        }
    }
}
