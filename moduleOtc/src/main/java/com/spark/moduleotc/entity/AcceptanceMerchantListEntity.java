package com.spark.moduleotc.entity;


/**
 * 我的资产-累计佣金和待结算佣金
 */

public class AcceptanceMerchantListEntity {

    /**
     * code : 200
     * message : SUCCESS
     * data : {"id":17,"memberId":46,"username":"u1554085573703","realName":"张张张","merchantType":"1","publishAdvertise":1,"orderTakingStatus":0,"isOnline":1,"onlineTime":1554272547000,"offlineTime":null,"startOrderTakingTime":1554263237000,"stopOrderTakingTime":1554272545000,"orderTakingType":3,"intransitOrder":0,"timeoutOrder":0,"lastOrderTime":1554263242000,"totalSuccessBuyOrder":9,"totalSuccessSellOrder":20,"daysSuccessBuyOrder":1,"daysSuccessSellOrder":6,"totalBuyOrder":87,"totalSellOrder":20,"daysBuyOrder":3,"daysSellOrder":6,"totalBuyReward":12,"totalSellReward":153,"daysBuyReward":2,"daysSellReward":85,"realtimeBuyTakingTimes":0,"realtimeSellTakingTimes":1,"realtimeBuyReward":0,"realtimeSellReward":5,"noSettledReward":null,"settledReward":null,"appealFailTimes":0,"appealTimes":0,"daysAppealFailTimes":0,"daysAppealTimes":0,"hurryTimes":0,"daysHurryTimes":0,"orderFailResponsibleTimes":78,"daysOrderFailResponsibleTimes":2,"avgReleaseTime":null,"avgPayTime":null,"rangeTimeOrder":0,"rangeTimeSuccessOrder":0,"createTime":1554087485000,"updateTime":1554263237000,"tradingDay":20190403}
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
         * id : 17
         * memberId : 46
         * username : u1554085573703
         * realName : 张张张
         * merchantType : 1
         * publishAdvertise : 1
         * orderTakingStatus : 0
         * isOnline : 1
         * onlineTime : 1554272547000
         * offlineTime : null
         * startOrderTakingTime : 1554263237000
         * stopOrderTakingTime : 1554272545000
         * orderTakingType : 3
         * intransitOrder : 0
         * timeoutOrder : 0
         * lastOrderTime : 1554263242000
         * totalSuccessBuyOrder : 9
         * totalSuccessSellOrder : 20
         * daysSuccessBuyOrder : 1
         * daysSuccessSellOrder : 6
         * totalBuyOrder : 87
         * totalSellOrder : 20
         * daysBuyOrder : 3
         * daysSellOrder : 6
         * totalBuyReward : 12.0
         * totalSellReward : 153.0
         * daysBuyReward : 2.0
         * daysSellReward : 85.0
         * realtimeBuyTakingTimes : 0
         * realtimeSellTakingTimes : 1
         * realtimeBuyReward : 0.0
         * realtimeSellReward : 5.0
         * noSettledReward : null
         * settledReward : null
         * appealFailTimes : 0
         * appealTimes : 0
         * daysAppealFailTimes : 0
         * daysAppealTimes : 0
         * hurryTimes : 0
         * daysHurryTimes : 0
         * orderFailResponsibleTimes : 78
         * daysOrderFailResponsibleTimes : 2
         * avgReleaseTime : null
         * avgPayTime : null
         * rangeTimeOrder : 0
         * rangeTimeSuccessOrder : 0
         * createTime : 1554087485000
         * updateTime : 1554263237000
         * tradingDay : 20190403
         */

        private int id;
        private int memberId;
        private String username;
        private String realName;
        private String merchantType;
        private int publishAdvertise;
        private int orderTakingStatus;
        private int isOnline;
        private long onlineTime;
        private Object offlineTime;
        private long startOrderTakingTime;
        private long stopOrderTakingTime;
        private int orderTakingType;
        private int intransitOrder;
        private int timeoutOrder;
        private long lastOrderTime;
        private int totalSuccessBuyOrder;
        private int totalSuccessSellOrder;
        private int daysSuccessBuyOrder;
        private int daysSuccessSellOrder;
        private int totalBuyOrder;
        private int totalSellOrder;
        private int daysBuyOrder;
        private int daysSellOrder;
        private double totalBuyReward;
        private double totalSellReward;
        private double daysBuyReward;
        private double daysSellReward;
        private int realtimeBuyTakingTimes;
        private int realtimeSellTakingTimes;
        private double realtimeBuyReward;
        private double realtimeSellReward;
        private double noSettledReward;
        private double settledReward;
        private int appealFailTimes;
        private int appealTimes;
        private int daysAppealFailTimes;
        private int daysAppealTimes;
        private int hurryTimes;
        private int daysHurryTimes;
        private int orderFailResponsibleTimes;
        private int daysOrderFailResponsibleTimes;
        private Object avgReleaseTime;
        private Object avgPayTime;
        private int rangeTimeOrder;
        private int rangeTimeSuccessOrder;
        private long createTime;
        private long updateTime;
        private int tradingDay;

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getMerchantType() {
            return merchantType;
        }

