package com.spark.coinpay.main.buy;

import java.io.Serializable;

/**
 * 创建订单成功返回的数据
 */

public class PayEntity implements Serializable {

    /**
     * orderType : 0
     * releaseTime : null
     * orderSn : 192389373060759552
     * payTime : null
     * remark : null
     * localCurrency : CNY
     * advertiseId : 149
     * number : 10
     * actualPayment : null
     * partitionKey : null
     * payRefer : 632438
     * price : 5
     * customerId : 132
     * commission : 0.1
     * id : 280
     * tradeType : 1
     * memberId : 113
     * address : 12212
     * payMode : alipay,wechat,paypal
     * timeLimit : 30
     * money : 50
     * acceptanceMemberId : 96
     * createTime : 1559741200957
     * cancelTime : null
     * coinName : CNT
     * payData : [{"createTime":1559614384000,"id":82,"memberId":113,"payAddress":"324324","payType":"paypal","status":1,"updateTime":1559614384000},{"createTime":1559615201000,"id":83,"memberId":113,"payAddress":"15285","payType":"wechat","qrCodeUrl":"http://nbtconline.oss-ap-northeast-1.aliyuncs.com/2019/06/04/59efffab-d254-41cb-8ed5-dd6da8bba1f5.png","status":1,"updateTime":1559615201000},{"createTime":1559615390000,"id":84,"memberId":113,"payAddress":"7171","payType":"alipay","qrCodeUrl":"http://nbtconline.oss-ap-northeast-1.aliyuncs.com/2019/06/04/b5cb68b4-41e1-419a-978a-29b6ca624dfd.png","status":1,"updateTime":1559615390000}]
     * status : 1
     */

    private String orderType;
    private Object releaseTime;
    private String orderSn;
    private Object payTime;
    private Object remark;
    private String localCurrency;
    private long advertiseId;
    private double number;
    private Object actualPayment;
    private Object partitionKey;
    private String payRefer;
    private double price;
    private long customerId;
    private double commission;
    private long id;
    private int tradeType;
    private long memberId;
    private String address;
    private String payMode;
    private double timeLimit;
    private double money;
    private long acceptanceMemberId;
    private long createTime;
    private Object cancelTime;
    private String coinName;
    private String payData;
    private int status;
    private String tradeToUsername;

    public String getTradeToUsername() {
        return tradeToUsername;
    }

    public void setTradeToUsername(String tradeToUsername) {
        this.tradeToUsername = tradeToUsername;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Object getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Object releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Object getPayTime() {
        return payTime;
    }

    public void setPayTime(Object payTime) {
        this.payTime = payTime;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public void setLocalCurrency(String localCurrency) {
        this.localCurrency = localCurrency;
    }

    public long getAdvertiseId() {
        return advertiseId;
    }

    public void setAdvertiseId(long advertiseId) {
        this.advertiseId = advertiseId;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public Object getActualPayment() {
        return actualPayment;
    }

    public void setActualPayment(Object actualPayment) {
        this.actualPayment = actualPayment;
    }

    public Object getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(Object partitionKey) {
        this.partitionKey = partitionKey;
    }

    public String getPayRefer() {
        return payRefer;
    }

    public void setPayRefer(String payRefer) {
        this.payRefer = payRefer;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public double getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(double timeLimit) {
        this.timeLimit = timeLimit;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Object getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Object cancelTime) {
        this.cancelTime = cancelTime;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
