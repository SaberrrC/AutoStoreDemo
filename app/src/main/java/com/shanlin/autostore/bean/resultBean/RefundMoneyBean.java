package com.shanlin.autostore.bean.resultBean;

import com.shanlin.autostore.base.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by saberrrc on 2017/7/30.
 */

public class RefundMoneyBean extends BaseBean {


    /**
     * data : [{"amount":"","balance":"","content":"","createdTime":"","paymentType":"","title":""}]
     * version : 0.0.1
     */

    private String         version;
    private List<DataBean> data;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * amount :
         * balance :
         * content :
         * createdTime :
         * paymentType :
         * title :
         */

        private String amount;
        private String balance;
        private String content;
        private String createdTime;
        private String paymentType;
        private String title;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}