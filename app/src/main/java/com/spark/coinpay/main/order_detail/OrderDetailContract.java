package com.spark.coinpay.main.order_detail;

import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultAcceptMerchantTrade;
import com.spark.library.acp.model.MessageResultPageOrderInTransit;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulelogin.entity.CasLoginEntity;

import java.util.HashMap;
import java.util.List;

/**
 * 订单详情
 */

public class OrderDetailContract {
    interface View extends BaseContract.BaseView {

        void cancelOrderSuccess(MessageResult response);

        void releaseOrderSuccess(MessageResult response);

    }

    interface Presenter extends BaseContract.BasePresenter {

        void cancelOrder(String businessId, String tradePassword);

        void ensureOrderRelease(String businessId, String tradePassword);

    }
}
