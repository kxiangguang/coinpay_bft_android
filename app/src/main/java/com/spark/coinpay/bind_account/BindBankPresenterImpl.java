package com.spark.coinpay.bind_account;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.PayControllerModel;
import com.spark.moduleotc.model.UploadControllerModel;

/**
 * Created by Administrator on 2019/3/15 0015.
 */
public class BindBankPresenterImpl implements BindAccountContract.BankPresenter {

    private BindAccountContract.BankView bankView;
    private PayControllerModel payControllerModel;
    private UploadControllerModel uploadControllerModel;

    public BindBankPresenterImpl(BindAccountContract.BankView bankView) {
        this.bankView = bankView;
        this.payControllerModel = new PayControllerModel();
        uploadControllerModel = new UploadControllerModel();
    }

    @Override
    public void showLoading() {
        if (bankView != null) {
            bankView.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (bankView != null) {
            bankView.hideLoading();
        }
    }

    @Override
    public void destory() {
        bankView = null;
    }


    /**
     * 绑定银行账户
     *
     * @param payType
     * @param payAddress
     * @param bankNum
     * @param branch
     * @param tradePassword
     */
    @Override
    public void doBindBank(String payType, String payAddress, final String bankNum, String branch, String tradePassword, String etWarn, String dayLimit, String qrCodeUrl) {
        showLoading();
        payControllerModel.doBindBank(payType, payAddress, bankNum, branch, tradePassword, qrCodeUrl, etWarn, dayLimit, "", "", "",
                new ResponseCallBack.SuccessListener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        hideLoading();
                        if (bankView != null) {
                            bankView.doBindBankSuccess(response);
                        }
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (bankView != null) {
                            bankView.dealError(httpErrorEntity);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (bankView != null) {
                            bankView.dealError(volleyError);
                        }
                    }
                });
    }

    /**
     * 更新银行账户信息
     *
     * @param id
     * @param payType
     * @param payAddress
     * @param bankNum
     * @param branch
     * @param tradePassword
     */
    @Override
    public void doUpdateBank(Long id, String payType, String payAddress, String bankNum, String branch, String tradePassword, String etWarn, String dayLimit, String qrCodeUrl) {
        showLoading();
        payControllerModel.doUpdateBank(id, payType, payAddress, bankNum, branch, tradePassword, qrCodeUrl, etWarn, dayLimit, "", "", "",
                new ResponseCallBack.SuccessListener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        hideLoading();
                        if (bankView != null) {
                            bankView.doUpdateBankSuccess(response);
                        }
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (bankView != null) {
                            bankView.dealError(httpErrorEntity);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (bankView != null) {
                            bankView.dealError(volleyError);
                        }
                    }
                });
    }

    @Override
    public void uploadBase64Pic(String base64) {
        showLoading();
        uploadControllerModel.uploadBase64Pic(base64,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (bankView != null)
                            bankView.uploadBase64PicSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (bankView != null)
                            bankView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (bankView != null)
                            bankView.dealError(volleyError);
                    }
                });
    }
}
