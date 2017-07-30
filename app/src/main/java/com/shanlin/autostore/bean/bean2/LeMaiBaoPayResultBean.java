package com.shanlin.autostore.bean.bean2;

/**
 * Created by DELL on 2017/7/28 0028.
 */

public class LeMaiBaoPayResultBean {


    /**
     * code : 测试内容5p50
     * data : {"payAmount":"测试内容4n38","payStatus":"测试内容s3y6","paymentTime":"测试内容w24e"}
     * message : 测试内容6x63
     */

    private String code;
    private DataBean data;
    private String message;

    @Override
    public String toString() {
        return "LeMaiBaoPayResultBean{" +
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
         * payAmount : 测试内容4n38
         * payStatus : 测试内容s3y6
         * paymentTime : 测试内容w24e
         */

        private String payAmount;
        private String payStatus;
        private String paymentTime;

        @Override
        public String toString() {
            return "DataBean{" +
                    "payAmount='" + payAmount + '\'' +
                    ", payStatus='" + payStatus + '\'' +
                    ", paymentTime='" + paymentTime + '\'' +
                    '}';
        }

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        public String getPaymentTime() {
            return paymentTime;
        }

        public void setPaymentTime(String paymentTime) {
            this.paymentTime = paymentTime;
        }
    }
}
