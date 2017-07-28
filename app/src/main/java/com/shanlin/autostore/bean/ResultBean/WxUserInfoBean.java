package com.shanlin.autostore.bean.resultBean;

import java.util.List;

/**
 * @author:Zou ChangCheng
 * @date:2017/7/26
 * @project:autostore
 * @detail:
 */

public class WxUserInfoBean {
    public String openid;
    public String nickname;
    public String sex;
    public String province;
    public String city;
    public String country;
    public String headimgurl;
    public String unionid;
    public List<privilegeBean> privilege;
    public static  class privilegeBean{
        public String PRIVILEGE1;
        public String PRIVILEGE2;
    }

}

