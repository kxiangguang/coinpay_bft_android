package com.spark.coinpay.my.about;


import com.spark.modulebase.base.BaseContract;

/**
 * 关于我们
 */

public interface AboutContract {
    interface AboutView extends BaseContract.BaseView {

        void checkVersionSuccess(String obj);

    }

    interface AboutPresenter extends BaseContract.BasePresenter {

        void checkVersion();

    }
}
