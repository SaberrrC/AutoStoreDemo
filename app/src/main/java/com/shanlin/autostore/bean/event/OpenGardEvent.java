package com.shanlin.autostore.bean.event;

/**
 * Created by saberrrc on 2017/7/31.
 */

public class OpenGardEvent {
//{"status":"闸机打开成功！","storeId":"1","type":"1"}

    /**
     * status : 闸机打开成功！
     * storeId : 1
     * type : 1
     */

    private String status;
    private String storeId;
    private String type;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
