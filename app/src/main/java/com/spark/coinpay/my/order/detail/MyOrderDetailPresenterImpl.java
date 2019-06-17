package com.spark.coinpay.my.order.detail;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.OrderModel;

/**
 * 我的订单详情
 */

public class MyOrderDetailPresenterImpl implements MyOrderDetailContract.MyOrderDetailIngPresenter {
    private MyOrderDetailContract.MyOrderDetailIngView myOrderDetailIngView;
    private OrderModel orderModel;

    public MyOrderDetailPresenterImpl(MyOrderDetailContract.MyOrderDetailIngView MyOrderDetailIngView) {
        this.myOrderDetailIngView = MyOrderDetailIngView;
        orderModel = new OrderModel();
    }

    @Override
    public void releaseOrder(String businessId, String passWord) {
        showLoading();
        orderModel.ensureOrderRelease(businessId, passWord, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (myOrderDetailIngView != null) {
                    myOrderDetailIngView.releaseOrderSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (myOrderDetailIngView != null) {
                    myOrderDetailIngView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (myOrderDetailIngView != null) {
                    myOrderDetailIngView.dealError(volleyError);
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
                if (myOrderDetailIngView != null) {
                    myOrderDetailIngView.cancelOrderSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (myOrderDetailIngView != null)
                    myOrderDetailIngView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (myOrderDetailIngView != null)
                    myOrderDetailIngView.dealError(volleyError);
            }
        });
    }


    @Override
    public void showLoading() {
        if (myOrderDetailIngView != null)
            myOrderDetailIngView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (myOrderDetailIngView != null)
            myOrderDetailIngView.hideLoading();
    }

    @Override
    public void destory() {
        myOrderDetailIngView = null;
    }
}
