package com.shanlin.android.autostore.entity.body;

/**
 * Created by cuieney on 17/08/2017.
 */

public class AliPayOrderBody {
    private String deviceId;
    private String orderNo;
    private String payAmount;
    private String storeId;

    public AliPayOrderBody(String deviceId, String orderNo, String payAmount, String storeId) {
        this.deviceId = deviceId;
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
