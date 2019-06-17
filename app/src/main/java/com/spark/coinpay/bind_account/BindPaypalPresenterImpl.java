package com.spark.coinpay.bind_account;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.PayControllerModel;

/**
 * Created by Administrator on 2019/3/16 0016.
 */

public class BindPaypalPresenterImpl implements BindAccountContract.PaypalPresenter {

    private BindAccountContract.PaypalView paypalView;
    private PayControllerModel payControllerModel;

    public BindPaypalPresenterImpl(BindAccountContract.PaypalView paypalView) {
        this.paypalView = paypalView;
        payControllerModel = new PayControllerModel();
    }

    @Override
    public void showLoading() {
        if (paypalView != null) {
            paypalView.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (paypalView != null) {
            paypalView.hideLoading();
        }
    }

    @Override
    public void destory() {
        paypalView = null;
    }

    @Override
    public void doBindPaypal(String payType, String account, String bank, String branch, String pwd, String etWarn, String dayLimit) {
        showLoading();
        payControllerModel.doBindBank(payType, account, bank, branch, pwd, "", etWarn, dayLimit, "", "", "",
                new ResponseCallBack.SuccessListener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        hideLoading();
                        if (paypalView != null) {
                            paypalView.doBindPaypalSuccess(response);
                        }
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (paypalView != null) {
                            paypalView.dealError(httpErrorEntity);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (paypalView != null) {
                            paypalView.dealError(volleyError);
                        }
                    }
                });
    }

    @Override
    public void doUpdatePaypal(Long id, String payType, String account, String bank, String branch, String pwd, String etWarn, String dayLimit) {
        showLoading();
        payControllerModel.doUpdateBank(id, payType, account, bank, branch, pwd, "", etWarn, dayLimit, "", "", "",
                new ResponseCallBack.SuccessListener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        hideLoading();
                        if (paypalView != null) {
                            paypalView.doUpdatePaypalSuccess(response);
                        }
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (paypalView != null) {
                            paypalView.dealError(httpErrorEntity);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (paypalView != null) {
                            paypalView.dealError(volleyError);
                        }
                    }
                });
    }
}
