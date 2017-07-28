package com.shanlin.autostore.bean.resultBean;

import com.shanlin.autostore.base.BaseBean;

/**
 * @author:Zou ChangCheng
 * @date:2017/7/25
 * @project:autostore
 * @detail:
 */

public class CheckUpdateBean  extends BaseBean{
    private  int forceUpdate;
    private String MinVersion;
    private String version;
    private String downloadUrl;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public String getMinVersion() {
        return MinVersion;
    }

    public String getVersion() {
        return version;
    }
}
