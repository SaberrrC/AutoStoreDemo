package com.shanlin.autostore.bean.paramsBean;

/**
 * Created by shanlin on 2017-7-30.
 */

public class OpenGardBody {

    public String deviceId;
    public String storeId;
    public String userDeviceId;

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

    public String getUserDeviceId() {
        return userDeviceId;
    }

    public void setUserDeviceId(String userDeviceId) {
        this.userDeviceId = userDeviceId;
    }

    public OpenGardBody(String deviceId, String storeId, String userDeviceId) {
        this.deviceId = deviceId;
        this.storeId = storeId;
        this.userDeviceId = userDeviceId;
    }
}
