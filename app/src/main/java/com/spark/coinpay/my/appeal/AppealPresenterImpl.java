package com.spark.coinpay.my.appeal;

import com.android.volley.VolleyError;
import com.spark.coinpay.my.order.MyOrderContract.MyOrderPresenter;
import com.spark.library.acp.model.AppealApplyInTransitVo;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.AppealApplyControllerModel;
import com.spark.moduleotc.model.OrderModel;

import java.util.HashMap;
import java.util.List;

/**
 * 纠纷管理
 */
public class AppealPresenterImpl implements AppealContract.AppealPresenter {

    private AppealContract.AppealView myOrderView;
    private AppealApplyControllerModel orderModel;

    public AppealPresenterImpl(AppealContract.AppealView myOrderView) {
        this.myOrderView = myOrderView;
        orderModel = new AppealApplyControllerModel();
    }

    @Override
    public void getOrder(boolean isShow, HashMap<String, String> params) {
        if (isShow)
            showLoading();
        orderModel.findOrderIng(params, new ResponseCallBack.SuccessListener<List<AppealApplyInTransitVo>>() {
            @Override
            public void onResponse(List<AppealApplyInTransitVo> list) {
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
