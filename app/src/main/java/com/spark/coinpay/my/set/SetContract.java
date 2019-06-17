package com.spark.coinpay.my.set;

import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.User;

/**
 * 设置中心
 */

public interface SetContract {
    interface SetView extends BaseContract.BaseView {

        void loginOutSuccess(String obj);

        void getAutoReleaseSuccess(String obj);

        void updateAutoReleaseSuccess(MessageResult obj);

        void getUserInfoSuccess(User user);

    }

    interface SetPresenter extends BaseContract.BasePresenter {

        void loginOut();

        void getAutoRelease();

        void updateAutoRelease(int autoReleaseSwitch);

        void getUserInfo();

    }
}
