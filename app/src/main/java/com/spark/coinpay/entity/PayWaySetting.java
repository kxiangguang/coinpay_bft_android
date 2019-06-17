package com.spark.coinpay.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class PayWaySetting implements Serializable {

    /**
     * id : 25
     * memberId : 59
     * payType : BANK
     * payAddress : 45879361
     * realName : null
     * qrCodeUrl : null
     * urlMd5 : null
     * bank : 广发银行
     * branch : null
     * status : 1
     * createTime : 1552639659000
     * updateTime : 1552639659000
     * payNotes : null
     */

    private Long id;
    private int memberId;
    private String payType;
    private String payAddress;
    private String realName;
    private String qrCodeUrl;
    private Object urlMd5;
    private String bank;
    private String branch;
    private int status;
    private long createTime;
    private long updateTime;
    private String payNotes;
    private double dayLimit;
    private String expandField;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getExpandField() {
        return expandField;
    }

    public void setExpandField(String expandField) {
        this.expandField = expandField;
    }

    public double getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(double dayLimit) {
        this.dayLimit = dayLimit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayAddress() {
        return payAddress;
    }

    public void setPayAddress(String payAddress) {
        this.payAddress = payAddress;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }


    public Object getUrlMd5() {
        return urlMd5;
    }

    public void setUrlMd5(Object urlMd5) {
        this.urlMd5 = urlMd5;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getPayNotes() {
        return payNotes;
    }

    public void setPayNotes(String payNotes) {
        this.payNotes = payNotes;
    }
}
