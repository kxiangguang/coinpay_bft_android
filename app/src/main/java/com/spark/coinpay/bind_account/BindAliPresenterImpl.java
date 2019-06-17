package com.spark.coinpay.bind_account;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.PayControllerModel;
import com.spark.moduleotc.model.UploadControllerModel;


/**
 * Created by Administrator on 2019/3/16 0016.
 */

public class BindAliPresenterImpl implements BindAccountContract.AliPresenter {

    private BindAccountContract.AliView aliView;
    private PayControllerModel payControllerModel;
    private UploadControllerModel uploadControllerModel;

    public BindAliPresenterImpl(BindAccountContract.AliView aliView) {
        this.aliView = aliView;
        payControllerModel = new PayControllerModel();
        uploadControllerModel = new UploadControllerModel();
    }

    @Override
    public void showLoading() {
        if (aliView != null) {
            aliView.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (aliView != null) {
            aliView.hideLoading();
        }
    }

    @Override
    public void destory() {
        aliView = null;
    }

    @Override
    public void uploadBase64Pic(String base64) {
        showLoading();
        uploadControllerModel.uploadBase64Pic(base64,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (aliView != null)
                            aliView.uploadBase64PicSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (aliView != null)
                            aliView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (aliView != null)
                            aliView.dealError(volleyError);
                    }
                });
    }

    @Override
    public void getBindAliOrWechat(String payType, String payAddress, final String bank, String branch, String tradePassword, String qrCodeUrl, String etWarn, String dayLimit, String accountId, String accountNickname, String realName) {
        showLoading();
        payControllerModel.doBindBank(payType, payAddress, bank, branch, tradePassword, qrCodeUrl, etWarn, dayLimit, accountId, accountNickname, realName,
                new ResponseCallBack.SuccessListener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        hideLoading();
                        if (aliView != null)
                            aliView.doBindAliOrWechatSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (aliView != null)
                            aliView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (aliView != null)
                            aliView.dealError(volleyError);
                    }
                });
    }

    @Override
    public void getUpdateAliOrWechat(Long id, String payType, String payAddress, final String bank, String branch, String tradePassword, String qrCodeUrl, String etWarn, String dayLimit, String accountId, String accountNickname, String realName) {
        showLoading();
        payControllerModel.doUpdateBank(id, payType, payAddress, bank, branch, tradePassword, qrCodeUrl, etWarn, dayLimit, accountId, accountNickname, realName,
                new ResponseCallBack.SuccessListener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        hideLoading();
                        if (aliView != null)
                            aliView.doUpdateAliOrWechatSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (aliView != null)
                            aliView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (aliView != null)
                            aliView.dealError(volleyError);
                    }
                });
    }


}
