package com.shanlin.android.autostore.entity.respone;

import com.google.gson.annotations.SerializedName;
import com.shanlin.android.autostore.entity.BaseBean;

/**
 * Created by cuieney on 17/08/2017.
 */

public class WxChatBean extends BaseBean{
    /**
     * code : 200
     * data : {"package":"Sign=WXPay","appid":"wxb51b89cba83263de","sign":"927B4F5D1D50C1FFF7A024EA84A66A29","partnerid":"1486219452","prepayid":"wx2017073116120729080652940544279638","noncestr":"PCprz9ZrbkEi714C","timestamp":"1501488727"}
     * message : 创建微信支付预订单成功！
     * version : 0.0.1
     */

    private DataBean data;
    private String version;

    @Override
    public String toString() {
        return "WxChatBean{" +
                ", data=" + data +
                ", version='" + version + '\'' +
                '}';
    }

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
         * package : Sign=WXPay
         * appid : wxb51b89cba83263de
         * sign : 927B4F5D1D50C1FFF7A024EA84A66A29
         * partnerid : 1486219452
         * prepayid : wx2017073116120729080652940544279638
         * noncestr : PCprz9ZrbkEi714C
         * timestamp : 1501488727
         */

        @SerializedName("package")
        private String packageX;
        private String appid;
        private String sign;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private String timestamp;

        @Override
        public String toString() {
            return "DataBean{" +
                    "packageX='" + packageX + '\'' +
                    ", appid='" + appid + '\'' +
                    ", sign='" + sign + '\'' +
                    ", partnerid='" + partnerid + '\'' +
                    ", prepayid='" + prepayid + '\'' +
                    ", noncestr='" + noncestr + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
