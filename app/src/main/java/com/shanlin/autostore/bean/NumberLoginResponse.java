package com.shanlin.autostore.bean;

import com.shanlin.autostore.base.BaseBean;

/**
 * Created by shanlin on 2017-7-20.
 {
 "code": "200",
 "data": {
 "token": "134U1AH8OOHNTKLFP8DDT3F4LD"
 },
 "message": "登录成功!",
 "version": "0.0.1"
 }
 */

public class NumberLoginResponse extends BaseBean {


    /**
     * data : {"token":"134U1AH8OOHNTKLFP8DDT3F4LD"}
     * version : 0.0.1
     */

    private DataBean data;
    private String version;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class DataBean {
        /**
         * token : 134U1AH8OOHNTKLFP8DDT3F4LD
         */

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
