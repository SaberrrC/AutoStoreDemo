package com.shanlin.android.autostore.entity.event;

public class WxMessageEvent {

    private String message;
    private  String code;

    public WxMessageEvent() {
    }

    public WxMessageEvent(String message, String code) {
        this.message = message;
        this.code = code;
    }

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
