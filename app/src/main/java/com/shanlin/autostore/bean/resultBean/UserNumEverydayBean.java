package com.shanlin.autostore.bean.resultBean;

/**
 * Created by DELL on 2017/7/30 0030.
 */

public class UserNumEverydayBean {


    /**
     * code :
     * data : {"femaleCount":50870,"maleCount":15163,"memberCount":1}
     * message :
     */

    private String code;
    private DataBean data;
    private String message;

    @Override
    public String toString() {
        return "UserNumEverydayBean{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * femaleCount : 50870
         * maleCount : 15163
         * memberCount : 1
         */

        private int femaleCount;
        private int maleCount;
        private int memberCount;

        public int getFemaleCount() {
            return femaleCount;
        }

        public void setFemaleCount(int femaleCount) {
            this.femaleCount = femaleCount;
        }

        public int getMaleCount() {
            return maleCount;
        }

        public void setMaleCount(int maleCount) {
            this.maleCount = maleCount;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "femaleCount=" + femaleCount +
                    ", maleCount=" + maleCount +
                    ", memberCount=" + memberCount +
                    '}';
        }
    }
}
