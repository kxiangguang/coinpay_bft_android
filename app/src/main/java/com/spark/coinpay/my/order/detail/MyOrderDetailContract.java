package com.spark.coinpay.my.order.detail;

import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultAcceptMerchantTrade;
import com.spark.library.acp.model.MessageResultPageOrderInTransit;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulelogin.entity.CasLoginEntity;

/**
 * 我的订单详情
 */

public class MyOrderDetailContract {
    public interface MyOrderDetailIngView extends BaseContract.BaseView {

        void releaseOrderSuccess(MessageResult response);

        void cancelOrderSuccess(MessageResult response);

    }

    interface MyOrderDetailIngPresenter extends BaseContract.BasePresenter {

        void releaseOrder(String businessId, String tradePassword);

        void cancelOrder(String businessId, String tradePassword);
    }
}
