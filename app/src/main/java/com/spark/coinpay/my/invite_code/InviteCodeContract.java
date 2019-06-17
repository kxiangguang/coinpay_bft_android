package com.spark.coinpay.my.invite_code;


import com.spark.modulebase.base.BaseContract;

/**
 * 设置/修改交易密码
 */

public interface InviteCodeContract {
    interface View extends BaseContract.BaseView {

        void accountPwdSuccess(String obj);

    }

    interface Presenter extends BaseContract.BasePresenter {
        void accountPwd(String tradePassword);

    }


}
