package com.shanlin.autostore.bean.resultBean;

import com.shanlin.autostore.base.BaseBean;

/**
 * Created by Sxf on 2017/7/25.
 */

public class WxChatBean extends BaseBean{
    public WxResponseBean data;

    public class WxResponseBean{
        public String appid;
        public String noncestr;
        public String partnerid;
        public String timestamp;
        public String prepay_id;
        public String sign;
    }
}
