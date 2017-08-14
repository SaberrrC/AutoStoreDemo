package com.shanlin.android.autostore.entity;

import java.io.Serializable;

/**
 * Created by shanlin on 2017-7-21.
 */

public class BaseBean implements Serializable {

    /**
     * code : 200
     * message : 登录成功!
     */

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
