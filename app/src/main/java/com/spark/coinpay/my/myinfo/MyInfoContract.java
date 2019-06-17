package com.spark.coinpay.my.myinfo;


import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.User;
import com.spark.moduleuc.entity.SafeSetting;

/**
 * 账户设置
 */

public interface MyInfoContract {
    interface MyInfoView extends BaseContract.BaseView {

        void uploadBase64PicSuccess(String obj);

        void avatarSuccess(String obj);

        void findAuthenticationStatusSuccess(String response);

        void getUserInfoSuccess(User user);
    }

    interface MyInfoPresenter extends BaseContract.BasePresenter {

        void uploadBase64Pic(String strBase64Data);

        void avatar(String url);

        void findAuthenticationStatus();

        void getUserInfo();
    }
}
