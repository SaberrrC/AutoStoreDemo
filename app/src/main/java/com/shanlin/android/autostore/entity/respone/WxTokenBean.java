package com.shanlin.android.autostore.entity.respone;

/**
 * @author:Zou ChangCheng
 * @date:2017/7/26
 * @project:autostore
 * @detail:
 */

public class WxTokenBean {
    private String access_token;
    private String refresh_token;
    private String openid;
    private String scope;
    private int    expires;

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public String getScope() {
        return scope;
    }

    public int getExpires() {
        return expires;
    }
}
