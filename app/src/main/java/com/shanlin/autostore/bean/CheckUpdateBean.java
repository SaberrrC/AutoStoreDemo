package com.shanlin.autostore.bean;

/**
 * @author:Zou ChangCheng
 * @date:2017/7/25
 * @project:autostore
 * @detail:
 */

public class CheckUpdateBean {
    private  int forceUpdate;
    private String MinVersion;
    private String version;

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
