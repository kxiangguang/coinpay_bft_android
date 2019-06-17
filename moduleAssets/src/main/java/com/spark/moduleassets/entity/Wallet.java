package com.spark.moduleassets.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/9/18 0018.
 */

public class Wallet implements Serializable {


    /**
     * address : 0x0a098be61e00648a4a81c0d75c7b47ad32fc2459
     * balance : 30179.0
     * frozenBalance : -133.0
     * isLock : 1
     * memberId : 12565
     * coinId : GUSD
     * cnyRate : 6.8649
     * usdRate : 1.0
     * canAutoWithdraw : 1
     * canRecharge : 1
     * canTransfer : null
     * canWithdraw : 1
     * minRechargeAmount : 10.0
     * rateMap : {"KRW":1130.7,"JPY":113.8,"EUR":0.8594,"USD":1,"PHP":53.456,"CNY":6.8649}
     */

    private String address;
    private double balance;
    private String coinId;
    private double frozenBalance;
    private Long id;
    private Integer isLock;
    private double legalRate;
    private String localCurrency;
    private Long memberId;
    private double platformAssetRate;
    private double totalLegalAssetBalance;
    private double totalPlatformAssetBalance;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public double getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(double frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    public double getLegalRate() {
        return legalRate;
    }

    public void setLegalRate(double legalRate) {
        this.legalRate = legalRate;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public void setLocalCurrency(String localCurrency) {
        this.localCurrency = localCurrency;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public double getPlatformAssetRate() {
        return platformAssetRate;
    }

    public void setPlatformAssetRate(double platformAssetRate) {
        this.platformAssetRate = platformAssetRate;
    }

    public double getTotalLegalAssetBalance() {
        return totalLegalAssetBalance;
    }

    public void setTotalLegalAssetBalance(double totalLegalAssetBalance) {
        this.totalLegalAssetBalance = totalLegalAssetBalance;
    }

    public double getTotalPlatformAssetBalance() {
        return totalPlatformAssetBalance;
    }

    public void setTotalPlatformAssetBalance(double totalPlatformAssetBalance) {
        this.totalPlatformAssetBalance = totalPlatformAssetBalance;
    }
}
