package com.shanlin.android.autostore.entity.respone;

import com.shanlin.android.autostore.entity.BaseBean;

/**
 * Created by DELL on 2017/7/27 0027.
 */

public class RealNameAuthenBean extends BaseBean {


    /**
     * code : 400
     * message : 姓名或身份证号错误
     * version : 0.0.1
     */

    private String version;

    @Override
    public String toString() {
        return "RealNameAuthenBean{" +
                ", version='" + version + '\'' +
                '}';
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
