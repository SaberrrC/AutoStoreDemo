package com.shanlin.android.autostore.entity.body;

import com.shanlin.android.autostore.entity.BaseBean;

/**
 * Created by shanlin on 2017-7-27.
 */

public class NumberLoginBean extends BaseBean {

    /**
     * userName : 18601615173
     * validCode : 4761
     */

    private String userName;
    private String validCode;

    public NumberLoginBean(String userName, String validCode) {
        this.userName = userName;
        this.validCode = validCode;
    }
}
