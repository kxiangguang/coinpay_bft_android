package com.spark.coinpay.overtime_order;

import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OrderAchive;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.modulebase.base.BaseContract;

import java.util.HashMap;
import java.util.List;

/**
 * 超时单
 */

public interface OverTimeOrderContract {
    interface MyOrderView extends BaseContract.BaseView {
        void getOrderSuccess(List<OrderInTransit> list);

        void releaseOrderSuccess(MessageResult response);
    }

    interface MyOrderPresenter extends BaseContract.BasePresenter {
        void getOrder(boolean isShow, HashMap<String, String> params);

        void releaseOrder(String businessId, String tradePassword);
    }

}
