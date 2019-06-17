package com.spark.coinpay.main.order_detail;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.OrderModel;

/**
 * 订单详情
 */

public class OrderDetailPresenterImpl implements OrderDetailContract.Presenter {
    private OrderDetailContract.View orderView;
    private OrderModel orderModel;

    public OrderDetailPresenterImpl(OrderDetailContract.View mainView) {
        this.orderView = mainView;
        orderModel = new OrderModel();
    }

    @Override
    public void ensureOrderRelease(String businessId, String passWord) {
        showLoading();
        orderModel.ensureOrderRelease(businessId, passWord, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (orderView != null) {
                    orderView.releaseOrderSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (orderView != null) {
                    orderView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (orderView != null) {
                    orderView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void cancelOrder(String businessId, String tradePassword) {
        showLoading();
        orderModel.orderRefuse(businessId, tradePassword, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (orderView != null) {
                    orderView.cancelOrderSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (orderView != null)
                    orderView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (orderView != null)
                    orderView.dealError(volleyError);
            }
        });
    }


    @Override
    public void showLoading() {
        if (orderView != null)
            orderView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (orderView != null)
            orderView.hideLoading();
    }

    @Override
    public void destory() {
        orderView = null;
    }
}
