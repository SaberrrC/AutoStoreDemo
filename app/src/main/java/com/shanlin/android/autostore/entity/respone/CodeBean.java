package com.shanlin.android.autostore.entity.respone;


import com.shanlin.android.autostore.entity.BaseBean;

/**
 * Created by shanlin on 2017-7-20.
 {
 "code": "200",
 "message": "验证码发送成功"
 }
 */

public class CodeBean extends BaseBean {


    /**
     * version : 0.0.1
     */

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
