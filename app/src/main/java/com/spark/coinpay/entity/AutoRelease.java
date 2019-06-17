package com.spark.coinpay.entity;

/**
 * Created by Administrator on 2019/5/13 0013.
 */

public class AutoRelease {

    /**
     * cid : string
     * code : 0
     * count : {}
     * data : {"autoReleaseSwitch":0,"gmtCreate":"2019-05-13T09:29:27.067Z","gmtModified":"2019-05-13T09:29:27.067Z","id":0,"memberId":0}
     * message : string
     * responseString : string
     * url : string
     */

    private String cid;
    private int code;
    private CountBean count;
    private DataBean data;
    private String message;
    private String responseString;
    private String url;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CountBean getCount() {
        return count;
    }

    public void setCount(CountBean count) {
        this.count = count;
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

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class CountBean {
    }

    public static class DataBean {
        /**
         * autoReleaseSwitch : 0
         * gmtCreate : 2019-05-13T09:29:27.067Z
         * gmtModified : 2019-05-13T09:29:27.067Z
         * id : 0
         * memberId : 0
         */

        private int autoReleaseSwitch;
        private String gmtCreate;
        private String gmtModified;
        private int id;
        private int memberId;

        public int getAutoReleaseSwitch() {
            return autoReleaseSwitch;
        }

        public void setAutoReleaseSwitch(int autoReleaseSwitch) {
            this.autoReleaseSwitch = autoReleaseSwitch;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(String gmtModified) {
            this.gmtModified = gmtModified;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }
    }
}
