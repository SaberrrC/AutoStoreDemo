package com.shanlin.autostore.bean.resultBean;

import com.shanlin.autostore.base.BaseBean;

/**
 * Created by DELL on 2017/7/27 0027.
 */

public class UserVertifyStatusBean extends BaseBean {


    /**
     * code : 200
     * data : {"verifyStatus":"0"}
     * message : 未认证
     */

    private String code;
    private DataBean data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
