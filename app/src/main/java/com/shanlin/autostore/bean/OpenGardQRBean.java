package com.shanlin.autostore.bean;

/**
 * Created by shanlin on 2017-7-30.
 */

public class OpenGardQRBean {

    /**
     * action : 1
     * deviceId :  1
     * storeId : 1
     * title : 打开闸机
     */

    private int action;
    private String deviceId;
    private String storeId;
    private String title;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
