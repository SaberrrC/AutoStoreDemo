package com.shanlin.android.autostore.entity.respone;

import com.shanlin.android.autostore.entity.BaseBean;

/**
 * Created by DELL on 2017/7/30 0030.
 */

public class PswSettingBean extends BaseBean{


    /**
     * code : 400
     * message : 密码格式不正确
     * version : 0.0.1
     */
    private String version;

    @Override
    public String toString() {
        return "PswSettingBean{" +
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
