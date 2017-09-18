package com.shanlin.autostore.bean.resultBean;

/**
 * Created by DELL on 2017/7/30 0030.
 */

public class AliPayResultBean {


    /**
     * code : 200
     * data : {"alipay":"测试内容a771","timestamp":"测试内容l6xp"}
     * message : 创建支付宝支付预订单成功！
     */

    private String code;
    private DataBean data;
    private String message;

    @Override
    public String toString() {
        return "AliPayResultBean{" +
                "code=" + code +
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
         * alipay : 测试内容a771
         * timestamp : 测试内容l6xp
         */

        private String alipay;
        private String timestamp;

        public String getAlipay() {
            return alipay;
        }

        public void setAlipay(String alipay) {
            this.alipay = alipay;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "alipay='" + alipay + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
    }
}
