package com.shanlin.autostore.bean.resultBean;

/**
 * Created by DELL on 2017/7/27 0027.
 */

public class CreditBalanceCheckBean {


    /**
     * code : 200
     * data : {"creditLevel":0,"creditUsed":0,"credit":100,"creditBalance":100,"status":"0"}
     * message : 查询用户乐买宝信息成功
     * version : 0.0.1
     */

    private String code;
    private DataBean data;
    private String message;
    private String version;

    @Override
    public String toString() {
        return "CreditBalanceCheckBean{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", version='" + version + '\'' +
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class DataBean {
        /**
         * creditLevel : 0
         * creditUsed : 0
         * credit : 100
         * creditBalance : 100
         * status : 0
         */

        private String creditLevel;
        private String creditUsed;
        private String credit;
        private String creditBalance;
        private String status;

        @Override
        public String toString() {
            return "DataBean{" +
                    "creditLevel='" + creditLevel + '\'' +
                    ", creditUsed='" + creditUsed + '\'' +
                    ", credit='" + credit + '\'' +
                    ", creditBalance='" + creditBalance + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }

        public String getCreditLevel() {
            return creditLevel;
        }

        public void setCreditLevel(String creditLevel) {
            this.creditLevel = creditLevel;
        }

        public String getCreditUsed() {
            return creditUsed;
        }

        public void setCreditUsed(String creditUsed) {
            this.creditUsed = creditUsed;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getCreditBalance() {
            return creditBalance;
        }

        public void setCreditBalance(String creditBalance) {
            this.creditBalance = creditBalance;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
