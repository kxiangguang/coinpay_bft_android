package com.spark.coinpay.main.buy.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2019/6/6 0006.
 */

public class BuyOrderEntity implements Serializable {

    /**
     * total : 4.0
     * current : 1.0
     * pages : 1.0
     * size : 10.0
     * records : [{"orderType":"0","orderSn":"192667079270776832","payTime":1.55980751E12,"payMode":"alipay,wechat,paypal","tradeToUsername":"234234","localCurrency":"CNY","advertiseId":149,"number":0.26,"actualPayment":"alipay","money":1.3,"acceptanceMemberId":77,"payRefer":"1043848","createTime":1.559807411E12,"price":5,"customerId":132,"commission":0.0026,"coinName":"CNT","payData":"[{\"createTime\":1559615390000,\"id\":84,\"memberId\":113,\"payAddress\":\"7171\",\"payType\":\"alipay\",\"qrCodeUrl\":\"http://nbtconline.oss-ap-northeast-1.aliyuncs.com/2019/06/04/b5cb68b4-41e1-419a-978a-29b6ca624dfd.png\",\"status\":1,\"updateTime\":1559615390000}]","tradeType":1,"memberId":113,"status":2},{"orderType":"0","orderSn":"192671185473421312","payTime":1.559808397E12,"payMode":"alipay,wechat,paypal","tradeToUsername":"234234","localCurrency":"CNY","advertiseId":149,"number":1,"actualPayment":"alipay","money":1,"acceptanceMemberId":77,"payRefer":"157318","createTime":1.55980839E12,"price":1,"customerId":132,"commission":0.01,"coinName":"CNT","payData":"[{\"createTime\":1559615390000,\"id\":84,\"memberId\":113,\"payAddress\":\"7171\",\"payType\":\"alipay\",\"qrCodeUrl\":\"http://nbtconline.oss-ap-northeast-1.aliyuncs.com/2019/06/04/b5cb68b4-41e1-419a-978a-29b6ca624dfd.png\",\"status\":1,\"updateTime\":1559615390000}]","tradeType":1,"memberId":113,"status":2},{"orderType":"0","orderSn":"192672319835521024","payTime":1.559808672E12,"payMode":"alipay,wechat,paypal","tradeToUsername":"234234","localCurrency":"CNY","advertiseId":149,"number":2,"actualPayment":"alipay","money":2,"acceptanceMemberId":77,"payRefer":"827063","createTime":1.559808661E12,"price":1,"customerId":132,"commission":0.02,"coinName":"CNT","payData":"[{\"createTime\":1559615390000,\"id\":84,\"memberId\":113,\"payAddress\":\"7171\",\"payType\":\"alipay\",\"qrCodeUrl\":\"http://nbtconline.oss-ap-northeast-1.aliyuncs.com/2019/06/04/b5cb68b4-41e1-419a-978a-29b6ca624dfd.png\",\"status\":1,\"updateTime\":1559615390000}]","tradeType":1,"memberId":113,"status":2},{"orderType":"0","orderSn":"192673225155067904","payTime":1.559808887E12,"payMode":"alipay,wechat,paypal","tradeToUsername":"234234","localCurrency":"CNY","advertiseId":149,"number":5,"actualPayment":"alipay","money":5,"acceptanceMemberId":77,"payRefer":"809854","createTime":1.559808877E12,"price":1,"customerId":132,"commission":0.05,"coinName":"CNT","payData":"[{\"createTime\":1559615390000,\"id\":84,\"memberId\":113,\"payAddress\":\"7171\",\"payType\":\"alipay\",\"qrCodeUrl\":\"http://nbtconline.oss-ap-northeast-1.aliyuncs.com/2019/06/04/b5cb68b4-41e1-419a-978a-29b6ca624dfd.png\",\"status\":1,\"updateTime\":1559615390000}]","tradeType":1,"memberId":113,"status":2}]
     */

    private double total;
    private double current;
    private double pages;
    private double size;
    private List<RecordsBean> records;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getPages() {
        return pages;
    }

    public void setPages(double pages) {
        this.pages = pages;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
        /**
         * orderType : 0
         * orderSn : 192667079270776832
         * payTime : 1.55980751E12
         * payMode : alipay,wechat,paypal
         * tradeToUsername : 234234
         * localCurrency : CNY
         * advertiseId : 149.0
         * number : 0.26
         * actualPayment : alipay
         * money : 1.3
         * acceptanceMemberId : 77.0
         * payRefer : 1043848
         * createTime : 1.559807411E12
         * price : 5.0
         * customerId : 132.0
         * commission : 0.0026
         * coinName : CNT
         * payData : [{"createTime":1559615390000,"id":84,"memberId":113,"payAddress":"7171","payType":"alipay","qrCodeUrl":"http://nbtconline.oss-ap-northeast-1.aliyuncs.com/2019/06/04/b5cb68b4-41e1-419a-978a-29b6ca624dfd.png","status":1,"updateTime":1559615390000}]
         * tradeType : 1.0
         * memberId : 113.0
         * status : 2.0
         */

        private String orderType;
        private String orderSn;
        private long payTime;
        private String payMode;
        private String tradeToUsername;
        private String localCurrency;
        private double advertiseId;
        private double number;
        private String actualPayment;
        private double money;
        private long acceptanceMemberId;
        private String payRefer;
        private long createTime;
        private double price;
        private long customerId;
        private double commission;
        private String coinName;
        private String payData;
        private double tradeType;
        private long memberId;
        private int status;
        private double timeLimit;

        public double getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(double timeLimit) {
            this.timeLimit = timeLimit;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
        }

        public long getPayTime() {
            return payTime;
        }

        public void setPayTime(long payTime) {
            this.payTime = payTime;
        }

        public String getPayMode() {
            return payMode;
        }

        public void setPayMode(String payMode) {
            this.payMode = payMode;
        }

        public String getTradeToUsername() {
            return tradeToUsername;
        }

        public void setTradeToUsername(String tradeToUsername) {
            this.tradeToUsername = tradeToUsername;
        }

        public String getLocalCurrency() {
            return localCurrency;
        }

        public void setLocalCurrency(String localCurrency) {
            this.localCurrency = localCurrency;
        }

        public double getAdvertiseId() {
            return advertiseId;
        }

        public void setAdvertiseId(double advertiseId) {
            this.advertiseId = advertiseId;
        }

        public double getNumber() {
            return number;
        }

        public void setNumber(double number) {
            this.number = number;
        }

        public String getActualPayment() {
            return actualPayment;
        }

        public void setActualPayment(String actualPayment) {
            this.actualPayment = actualPayment;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public long getAcceptanceMemberId() {
            return acceptanceMemberId;
        }

        public void setAcceptanceMemberId(long acceptanceMemberId) {
            this.acceptanceMemberId = acceptanceMemberId;
        }

        public String getPayRefer() {
            return payRefer;
        }

        public void setPayRefer(String payRefer) {
            this.payRefer = payRefer;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(long customerId) {
            this.customerId = customerId;
        }

        public double getCommission() {
            return commission;
        }

        public void setCommission(double commission) {
            this.commission = commission;
        }

        public String getCoinName() {
            return coinName;
        }

        public void setCoinName(String coinName) {
            this.coinName = coinName;
        }

        public String getPayData() {
            return payData;
        }

        public void setPayData(String payData) {
            this.payData = payData;
        }

        public double getTradeType() {
            return tradeType;
        }

        public void setTradeType(double tradeType) {
            this.tradeType = tradeType;
        }

        public long getMemberId() {
            return memberId;
        }

        public void setMemberId(long memberId) {
            this.memberId = memberId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
