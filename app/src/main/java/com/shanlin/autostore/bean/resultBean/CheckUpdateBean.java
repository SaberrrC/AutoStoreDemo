package com.shanlin.autostore.bean.resultBean;

import com.shanlin.autostore.base.BaseBean;

import java.io.Serializable;

/**
 * @author:Zou ChangCheng
 * @date:2017/7/25
 * @project:autostore
 * @detail:
 */

public class CheckUpdateBean  extends BaseBean{
    /**
     * data : {"id":2,"version":"2","minVersion":"1.0.1","forceUpdate":"0","downloadUrl":"0","type":"2"}
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

    public static class DataBean implements Serializable{
        /**
         * id : 2
         * version : 2
         * minVersion : 1.0.1
         * forceUpdate : 0
         * downloadUrl : 0
         * type : 2
         */

        private int id;
        private String version;
        private String minVersion;
        private String forceUpdate;
        private String downloadUrl;
        private String type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getMinVersion() {
            return minVersion;
        }

        public void setMinVersion(String minVersion) {
            this.minVersion = minVersion;
        }

        public String getForceUpdate() {
            return forceUpdate;
        }

        public void setForceUpdate(String forceUpdate) {
            this.forceUpdate = forceUpdate;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
