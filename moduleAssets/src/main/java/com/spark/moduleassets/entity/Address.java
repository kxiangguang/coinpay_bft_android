package com.spark.moduleassets.entity;

import java.io.Serializable;

/**
 * 提币地址
 */

public class Address implements Serializable{
    private String remark;
    private String address;
    private String coinId;

    public String getAddress() {
        return address;
    }

    public String getRemark() {
        return remark;
    }

    public String getCoinId() {
        return coinId;
    }
}
