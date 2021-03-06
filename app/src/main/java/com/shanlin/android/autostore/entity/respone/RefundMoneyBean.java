package com.shanlin.android.autostore.entity.respone;

import com.shanlin.android.autostore.entity.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cuieney on 15/08/2017.
 */

public class RefundMoneyBean extends BaseBean {
    /**
     * data : [{"amount":"1.00","balance":"1.00","content":"内容2321","createdTime":"1501206138000","paymentType":"1","title":"title"},{"amount":"1.00","balance":"2.00","content":"内容2321","createdTime":"1501206138000","paymentType":"1","title":"title"},{"amount":"11.00","balance":"2.00","content":"内容2321","createdTime":"1501206138000","paymentType":"1","title":"title"},{"amount":"1.00","balance":"2.00","content":"内容2321","createdTime":"1501206138000","paymentType":"1","title":"title"},{"amount":"1.00","balance":"2.00","content":"内容2321","createdTime":"1501206138000","paymentType":"1","title":"title"},{"amount":"1.00","balance":"2.00","content":"内容2321","createdTime":"1501206138000","paymentType":"1","title":"title"},{"amount":"1.00","balance":"2.00","content":"内容2321","createdTime":"1501206138000","paymentType":"1","title":"title"},{"amount":"1.00","balance":"2.00","content":"内容2321","createdTime":"1501206138000","paymentType":"1","title":"title"},{"amount":"1.00","balance":"2.00","content":"内容2321","createdTime":"1501206138000","paymentType":"1","title":"title"}]
     * version : 0.0.1
     */

    private String version;
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
         * amount : 1.00
         * balance : 1.00
         * content : 内容2321
         * createdTime : 1501206138000
         * paymentType : 1
         * title : title
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
