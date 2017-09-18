package com.shanlin.autostore.bean.resultBean;

/**
 * Created by DELL on 2017/7/27 0027.
 */

public class RealNameAuthenBean {


    /**
     * code : 400
     * message : 姓名或身份证号错误
     * version : 0.0.1
     */

    private String code;
    private String message;
    private String version;

    @Override
    public String toString() {
        return "RealNameAuthenBean{" +
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
