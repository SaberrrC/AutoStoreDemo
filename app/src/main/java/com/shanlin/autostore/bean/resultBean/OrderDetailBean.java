package com.shanlin.autostore.bean.resultBean;

import com.shanlin.android.autostore.entity.BaseBean;

import java.util.List;

/**
 * Created by shanlin on 2017-7-30.
 */

public class OrderDetailBean extends BaseBean {


    /**
     * data : {"cancelTime":"","couponId":"","createTime":1501395338000,"deviceId":1,"discountAmount":0,"discountRate":1,"finishTime":"","id":562,"items":[{"RFID":"","amount":0.01,"couponId":"","createTime":1501395338000,"discountAmount":0,"discountRate":1,"id":812,"label":"","name":"恒大冰泉","orderId":562,"orderNo":"20170730OVGT4ST5","price":0.01,"productCode":"product00000001","productId":1,"quantity":1,"realAmount":0.01,"remark":"","remitAmount":"","rfid":"","score":"","status":"1","thumbnailsUrl":"http://www.picture1.com","updateTime":""}],"linkOrderId":"","memberId":288,"orderNo":"20170730OVGT4ST5","orderStatus":"1","payAmount":0.01,"payOrderNo":"","payStatus":"0","paymentTime":"","paymentType":"未支付","redpacketId":"","remark":"","remitAmount":"","score":"","splitType":"0","storeAddress":"","storeId":2,"storeName":"有人小店","totalAmount":0.01,"totalNumber":1,"updateTime":1501395343000}
     * version : 0.0.1
     */

