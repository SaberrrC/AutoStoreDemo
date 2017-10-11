package com.shanlin.android.autostore.entity.respone;

import com.shanlin.android.autostore.entity.BaseBean;

/**
 * Created by cuieney on 17/08/2017.
 */

public class AliPayResultBean extends BaseBean{
    /**
     * code : 200
     * data : {"alipay":"测试内容a771","timestamp":"测试内容l6xp"}
     * message : 创建支付宝支付预订单成功！
     */

    private DataBean data;

    @Override
    public String toString() {
        return "AliPayResultBean{" +
                ", data=" + data +
                '}';
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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
