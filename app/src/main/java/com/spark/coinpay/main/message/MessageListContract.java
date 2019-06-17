package com.spark.coinpay.main.message;

import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.modulebase.base.BaseContract;

import java.util.HashMap;
import java.util.List;

/**
 * 消息中心
 */

public interface MessageListContract {
    interface MessageListView extends BaseContract.BaseView {
        void getOrderSuccess(List<OrderInTransit> list);

    }

    interface MessageListPresenter extends BaseContract.BasePresenter {
        void getOrder(boolean isShow, HashMap<String, String> params);

    }

}
