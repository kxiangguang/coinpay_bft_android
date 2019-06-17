package com.spark.coinpay.pay;

import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultAcceptMerchantTrade;
import com.spark.library.acp.model.MessageResultPageOrderInTransit;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulelogin.entity.CasLoginEntity;

/**
 * 支付界面
 */

public class PayContract {
    interface PayView extends BaseContract.BaseView {

        void payCompleteSuccess(MessageResult response);

    }

    interface PayPresenter extends BaseContract.BasePresenter {

        void payComplete(String actualPayment, String orderNo, String tradePassword);

    }
}
