package com.spark.modulebase.entity;

import java.io.Serializable;

/**
 * 全局用到的实体类
 */

public class User implements Serializable {
    private String username;
    private String realName;
    private boolean isLogin;
    private String gtc;
    private String avatar;
    private int realNameStatus;
    private int fundsVerified;
    private String mobilePhone;
    private String promotionCode;

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public void setRealNameStatus(int realNameStatus) {
        this.realNameStatus = realNameStatus;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public int getFundsVerified() {
        return fundsVerified;
    }

    public void setFundsVerified(int fundsVerified) {
        this.fundsVerified = fundsVerified;
    }

    public int getRealNameStatus() {
        return realNameStatus;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGtc() {
        return gtc;
    }

    public void setGtc(String gtc) {
        this.gtc = gtc;
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

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
