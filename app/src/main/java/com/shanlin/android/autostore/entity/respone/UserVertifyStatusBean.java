package com.shanlin.android.autostore.entity.respone;

import com.shanlin.android.autostore.entity.BaseBean;

/**
 * Created by DELL on 2017/7/27 0027.
 */

public class UserVertifyStatusBean extends BaseBean{


    /**
     * code : 200
     * data : {"verifyStatus":"0"}
     * message : 未认证
     */

    private DataBean data;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        /**
         * verifyStatus : 0
         */

        private String verifyStatus;

        public String getVerifyStatus() {
            return verifyStatus;
        }

        public void setVerifyStatus(String verifyStatus) {
            this.verifyStatus = verifyStatus;
        }
    }
}
