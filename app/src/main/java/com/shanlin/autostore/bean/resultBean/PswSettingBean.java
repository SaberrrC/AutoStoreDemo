package com.shanlin.autostore.bean.resultBean;

/**
 * Created by DELL on 2017/7/30 0030.
 */

public class PswSettingBean {


    /**
     * code : 200
     * message : 支付密码设置成功
     */

    private String code;
    private String message;

    @Override
    public String toString() {
        return "PswSettingBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

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
