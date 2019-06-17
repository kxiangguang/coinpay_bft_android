package com.spark.coinpay.overtime_order;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.OrderModel;

import java.util.HashMap;
import java.util.List;

/**
 * 超时单
 */

public class OrerTimeOrderPresenterImpl implements OverTimeOrderContract.MyOrderPresenter {

    private OverTimeOrderContract.MyOrderView myOrderView;
    private OrderModel orderModel;

    public OrerTimeOrderPresenterImpl(OverTimeOrderContract.MyOrderView myOrderView) {
        this.myOrderView = myOrderView;
        orderModel = new OrderModel();
    }

    @Override
    public void getOrder(boolean isShow, HashMap<String, String> params) {
        if (isShow)
            showLoading();
        orderModel.findOrderIng(params, new ResponseCallBack.SuccessListener<List<OrderInTransit>>() {
            @Override
            public void onResponse(List<OrderInTransit> list) {
                hideLoading();
                if (myOrderView != null) {
                    myOrderView.getOrderSuccess(list);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (myOrderView != null) {
                    myOrderView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (myOrderView != null) {
                    myOrderView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void releaseOrder(String businessId, String passWord) {
        showLoading();
        orderModel.ensureOrderRelease(businessId, passWord, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (myOrderView != null) {
                    myOrderView.releaseOrderSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (myOrderView != null) {
                    myOrderView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (myOrderView != null) {
                    myOrderView.dealError(volleyError);
                }
            }
        });
    }


    @Override
    public void showLoading() {
        if (myOrderView != null)
            myOrderView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (myOrderView != null)
            myOrderView.hideLoading();
    }

    @Override
    public void destory() {
        myOrderView = null;
    }
}
