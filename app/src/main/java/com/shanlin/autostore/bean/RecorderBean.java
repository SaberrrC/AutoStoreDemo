package com.shanlin.autostore.bean;

import java.util.List;

/**
 * Created by shanlin on 2017-7-28.
 {
 "code": "测试内容1951",
 "data": {
 "list": [
 {
 "cancelTime": 27147,
 "createTime": 67541,
 "discountAmount": 76864,
 "discountRate": 30284,
 "finishTime": 38023,
 "linkOrderId": "测试内容o1w8",
 "memberId": 58277,
 "orderNo": "测试内容23sj",
 "orderStatus": "测试内容2om4",
 "payAmount": 72432,
 "payStatus": "测试内容l76s",
 "paymentTime": 36031,
 "paymentType": "测试内容270d",
 "remark": "测试内容v360",
 "splitType": "测试内容70p0",
 "storeId": 77833,
 "totalAmount": 25221,
 "updateTime": 50005
 }
 ],
 "pageNum": 65318,
 "pageSize": 30862,
 "pages": 62465,
 "total": 40277
 },
 "message": "测试内容114u",
 "version": "测试内容3boa"
 }
 */

public class RecorderBean {

    /**
     * code : 测试内容1951
     * data : {"list":[{"cancelTime":27147,"createTime":67541,"discountAmount":76864,"discountRate":30284,"finishTime":38023,"linkOrderId":"测试内容o1w8","memberId":58277,"orderNo":"测试内容23sj","orderStatus":"测试内容2om4","payAmount":72432,"payStatus":"测试内容l76s","paymentTime":36031,"paymentType":"测试内容270d","remark":"测试内容v360","splitType":"测试内容70p0","storeId":77833,"totalAmount":25221,"updateTime":50005}],"pageNum":65318,"pageSize":30862,"pages":62465,"total":40277}
     * message : 测试内容114u
     * version : 测试内容3boa
     */

    private String code;
    private DataBean data;
    private String   message;
    private String   version;

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
         * list : [{"cancelTime":27147,"createTime":67541,"discountAmount":76864,"discountRate":30284,"finishTime":38023,"linkOrderId":"测试内容o1w8","memberId":58277,"orderNo":"测试内容23sj","orderStatus":"测试内容2om4","payAmount":72432,"payStatus":"测试内容l76s","paymentTime":36031,"paymentType":"测试内容270d","remark":"测试内容v360","splitType":"测试内容70p0","storeId":77833,"totalAmount":25221,"updateTime":50005}]
         * pageNum : 65318
         * pageSize : 30862
         * pages : 62465
         * total : 40277
         */

        private int pageNum;
        private int            pageSize;
        private int            pages;
        private int            total;
        private List<ListBean> list;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * cancelTime : 27147
             * createTime : 67541
             * discountAmount : 76864
             * discountRate : 30284
             * finishTime : 38023
             * linkOrderId : 测试内容o1w8
             * memberId : 58277
             * orderNo : 测试内容23sj
             * orderStatus : 测试内容2om4
             * payAmount : 72432
             * payStatus : 测试内容l76s
             * paymentTime : 36031
             * paymentType : 测试内容270d
             * remark : 测试内容v360
             * splitType : 测试内容70p0
             * storeId : 77833
             * totalAmount : 25221
             * updateTime : 50005
             */

            private int cancelTime;
            private int    createTime;
            private int    discountAmount;
            private int    discountRate;
            private int    finishTime;
            private String linkOrderId;
            private int    memberId;
            private String orderNo;
            private String orderStatus;
            private int    payAmount;
            private String payStatus;
            private int    paymentTime;
            private String paymentType;
            private String remark;
            private String splitType;
            private int    storeId;
            private int    totalAmount;
            private int    updateTime;

            public int getCancelTime() {
                return cancelTime;
            }

            public void setCancelTime(int cancelTime) {
                this.cancelTime = cancelTime;
            }

            public int getCreateTime() {
                return createTime;
            }

            public void setCreateTime(int createTime) {
                this.createTime = createTime;
            }

            public int getDiscountAmount() {
                return discountAmount;
            }

            public void setDiscountAmount(int discountAmount) {
                this.discountAmount = discountAmount;
            }

            public int getDiscountRate() {
                return discountRate;
            }

            public void setDiscountRate(int discountRate) {
                this.discountRate = discountRate;
            }

            public int getFinishTime() {
                return finishTime;
            }

            public void setFinishTime(int finishTime) {
                this.finishTime = finishTime;
            }

            public String getLinkOrderId() {
                return linkOrderId;
            }

            public void setLinkOrderId(String linkOrderId) {
                this.linkOrderId = linkOrderId;
            }

            public int getMemberId() {
                return memberId;
            }

            public void setMemberId(int memberId) {
                this.memberId = memberId;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public String getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(String orderStatus) {
                this.orderStatus = orderStatus;
            }

            public int getPayAmount() {
                return payAmount;
            }

            public void setPayAmount(int payAmount) {
                this.payAmount = payAmount;
            }

            public String getPayStatus() {
                return payStatus;
            }

            public void setPayStatus(String payStatus) {
                this.payStatus = payStatus;
            }

            public int getPaymentTime() {
                return paymentTime;
            }

            public void setPaymentTime(int paymentTime) {
                this.paymentTime = paymentTime;
            }

            public String getPaymentType() {
                return paymentType;
            }

            public void setPaymentType(String paymentType) {
                this.paymentType = paymentType;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getSplitType() {
                return splitType;
            }

            public void setSplitType(String splitType) {
                this.splitType = splitType;
            }

            public int getStoreId() {
                return storeId;
            }

            public void setStoreId(int storeId) {
                this.storeId = storeId;
            }

            public int getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(int totalAmount) {
                this.totalAmount = totalAmount;
            }

            public int getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(int updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
