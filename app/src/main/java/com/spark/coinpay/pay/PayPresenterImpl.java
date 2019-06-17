package com.spark.coinpay.pay;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultAcceptMerchantTrade;
import com.spark.library.acp.model.MessageResultPageOrderInTransit;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulelogin.entity.CasLoginEntity;
import com.spark.moduleotc.model.OrderModel;

/**
 * 主界面
 */

public class PayPresenterImpl implements PayContract.PayPresenter {
    private PayContract.PayView payView;
    private OrderModel orderModel;

    public PayPresenterImpl(PayContract.PayView PayView) {
        this.payView = PayView;
        orderModel = new OrderModel();
    }


    @Override
    public void showLoading() {
        if (payView != null)
            payView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (payView != null)
            payView.hideLoading();
    }

    @Override
    public void destory() {
        payView = null;
    }

    /**
     * 确认订单支付完成
     */
    public void payComplete(String actualPayment, String orderNo, String tradePassword) {
        showLoading();
        orderModel.payComplete(actualPayment, orderNo, tradePassword, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (payView != null) {
                    payView.payCompleteSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (payView != null) {
                    payView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (payView != null) {
                    payView.dealError(volleyError);
                }
            }
        });
    }
}
