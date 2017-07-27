package com.shanlin.autostore.bean;

import com.shanlin.autostore.base.BaseBean;

/**
 * Created by shanlin on 2017-7-21.
 {
 "code": "200",
 "data": {
 "avetorUrl": "",
 "creaditLevel": "",
 "createdTime": "",
 "faceVerify": "1",
 "gender": "",
 "idCard": "",
 "level": "",
 "mobile": "",
 "nickName": "",
 "score": "",
 "token": "134U1AH8OOHNTKLFP8DDT3F4LD",
 "userDeviceId": "",
 "userName": ""
 },
 "message": "登录成功!",
 "version": "0.0.1"
 }
 */

public class FaceLoginBean extends BaseBean {


    /**
     * data : {"avetorUrl":"","creaditLevel":"","createdTime":"","faceVerify":"1","gender":"","idCard":"","level":"","mobile":"","nickName":"","score":"","token":"134U1AH8OOHNTKLFP8DDT3F4LD","userDeviceId":"","userName":""}
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

    public static class DataBean {
        /**
         * avetorUrl :
         * creaditLevel :
         * createdTime :
         * faceVerify : 1
         * gender :
         * idCard :
         * level :
         * mobile :
         * nickName :
         * score :
         * token : 134U1AH8OOHNTKLFP8DDT3F4LD
         * userDeviceId :
         * userName :
         */

        private String avetorUrl;
        private String creaditLevel;
        private String createdTime;
        private String faceVerify;
        private String gender;
        private String idCard;
        private String level;
        private String mobile;
        private String nickName;
        private String score;
        private String token;
        private String userDeviceId;
        private String userName;

        public String getAvetorUrl() {
            return avetorUrl;
        }

        public void setAvetorUrl(String avetorUrl) {
            this.avetorUrl = avetorUrl;
        }

        public String getCreaditLevel() {
            return creaditLevel;
        }

        public void setCreaditLevel(String creaditLevel) {
            this.creaditLevel = creaditLevel;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getFaceVerify() {
            return faceVerify;
        }

        public void setFaceVerify(String faceVerify) {
            this.faceVerify = faceVerify;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
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
    }
}