        public void setMerchantType(String merchantType) {
            this.merchantType = merchantType;
        }

        public int getPublishAdvertise() {
            return publishAdvertise;
        }

        public void setPublishAdvertise(int publishAdvertise) {
            this.publishAdvertise = publishAdvertise;
        }

        public int getOrderTakingStatus() {
            return orderTakingStatus;
        }

        public void setOrderTakingStatus(int orderTakingStatus) {
            this.orderTakingStatus = orderTakingStatus;
        }

        public int getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(int isOnline) {
            this.isOnline = isOnline;
        }

        public long getOnlineTime() {
            return onlineTime;
        }

        public void setOnlineTime(long onlineTime) {
            this.onlineTime = onlineTime;
        }

        public Object getOfflineTime() {
            return offlineTime;
        }

        public void setOfflineTime(Object offlineTime) {
            this.offlineTime = offlineTime;
        }

        public long getStartOrderTakingTime() {
            return startOrderTakingTime;
        }

        public void setStartOrderTakingTime(long startOrderTakingTime) {
            this.startOrderTakingTime = startOrderTakingTime;
        }

        public long getStopOrderTakingTime() {
            return stopOrderTakingTime;
        }

        public void setStopOrderTakingTime(long stopOrderTakingTime) {
            this.stopOrderTakingTime = stopOrderTakingTime;
        }

        public int getOrderTakingType() {
            return orderTakingType;
        }

        public void setOrderTakingType(int orderTakingType) {
            this.orderTakingType = orderTakingType;
        }

        public int getIntransitOrder() {
            return intransitOrder;
        }

        public void setIntransitOrder(int intransitOrder) {
            this.intransitOrder = intransitOrder;
        }

        public int getTimeoutOrder() {
            return timeoutOrder;
        }

        public void setTimeoutOrder(int timeoutOrder) {
            this.timeoutOrder = timeoutOrder;
        }

        public long getLastOrderTime() {
            return lastOrderTime;
        }

        public void setLastOrderTime(long lastOrderTime) {
            this.lastOrderTime = lastOrderTime;
        }

        public int getTotalSuccessBuyOrder() {
            return totalSuccessBuyOrder;
        }

        public void setTotalSuccessBuyOrder(int totalSuccessBuyOrder) {
            this.totalSuccessBuyOrder = totalSuccessBuyOrder;
        }

        public int getTotalSuccessSellOrder() {
            return totalSuccessSellOrder;
        }

        public void setTotalSuccessSellOrder(int totalSuccessSellOrder) {
            this.totalSuccessSellOrder = totalSuccessSellOrder;
        }

        public int getDaysSuccessBuyOrder() {
            return daysSuccessBuyOrder;
        }

        public void setDaysSuccessBuyOrder(int daysSuccessBuyOrder) {
            this.daysSuccessBuyOrder = daysSuccessBuyOrder;
        }

        public int getDaysSuccessSellOrder() {
            return daysSuccessSellOrder;
        }

        public void setDaysSuccessSellOrder(int daysSuccessSellOrder) {
            this.daysSuccessSellOrder = daysSuccessSellOrder;
        }

        public int getTotalBuyOrder() {
            return totalBuyOrder;
        }

        public void setTotalBuyOrder(int totalBuyOrder) {
            this.totalBuyOrder = totalBuyOrder;
        }

        public int getTotalSellOrder() {
            return totalSellOrder;
        }

        public void setTotalSellOrder(int totalSellOrder) {
            this.totalSellOrder = totalSellOrder;
        }

        public int getDaysBuyOrder() {
            return daysBuyOrder;
        }

        public void setDaysBuyOrder(int daysBuyOrder) {
            this.daysBuyOrder = daysBuyOrder;
        }

        public int getDaysSellOrder() {
            return daysSellOrder;
        }

        public void setDaysSellOrder(int daysSellOrder) {
            this.daysSellOrder = daysSellOrder;
        }


