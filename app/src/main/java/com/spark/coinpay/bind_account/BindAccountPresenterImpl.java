package com.spark.coinpay.bind_account;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.PayControllerModel;

/**
 * Created by Administrator on 2019/3/15 0015.
 */

public class BindAccountPresenterImpl implements BindAccountContract.BindAccountPresenter {

    private BindAccountContract.BindAccountView bindAccountView;
    private PayControllerModel payControllerModel;

    public BindAccountPresenterImpl(BindAccountContract.BindAccountView bindAccountView) {
        this.bindAccountView = bindAccountView;
        this.payControllerModel = new PayControllerModel();
    }

    @Override
    public void queryPayWayList() {
        showLoading();
        payControllerModel.queryListUsingGET(new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (bindAccountView != null) {
                    bindAccountView.queryPayWayListSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (bindAccountView != null) {
                    bindAccountView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (bindAccountView != null) {
                    bindAccountView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void doUpdateStatusBank(Long id, final Integer status) {
        showLoading();
        payControllerModel.doUpdateStatusBank(id,  status,new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (bindAccountView != null) {
                    bindAccountView.updateSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (bindAccountView != null) {
                    bindAccountView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (bindAccountView != null) {
                    bindAccountView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void doDeleteBank(Long id, String pws) {
        showLoading();
        payControllerModel.doDeleteBank(id,  pws,new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (bindAccountView != null) {
                    bindAccountView.updateSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (bindAccountView != null) {
                    bindAccountView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (bindAccountView != null) {
                    bindAccountView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void showLoading() {
        if (bindAccountView != null) {
            bindAccountView.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (bindAccountView != null) {
            bindAccountView.hideLoading();
        }
    }

    @Override
    public void destory() {
        bindAccountView = null;
    }
}
