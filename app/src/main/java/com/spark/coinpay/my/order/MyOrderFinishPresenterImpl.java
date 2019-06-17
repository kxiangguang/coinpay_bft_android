package com.spark.coinpay.my.order;

import com.android.volley.VolleyError;
import com.spark.coinpay.my.order.MyOrderContract.MyOrderPresenter;
import com.spark.library.acp.model.OrderAchive;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.OrderModel;

import java.util.HashMap;
import java.util.List;

/**
 * 我的订单-完成、取消
 */

public class MyOrderFinishPresenterImpl implements MyOrderContract.MyOrderPresenter {

    private MyOrderContract.MyOrderFinishView myOrderFinishView;
    private OrderModel orderModel;

    public MyOrderFinishPresenterImpl(MyOrderContract.MyOrderFinishView MyOrderFinishView) {
        this.myOrderFinishView = MyOrderFinishView;
        orderModel = new OrderModel();
    }

    @Override
    public void getOrder(boolean isShow, HashMap<String, String> params) {
        if (isShow)
            showLoading();
        orderModel.findOrderFinish(params, new ResponseCallBack.SuccessListener<List<OrderAchive>>() {
            @Override
            public void onResponse(List<OrderAchive> list) {
                hideLoading();
                if (myOrderFinishView != null) {
                    myOrderFinishView.getOrderSuccess(list);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (myOrderFinishView != null) {
                    myOrderFinishView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (myOrderFinishView != null) {
                    myOrderFinishView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void showLoading() {
        if (myOrderFinishView != null)
            myOrderFinishView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (myOrderFinishView != null)
            myOrderFinishView.hideLoading();
    }

    @Override
    public void destory() {
        myOrderFinishView = null;
    }
}
