package com.spark.coinpay.my.safe;


import com.spark.modulebase.base.BaseContract;
import com.spark.moduleuc.entity.SafeSetting;

/**
 * 安全中心
 */

public interface SafeContract {
    interface SafeView extends BaseContract.BaseView {
        void safeSettingSuccess(SafeSetting obj);
    }

    interface SafePresenter extends BaseContract.BasePresenter {
        void safeSetting();
    }
}
