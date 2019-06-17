package com.spark.moduleotc.entity;


/**
 *  我的资产-昨日佣金
 */

public class AcceptMerchantInfoEntity {


    /**
     * code : 200
     * message : SUCCESS
     * data : {"daysBuyReward":0,"daysSellReward":0,"coin":"CNT"}
     * count : null
     * responseString : 200~SUCCESS
     * url : null
     * cid : null
     */

    private int code;
    private String message;
    private DataBean data;
    private Object count;
    private String responseString;
    private Object url;
    private Object cid;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getCount() {
        return count;
    }

    public void setCount(Object count) {
        this.count = count;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public Object getCid() {
        return cid;
    }

    public void setCid(Object cid) {
        this.cid = cid;
    }

    public static class DataBean {
        /**
         * daysBuyReward : 0.0
         * daysSellReward : 0.0
         * coin : CNT
         */

        private double daysBuyReward;
        private double daysSellReward;
        private String coin;

        public double getDaysBuyReward() {
            return daysBuyReward;
        }

        public void setDaysBuyReward(double daysBuyReward) {
            this.daysBuyReward = daysBuyReward;
        }

        public double getDaysSellReward() {
            return daysSellReward;
        }

        public void setDaysSellReward(double daysSellReward) {
            this.daysSellReward = daysSellReward;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }
    }
}
