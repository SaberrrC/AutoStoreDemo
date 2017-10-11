package com.shanlin.android.autostore.entity.respone;

import com.shanlin.android.autostore.entity.BaseBean;

/**
 * Created by cuieney on 17/08/2017.
 */

public class LeMaiBaoPayResultBean extends BaseBean{
    /**
     * code : 测试内容5p50
     * data : {"payAmount":"测试内容4n38","payStatus":"测试内容s3y6","paymentTime":"测试内容w24e"}
     * message : 测试内容6x63
     */

    private DataBean data;

    @Override
    public String toString() {
        return "LeMaiBaoPayResultBean{" +
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
