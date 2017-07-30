package com.shanlin.autostore.bean.bean2;

import com.shanlin.autostore.base.BaseBean;

/**
 * Created by shanlin on 2017-7-21.
 {
 "code": "00",
 "data": {
 "creditLevel": "1",
 "gender": "0",
 "idCard": "350628177766920112",
 "level": "1",
 "memberId": "350628177766920112",
 "score": "200",
 "token": "测试内容t16e",
 "userName": "david"
 },
 "message": "成功"
 }
 */

public class FaceCheckBean extends BaseBean {

    /**
     * data : {"creditLevel":"1","gender":"0","idCard":"350628177766920112","level":"1","memberId":"350628177766920112","score":"200","token":"测试内容t16e","userName":"david"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * creditLevel : 1
         * gender : 0
         * idCard : 350628177766920112
         * level : 1
         * memberId : 350628177766920112
         * score : 200
         * token : 测试内容t16e
         * userName : david
         */

        private String creditLevel;
        private String gender;
        private String idCard;
        private String level;
        private String memberId;
        private String score;
        private String token;
        private String userName;

        public String getCreditLevel() {
            return creditLevel;
        }

        public void setCreditLevel(String creditLevel) {
            this.creditLevel = creditLevel;
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

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
