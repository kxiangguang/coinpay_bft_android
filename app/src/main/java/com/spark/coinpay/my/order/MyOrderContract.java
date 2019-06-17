package com.spark.coinpay.my.order;

import com.spark.library.acp.model.OrderAchive;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.modulebase.base.BaseContract;

import java.util.HashMap;
import java.util.List;

/**
 * 我的订单
 */

public interface MyOrderContract {
    interface MyOrderView extends BaseContract.BaseView {
        void getOrderSuccess(List<OrderInTransit> list);
    }

    interface MyOrderPresenter extends BaseContract.BasePresenter {
        void getOrder(boolean isShow, HashMap<String, String> params);
    }

    interface MyOrderFinishView extends BaseContract.BaseView {
        void getOrderSuccess(List<OrderAchive> list);
    }

    interface MyOrderFinishPresenter extends BaseContract.BasePresenter {
        void getOrder(boolean isShow, HashMap<String, String> params);
    }
}
