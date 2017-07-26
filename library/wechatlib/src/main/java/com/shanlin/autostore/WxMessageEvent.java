package com.shanlin.autostore;

/**
 * @author: Zcc
 * @date: 2017/7/26
 * @detail:
 */

public class WxMessageEvent {

    private String message;
    private  String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
