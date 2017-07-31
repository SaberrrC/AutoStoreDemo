package com.shanlin.autostore.bean.paramsBean;

/**
 * Created by DELL on 2017/7/28 0028.
 */

public class LeMaiBaoPayBody {

    private String deviceId;
    private String orderNo;
    private String password;
    private String payAmount;
    private String storeId;

    public LeMaiBaoPayBody(String deviceId, String orderNo, String password, String payAmount, String storeId) {
        this.deviceId = deviceId;
        this.orderNo = orderNo;
        this.password = password;
        this.payAmount = payAmount;
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "LeMaiBaoPayBody{" +
                "deviceId='" + deviceId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", password='" + password + '\'' +
                ", payAmount=" + payAmount +
                ", storeId='" + storeId + '\'' +
                '}';
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
