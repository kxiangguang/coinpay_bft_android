package com.spark.coinpay.entity;

/**
 * Api地址实体
 */

public class ApiAddressEntity {
    private String appName;
    private String apiAddress;

    public ApiAddressEntity() {
    }

    public ApiAddressEntity(String appName, String apiAddress) {
        this.appName = appName;
        this.apiAddress = apiAddress;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getApiAddress() {
        return apiAddress;
    }

    public void setApiAddress(String apiAddress) {
        this.apiAddress = apiAddress;
    }
}
