package com.spark.coinpay.my.credit;

import com.android.volley.VolleyError;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.UploadControllerModel;
import com.spark.moduleuc.RealNameCertificationControllerModel;
import com.spark.moduleuc.entity.Credit;

/**
 * Created by Administrator on 2019/3/2 0002.
 */

public class CreditPresenterImpl implements CreditContract.Presenter {
    private CreditContract.View creditView;
    private RealNameCertificationControllerModel realNameCertificationControllerModel;
    private UploadControllerModel uploadControllerModel;

    public CreditPresenterImpl(CreditContract.View creditView) {
        this.creditView = creditView;
        realNameCertificationControllerModel = new RealNameCertificationControllerModel();
        uploadControllerModel = new UploadControllerModel();
    }

    @Override
    public void showLoading() {
        if (creditView != null)
            creditView.showLoading();

    }

    @Override
    public void hideLoading() {
        if (creditView != null)
            creditView.hideLoading();

    }

    @Override
    public void destory() {
        creditView = null;
    }

    @Override
    public void uploadBase64Pic(String base64) {
        showLoading();
        uploadControllerModel.uploadBase64Pic(base64,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (creditView != null)
                            creditView.uploadBase64PicSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (creditView != null)
                            creditView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (creditView != null)
                            creditView.dealError(volleyError);
                    }
                });
    }

    @Override
    public void credit(Long certifiedType, String idCardNumber, String identityCardImgFront, String identityCardImgInHand, String identityCardImgReverse, String realName) {
        showLoading();
        realNameCertificationControllerModel.credit(certifiedType, idCardNumber, identityCardImgFront, identityCardImgInHand, identityCardImgReverse, realName,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (creditView != null)
                            creditView.doCreditSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (creditView != null)
                            creditView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (creditView != null)
                            creditView.dealError(volleyError);
                    }
                });

    }

    @Override
    public void getCreditInfo() {
        showLoading();
        realNameCertificationControllerModel.getCreditInfo(new ResponseCallBack.SuccessListener<Credit.DataBean>() {
            @Override
            public void onResponse(Credit.DataBean response) {
                hideLoading();
                if (creditView != null) {
                    creditView.getCreditInfoSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {

            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }
}
