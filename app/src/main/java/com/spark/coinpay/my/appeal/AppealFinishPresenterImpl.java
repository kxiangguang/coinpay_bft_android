package com.spark.coinpay.my.appeal;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.AppealApplyAchiveVo;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.AppealApplyControllerModel;

import java.util.HashMap;
import java.util.List;

/**
 * 纠纷管理
 */

public class AppealFinishPresenterImpl implements AppealContract.AppealFinishPresenter {

    private AppealContract.AppealFinishView myOrderFinishView;
    private AppealApplyControllerModel orderModel;

    public AppealFinishPresenterImpl(AppealContract.AppealFinishView MyOrderFinishView) {
        this.myOrderFinishView = MyOrderFinishView;
        orderModel = new AppealApplyControllerModel();
    }

    @Override
    public void getOrder(boolean isShow, HashMap<String, String> params) {
        if (isShow)
            showLoading();
        orderModel.findOrderFinish(params, new ResponseCallBack.SuccessListener<List<AppealApplyAchiveVo>>() {
            @Override
            public void onResponse(List<AppealApplyAchiveVo> list) {
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
