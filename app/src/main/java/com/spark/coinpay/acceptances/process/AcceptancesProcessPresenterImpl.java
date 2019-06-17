package com.spark.coinpay.acceptances.process;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.AcceptMerchantApplyMarginType;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.AcceptanceMerchantControllerModel;

/**
 * Created by Administrator on 2019/3/4 0004.
 */

public class AcceptancesProcessPresenterImpl implements AcceptancesProcessContract.AcceptancesProcessPresenter {
    private AcceptancesProcessContract.AcceptancesProcessView acceptancesProcessView;
    private AcceptanceMerchantControllerModel acceptanceMerchantControllerModel;

    public AcceptancesProcessPresenterImpl(AcceptancesProcessContract.AcceptancesProcessView acceptancesProcessView) {
        this.acceptancesProcessView = acceptancesProcessView;
        acceptanceMerchantControllerModel = new AcceptanceMerchantControllerModel();
    }

    @Override
    public void showLoading() {
        if (acceptancesProcessView != null)
            acceptancesProcessView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (acceptancesProcessView != null)
            acceptancesProcessView.hideLoading();
    }

    @Override
    public void destory() {
        acceptancesProcessView = null;
    }

    @Override
    public void getAcceptancesProcessInfo(int type) {
        showLoading();
        acceptanceMerchantControllerModel.getAcceptancesProcessInfo(type, new ResponseCallBack.SuccessListener<AcceptMerchantApplyMarginType>() {
            @Override
            public void onResponse(AcceptMerchantApplyMarginType response) {
                hideLoading();
                if (acceptancesProcessView != null)
                    acceptancesProcessView.getAcceptancesProcessInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (acceptancesProcessView != null)
                    acceptancesProcessView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (acceptancesProcessView != null)
                    acceptancesProcessView.dealError(volleyError);
            }
        });
    }

    @Override
    public void doAuthencation(Long id, String assetImg, String detail, String tradeDataImg) {
        showLoading();
        acceptanceMerchantControllerModel.doApply(id, assetImg, detail, tradeDataImg,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (acceptancesProcessView != null)
                            acceptancesProcessView.doAuthencationSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (acceptancesProcessView != null)
                            acceptancesProcessView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (acceptancesProcessView != null)
                            acceptancesProcessView.dealError(volleyError);
                    }
                });
    }
}
