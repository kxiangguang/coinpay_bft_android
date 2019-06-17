package com.spark.coinpay.main.buy;

import com.android.volley.VolleyError;
import com.spark.coinpay.pay.PayContract;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.OneKeyBuyControllerModel;
import com.spark.moduleotc.model.OrderModel;

/**
 * 主界面
 */

public class BuyPayFinishPresenterImpl implements BuyPayFinishContract.PayPresenter {
    private BuyPayFinishContract.PayView payView;
    private OneKeyBuyControllerModel oneKeyBuyControllerModel;

    public BuyPayFinishPresenterImpl(BuyPayFinishContract.PayView PayView) {
        this.payView = PayView;
        oneKeyBuyControllerModel = new OneKeyBuyControllerModel();
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
    public void payComplete(String actualPayment, String orderNo) {
        showLoading();
        oneKeyBuyControllerModel.oneKeyPayComplete(actualPayment, orderNo, new ResponseCallBack.SuccessListener<MessageResult>() {
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
