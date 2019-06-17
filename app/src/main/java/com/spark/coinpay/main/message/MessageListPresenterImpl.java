package com.spark.coinpay.main.message;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.OrderModel;

import java.util.HashMap;
import java.util.List;

/**
 * 消息中心
 */

public class MessageListPresenterImpl implements MessageListContract.MessageListPresenter {

    private MessageListContract.MessageListView messageListView;
    private OrderModel orderModel;

    public MessageListPresenterImpl(MessageListContract.MessageListView MessageListView) {
        this.messageListView = MessageListView;
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
                if (messageListView != null) {
                    messageListView.getOrderSuccess(list);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (messageListView != null) {
                    messageListView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (messageListView != null) {
                    messageListView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void showLoading() {
        if (messageListView != null)
            messageListView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (messageListView != null)
            messageListView.hideLoading();
    }

    @Override
    public void destory() {
        messageListView = null;
    }
}
