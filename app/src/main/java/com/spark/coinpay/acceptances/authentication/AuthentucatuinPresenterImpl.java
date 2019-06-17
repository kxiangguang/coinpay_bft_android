package com.spark.coinpay.acceptances.authentication;

import com.android.volley.VolleyError;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.AcceptanceMerchantControllerModel;
import com.spark.moduleotc.model.UploadControllerModel;

/**
 * Created by Administrator on 2019/3/4 0004.
 */

public class AuthentucatuinPresenterImpl implements AuthentucatuinContract.AuthentucatuinPresenter {
    private AuthentucatuinContract.AuthentucatuinView authentucatuinView;
    private AcceptanceMerchantControllerModel acceptanceMerchantControllerModel;
    private UploadControllerModel uploadControllerModel;

    public AuthentucatuinPresenterImpl(AuthentucatuinContract.AuthentucatuinView authentucatuinView) {
        this.authentucatuinView = authentucatuinView;
        acceptanceMerchantControllerModel = new AcceptanceMerchantControllerModel();
        uploadControllerModel = new UploadControllerModel();
    }

    @Override
    public void showLoading() {
        if (authentucatuinView != null)
            authentucatuinView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (authentucatuinView != null)
            authentucatuinView.hideLoading();
    }

    @Override
    public void destory() {
        authentucatuinView = null;
    }

    @Override
    public void doUpLoad(String base64) {
        showLoading();
        uploadControllerModel.uploadBase64Pic(base64, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (authentucatuinView != null)
                    authentucatuinView.doUpLoadSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (authentucatuinView != null)
                    authentucatuinView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (authentucatuinView != null)
                    authentucatuinView.dealError(volleyError);
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
                        if (authentucatuinView != null)
                            authentucatuinView.doAuthencationSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (authentucatuinView != null)
                            authentucatuinView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (authentucatuinView != null)
                            authentucatuinView.dealError(volleyError);
                    }
                });
    }
}
