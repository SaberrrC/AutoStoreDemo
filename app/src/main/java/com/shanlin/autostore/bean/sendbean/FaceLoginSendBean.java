package com.shanlin.autostore.bean.sendbean;

/**
 * Created by shanlin on 2017-7-27.
 */

public class FaceLoginSendBean {

    private String imageBase64;
    private String userDeviceId;

    public FaceLoginSendBean(String imageBase64, String userDeviceId) {
        this.imageBase64 = imageBase64;
        this.userDeviceId = userDeviceId;
    }
}
