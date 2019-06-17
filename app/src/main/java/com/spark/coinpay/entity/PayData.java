package com.spark.coinpay.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/3/21 0021.
 */

public class PayData implements Serializable {

    /**
     * bank : 中国农业银行
     * payAddress : 6325189465619
     * payType : BANK
     * realName : 飞龙
     */

    private String bank;
    private String payAddress;
    private String payType;
    private String realName;
    private String qrCodeUrl;
    private String branch;

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getPayAddress() {
        return payAddress;
    }

    public void setPayAddress(String payAddress) {
        this.payAddress = payAddress;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