        public int getRealtimeBuyTakingTimes() {
            return realtimeBuyTakingTimes;
        }

        public void setRealtimeBuyTakingTimes(int realtimeBuyTakingTimes) {
            this.realtimeBuyTakingTimes = realtimeBuyTakingTimes;
        }

        public int getRealtimeSellTakingTimes() {
            return realtimeSellTakingTimes;
        }

        public void setRealtimeSellTakingTimes(int realtimeSellTakingTimes) {
            this.realtimeSellTakingTimes = realtimeSellTakingTimes;
        }

        public double getTotalBuyReward() {
            return totalBuyReward;
        }

        public void setTotalBuyReward(double totalBuyReward) {
            this.totalBuyReward = totalBuyReward;
        }

        public double getTotalSellReward() {
            return totalSellReward;
        }

        public void setTotalSellReward(double totalSellReward) {
            this.totalSellReward = totalSellReward;
        }

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

        public double getRealtimeBuyReward() {
            return realtimeBuyReward;
        }

        public void setRealtimeBuyReward(double realtimeBuyReward) {
            this.realtimeBuyReward = realtimeBuyReward;
        }

        public double getRealtimeSellReward() {
            return realtimeSellReward;
        }

        public void setRealtimeSellReward(double realtimeSellReward) {
            this.realtimeSellReward = realtimeSellReward;
        }

        public double getNoSettledReward() {
            return noSettledReward;
        }

        public void setNoSettledReward(double noSettledReward) {
            this.noSettledReward = noSettledReward;
        }

        public double getSettledReward() {
            return settledReward;
        }

        public void setSettledReward(double settledReward) {
            this.settledReward = settledReward;
        }

        public int getAppealFailTimes() {
            return appealFailTimes;
        }

        public void setAppealFailTimes(int appealFailTimes) {
            this.appealFailTimes = appealFailTimes;
        }

        public int getAppealTimes() {
            return appealTimes;
        }

        public void setAppealTimes(int appealTimes) {
            this.appealTimes = appealTimes;
        }

        public int getDaysAppealFailTimes() {
            return daysAppealFailTimes;
        }

        public void setDaysAppealFailTimes(int daysAppealFailTimes) {
            this.daysAppealFailTimes = daysAppealFailTimes;
        }

        public int getDaysAppealTimes() {
            return daysAppealTimes;
        }

        public void setDaysAppealTimes(int daysAppealTimes) {
            this.daysAppealTimes = daysAppealTimes;
        }

        public int getHurryTimes() {
            return hurryTimes;
        }

        public void setHurryTimes(int hurryTimes) {
            this.hurryTimes = hurryTimes;
        }

        public int getDaysHurryTimes() {
            return daysHurryTimes;
        }

        public void setDaysHurryTimes(int daysHurryTimes) {
            this.daysHurryTimes = daysHurryTimes;
        }

        public int getOrderFailResponsibleTimes() {
            return orderFailResponsibleTimes;
        }

        public void setOrderFailResponsibleTimes(int orderFailResponsibleTimes) {
            this.orderFailResponsibleTimes = orderFailResponsibleTimes;
        }

        public int getDaysOrderFailResponsibleTimes() {
            return daysOrderFailResponsibleTimes;
        }

        public void setDaysOrderFailResponsibleTimes(int daysOrderFailResponsibleTimes) {
            this.daysOrderFailResponsibleTimes = daysOrderFailResponsibleTimes;
        }

        public Object getAvgReleaseTime() {
            return avgReleaseTime;
        }

        public void setAvgReleaseTime(Object avgReleaseTime) {
            this.avgReleaseTime = avgReleaseTime;
        }

        public Object getAvgPayTime() {
            return avgPayTime;
        }

        public void setAvgPayTime(Object avgPayTime) {
            this.avgPayTime = avgPayTime;
        }

        public int getRangeTimeOrder() {
            return rangeTimeOrder;
        }

        public void setRangeTimeOrder(int rangeTimeOrder) {
            this.rangeTimeOrder = rangeTimeOrder;
        }

        public int getRangeTimeSuccessOrder() {
            return rangeTimeSuccessOrder;
        }

        public void setRangeTimeSuccessOrder(int rangeTimeSuccessOrder) {
            this.rangeTimeSuccessOrder = rangeTimeSuccessOrder;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getTradingDay() {
            return tradingDay;
        }

        public void setTradingDay(int tradingDay) {
            this.tradingDay = tradingDay;
        }
    }
}
