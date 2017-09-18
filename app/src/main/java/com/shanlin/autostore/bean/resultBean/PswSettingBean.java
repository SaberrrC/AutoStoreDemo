package com.shanlin.autostore.bean.resultBean;

/**
 * Created by DELL on 2017/7/30 0030.
 */

public class PswSettingBean {


    /**
     * code : 400
     * message : 密码格式不正确
     * version : 0.0.1
     */

    private String code;
    private String message;
    private String version;

    @Override
    public String toString() {
        return "PswSettingBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", version='" + version + '\'' +
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
