package com.shanlin.android.autostore.entity.body;

/**
 * Created by DELL on 2017/7/28 0028.
 */

public class ZXingOrderBean {


    /**
     * title : 订单支付
     * action : 2
     * orderNo : 001
     * deviceId : rid
     */

    private String title;
    private int action;
    private String orderNo;
    private String deviceId;

    @Override
    public String toString() {
        return "ZXingOrderBean{" +
                "title='" + title + '\'' +
                ", action=" + action +
                ", orderNo='" + orderNo + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