    private DataBean data;
    private String version;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class DataBean {
        /**
         * cancelTime :
         * couponId :
         * createTime : 1501395338000
         * deviceId : 1
         * discountAmount : 0.0
         * discountRate : 1.0
         * finishTime :
         * id : 562
         * items : [{"RFID":"","amount":0.01,"couponId":"","createTime":1501395338000,"discountAmount":0,"discountRate":1,"id":812,"label":"","name":"恒大冰泉","orderId":562,"orderNo":"20170730OVGT4ST5","price":0.01,"productCode":"product00000001","productId":1,"quantity":1,"realAmount":0.01,"remark":"","remitAmount":"","rfid":"","score":"","status":"1","thumbnailsUrl":"http://www.picture1.com","updateTime":""}]
         * linkOrderId :
         * memberId : 288
         * orderNo : 20170730OVGT4ST5
         * orderStatus : 1
         * payAmount : 0.01
         * payOrderNo :
         * payStatus : 0
         * paymentTime :
         * paymentType : 未支付
         * redpacketId :
         * remark :
         * remitAmount :
         * score :
         * splitType : 0
         * storeAddress :
         * storeId : 2
         * storeName : 有人小店
         * totalAmount : 0.01
         * totalNumber : 1
         * updateTime : 1501395343000
         */

        private String cancelTime;
        private String          couponId;
        private long            createTime;
        private int             deviceId;
        private double          discountAmount;
        private double          discountRate;
        private String          finishTime;
        private int             id;
        private String          linkOrderId;
        private int             memberId;
        private String          orderNo;
        private String          orderStatus;
        private double          payAmount;
        private String          payOrderNo;
        private String          payStatus;
        private String          paymentTime;
        private String          paymentType;
        private String          redpacketId;
        private String          remark;
        private String          remitAmount;
        private String          score;
        private String          splitType;
        private String          storeAddress;
        private int             storeId;
        private String          storeName;
        private double          totalAmount;
        private int             totalNumber;
        private long            updateTime;
        private List<ItemsBean> items;

        public String getCancelTime() {
            return cancelTime;
        }

        public void setCancelTime(String cancelTime) {
            this.cancelTime = cancelTime;
        }

        public String getCouponId() {
            return couponId;
        }

        public void setCouponId(String couponId) {
            this.couponId = couponId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(int deviceId) {
            this.deviceId = deviceId;
        }

        public double getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(double discountAmount) {
            this.discountAmount = discountAmount;
        }

        public double getDiscountRate() {
            return discountRate;
        }

        public void setDiscountRate(double discountRate) {
            this.discountRate = discountRate;
        }

        public String getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(String finishTime) {
            this.finishTime = finishTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public double getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(double payAmount) {
            this.payAmount = payAmount;
        }

        public String getPayOrderNo() {
            return payOrderNo;
        }

        public void setPayOrderNo(String payOrderNo) {
            this.payOrderNo = payOrderNo;
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

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getRedpacketId() {
            return redpacketId;
        }

        public void setRedpacketId(String redpacketId) {
            this.redpacketId = redpacketId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRemitAmount() {
            return remitAmount;
        }

        public void setRemitAmount(String remitAmount) {
            this.remitAmount = remitAmount;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getSplitType() {
            return splitType;
        }

        public void setSplitType(String splitType) {
            this.splitType = splitType;
        }

        public String getStoreAddress() {
            return storeAddress;
        }

        public void setStoreAddress(String storeAddress) {
            this.storeAddress = storeAddress;
        }

        public int getStoreId() {
            return storeId;
        }

        public void setStoreId(int storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public int getTotalNumber() {
            return totalNumber;
        }

        public void setTotalNumber(int totalNumber) {
            this.totalNumber = totalNumber;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * RFID :
             * amount : 0.01
             * couponId :
             * createTime : 1501395338000
             * discountAmount : 0.0
             * discountRate : 1.0
             * id : 812
             * label :
             * name : 恒大冰泉
             * orderId : 562
             * orderNo : 20170730OVGT4ST5
             * price : 0.01
             * productCode : product00000001
             * productId : 1
             * quantity : 1
             * realAmount : 0.01
             * remark :
             * remitAmount :
             * rfid :
             * score :
             * status : 1
             * thumbnailsUrl : http://www.picture1.com
             * updateTime :
             */

            private String RFID;
            private double amount;
            private String couponId;
            private long   createTime;
            private double discountAmount;
            private double discountRate;
            private int    id;
            private String label;
            private String name;
            private int    orderId;
            private String orderNo;
            private double price;
            private String productCode;
            private int    productId;
            private int    quantity;
            private double realAmount;
            private String remark;
            private String remitAmount;
            private String rfid;
            private String score;
            private String status;
            private String thumbnailsUrl;
            private String updateTime;

            public String getRFID() {
                return RFID;
            }

            public void setRFID(String RFID) {
                this.RFID = RFID;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public String getCouponId() {
                return couponId;
            }

            public void setCouponId(String couponId) {
                this.couponId = couponId;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public double getDiscountAmount() {
                return discountAmount;
            }

            public void setDiscountAmount(double discountAmount) {
                this.discountAmount = discountAmount;
            }

            public double getDiscountRate() {
                return discountRate;
            }

            public void setDiscountRate(double discountRate) {
                this.discountRate = discountRate;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public String getProductCode() {
                return productCode;
            }

            public void setProductCode(String productCode) {
                this.productCode = productCode;
            }

            public int getProductId() {
                return productId;
            }

            public void setProductId(int productId) {
                this.productId = productId;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public double getRealAmount() {
                return realAmount;
            }

            public void setRealAmount(double realAmount) {
                this.realAmount = realAmount;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getRemitAmount() {
                return remitAmount;
            }

            public void setRemitAmount(String remitAmount) {
                this.remitAmount = remitAmount;
            }

            public String getRfid() {
                return rfid;
            }

            public void setRfid(String rfid) {
                this.rfid = rfid;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getThumbnailsUrl() {
                return thumbnailsUrl;
            }

            public void setThumbnailsUrl(String thumbnailsUrl) {
                this.thumbnailsUrl = thumbnailsUrl;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
