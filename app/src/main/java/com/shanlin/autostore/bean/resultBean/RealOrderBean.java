package com.shanlin.autostore.bean.resultBean;

import java.util.List;

/**
 * Created by DELL on 2017/7/28 0028.
 */

public class RealOrderBean {


    /**
     * code : 测试内容m5y8
     * data : {"createTime":42516,"discountAmount":78304,"items":[{"amount":78782,"discountAmount":58050,"label":"测试内容451b","name":"测试内容chiv","price":17416,"productCode":"测试内容s8f4","quantity":43073,"realAmount":68205,"thumbnailsUrl":"测试内容5u3n"}],"orderNo":"测试内容4dv5","remark":"测试内容ovu2","totalAmount":50635}
     * message : 测试内容o67x
     */

    private String code;
    private DataBean data;
    private String message;

    @Override
    public String toString() {
        return "RealOrderBean{" +
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
         * createTime : 42516
         * discountAmount : 78304
         * items : [{"amount":78782,"discountAmount":58050,"label":"测试内容451b","name":"测试内容chiv","price":17416,"productCode":"测试内容s8f4","quantity":43073,"realAmount":68205,"thumbnailsUrl":"测试内容5u3n"}]
         * orderNo : 测试内容4dv5
         * remark : 测试内容ovu2
         * totalAmount : 50635
         */

        private String createTime;
        private String discountAmount;
        private String orderNo;
        private String remark;
        private String totalAmount;
        private List<ItemsBean> items;

        @Override
        public String toString() {
            return "DataBean{" +
                    "createTime=" + createTime +
                    ", discountAmount=" + discountAmount +
                    ", orderNo='" + orderNo + '\'' +
                    ", remark='" + remark + '\'' +
                    ", totalAmount=" + totalAmount +
                    ", items=" + items +
                    '}';
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(String discountAmount) {
            this.discountAmount = discountAmount;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * amount : 78782
             * discountAmount : 58050
             * label : 测试内容451b
             * name : 测试内容chiv
             * price : 17416
             * productCode : 测试内容s8f4
             * quantity : 43073
             * realAmount : 68205
             * thumbnailsUrl : 测试内容5u3n
             */

            private String amount;
            private String discountAmount;
            private String label;
            private String name;
            private String price;
            private String productCode;
            private String quantity;
            private String realAmount;
            private String thumbnailsUrl;

            @Override
            public String toString() {
                return "ItemsBean{" +
                        "amount=" + amount +
                        ", discountAmount=" + discountAmount +
                        ", label='" + label + '\'' +
                        ", name='" + name + '\'' +
                        ", price=" + price +
                        ", productCode='" + productCode + '\'' +
                        ", quantity=" + quantity +
                        ", realAmount=" + realAmount +
                        ", thumbnailsUrl='" + thumbnailsUrl + '\'' +
                        '}';
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getDiscountAmount() {
                return discountAmount;
            }

            public void setDiscountAmount(String discountAmount) {
                this.discountAmount = discountAmount;
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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getProductCode() {
                return productCode;
            }

            public void setProductCode(String productCode) {
                this.productCode = productCode;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getRealAmount() {
                return realAmount;
            }

            public void setRealAmount(String realAmount) {
                this.realAmount = realAmount;
            }

            public String getThumbnailsUrl() {
                return thumbnailsUrl;
            }

            public void setThumbnailsUrl(String thumbnailsUrl) {
                this.thumbnailsUrl = thumbnailsUrl;
            }
        }
    }
}
