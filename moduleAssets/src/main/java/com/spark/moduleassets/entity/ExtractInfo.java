package com.spark.moduleassets.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 提币信息
 */
public class ExtractInfo {
    @SerializedName("coinName")
    private String coinName ;
    @SerializedName("coinType")
    private String coinType ;
    @SerializedName("dailyMaxWithdrawAmount")
    private double dailyMaxWithdrawAmount ;
    @SerializedName("depositFee")
    private double depositFee ;
    @SerializedName("depositFeeType")
    private Integer depositFeeType ;
    @SerializedName("enableAutoWithdraw")
    private Integer enableAutoWithdraw ;
    @SerializedName("enableGenerate")
    private Integer enableGenerate ;
    @SerializedName("enableWithdraw")
    private Integer enableWithdraw ;
    @SerializedName("iconUrl")
    private String iconUrl ;
    @SerializedName("isPlatformCoin")
    private Integer isPlatformCoin ;
    @SerializedName("mainCoinType")
    private String mainCoinType ;
    @SerializedName("maxDepositFee")
    private double maxDepositFee ;
    @SerializedName("maxWithdrawAmount")
    private double maxWithdrawAmount ;
    @SerializedName("maxWithdrawFee")
    private double maxWithdrawFee ;
    @SerializedName("minDepositAmount")
    private double minDepositAmount ;
    @SerializedName("minDepositFee")
    private double minDepositFee ;
    @SerializedName("minWithdrawAmount")
    private double minWithdrawAmount ;
    @SerializedName("minWithdrawFee")
    private double minWithdrawFee ;
    @SerializedName("nameCn")
    private String nameCn ;
    @SerializedName("rpcType")
    private Integer rpcType ;
    @SerializedName("scale")
    private Integer scale ;
    @SerializedName("sort")
    private Integer sort ;
    @SerializedName("status")
    private Integer status ;
    @SerializedName("unit")
    private String unit ;
    @SerializedName("withdrawFee")
    private double withdrawFee ;
    @SerializedName("withdrawFeeType")
    private Integer withdrawFeeType ;
    @SerializedName("withdrawThreshold")
    private double withdrawThreshold ;

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public double getDailyMaxWithdrawAmount() {
        return dailyMaxWithdrawAmount;
    }

    public void setDailyMaxWithdrawAmount(double dailyMaxWithdrawAmount) {
        this.dailyMaxWithdrawAmount = dailyMaxWithdrawAmount;
    }

    public double getDepositFee() {
        return depositFee;
    }

    public void setDepositFee(double depositFee) {
        this.depositFee = depositFee;
    }

    public Integer getDepositFeeType() {
        return depositFeeType;
    }

    public void setDepositFeeType(Integer depositFeeType) {
        this.depositFeeType = depositFeeType;
    }

    public Integer getEnableAutoWithdraw() {
        return enableAutoWithdraw;
    }

    public void setEnableAutoWithdraw(Integer enableAutoWithdraw) {
        this.enableAutoWithdraw = enableAutoWithdraw;
    }

    public Integer getEnableGenerate() {
        return enableGenerate;
    }

    public void setEnableGenerate(Integer enableGenerate) {
        this.enableGenerate = enableGenerate;
    }

    public Integer getEnableWithdraw() {
        return enableWithdraw;
    }

    public void setEnableWithdraw(Integer enableWithdraw) {
        this.enableWithdraw = enableWithdraw;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getIsPlatformCoin() {
        return isPlatformCoin;
    }

    public void setIsPlatformCoin(Integer isPlatformCoin) {
        this.isPlatformCoin = isPlatformCoin;
    }

    public String getMainCoinType() {
        return mainCoinType;
    }

    public void setMainCoinType(String mainCoinType) {
        this.mainCoinType = mainCoinType;
    }

    public double getMaxDepositFee() {
        return maxDepositFee;
    }

    public void setMaxDepositFee(double maxDepositFee) {
        this.maxDepositFee = maxDepositFee;
    }

    public double getMaxWithdrawAmount() {
        return maxWithdrawAmount;
    }

    public void setMaxWithdrawAmount(double maxWithdrawAmount) {
        this.maxWithdrawAmount = maxWithdrawAmount;
    }

    public double getMaxWithdrawFee() {
        return maxWithdrawFee;
    }

    public void setMaxWithdrawFee(double maxWithdrawFee) {
        this.maxWithdrawFee = maxWithdrawFee;
    }

    public double getMinDepositAmount() {
        return minDepositAmount;
    }

    public void setMinDepositAmount(double minDepositAmount) {
        this.minDepositAmount = minDepositAmount;
    }

    public double getMinDepositFee() {
        return minDepositFee;
    }

    public void setMinDepositFee(double minDepositFee) {
        this.minDepositFee = minDepositFee;
    }

    public double getMinWithdrawAmount() {
        return minWithdrawAmount;
    }

    public void setMinWithdrawAmount(double minWithdrawAmount) {
        this.minWithdrawAmount = minWithdrawAmount;
    }

    public double getMinWithdrawFee() {
        return minWithdrawFee;
    }

    public void setMinWithdrawFee(double minWithdrawFee) {
        this.minWithdrawFee = minWithdrawFee;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public Integer getRpcType() {
        return rpcType;
    }

    public void setRpcType(Integer rpcType) {
        this.rpcType = rpcType;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(double withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public Integer getWithdrawFeeType() {
        return withdrawFeeType;
    }

    public void setWithdrawFeeType(Integer withdrawFeeType) {
        this.withdrawFeeType = withdrawFeeType;
    }

    public double getWithdrawThreshold() {
        return withdrawThreshold;
    }

    public void setWithdrawThreshold(double withdrawThreshold) {
        this.withdrawThreshold = withdrawThreshold;
    }
}
