package com.spark.coinpay.my.appeal;

import com.spark.library.acp.model.AppealApplyAchiveVo;
import com.spark.library.acp.model.AppealApplyInTransitVo;
import com.spark.modulebase.base.BaseContract;

import java.util.HashMap;
import java.util.List;

/**
 * 纠纷管理
 */
public interface AppealContract {
    interface AppealView extends BaseContract.BaseView {
        void getOrderSuccess(List<AppealApplyInTransitVo> list);
    }

    interface AppealPresenter extends BaseContract.BasePresenter {
        void getOrder(boolean isShow, HashMap<String, String> params);
    }

    interface AppealFinishView extends BaseContract.BaseView {
        void getOrderSuccess(List<AppealApplyAchiveVo> list);
    }

    interface AppealFinishPresenter extends BaseContract.BasePresenter {
        void getOrder(boolean isShow, HashMap<String, String> params);
    }
}
