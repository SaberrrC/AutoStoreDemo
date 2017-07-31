package com.shanlin.autostore.bean.paramsBean;

/**
 * Created by DELL on 2017/7/31 0031.
 */

public class WXPayBody {

    private String deviceId;
    private String ip;
    private String orderNo;
    private String payAmount;
    private String storeId;

    public WXPayBody(String deviceId, String ip, String orderNo, String payAmount, String storeId) {
        this.deviceId = deviceId;
        this.ip = ip;
        this.orderNo = orderNo;
        this.payAmount = payAmount;
        this.storeId = storeId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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
