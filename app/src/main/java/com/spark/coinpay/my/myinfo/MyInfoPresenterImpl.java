package com.spark.coinpay.my.myinfo;


import com.android.volley.VolleyError;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.entity.User;
import com.spark.modulelogin.model.CasLoginModel;
import com.spark.moduleotc.model.AcceptanceMerchantControllerModel;
import com.spark.moduleotc.model.UploadControllerModel;
import com.spark.moduleuc.MemberControllerModel;
import com.spark.moduleuc.UcModel;


/**
 * 账户设置
 */
public class MyInfoPresenterImpl implements MyInfoContract.MyInfoPresenter {
    private MyInfoContract.MyInfoView myInfoView;
    private UcModel ucModel;
    private CasLoginModel casLoginModel;
    private AcceptanceMerchantControllerModel acceptanceMerchantControllerModel;
    private MemberControllerModel memberControllerModel;
    private UploadControllerModel uploadControllerModel;

    public MyInfoPresenterImpl(MyInfoContract.MyInfoView myInfoView) {
        this.myInfoView = myInfoView;
        ucModel = new UcModel();
        casLoginModel = new CasLoginModel();
        acceptanceMerchantControllerModel = new AcceptanceMerchantControllerModel();
        memberControllerModel = new MemberControllerModel();
        uploadControllerModel = new UploadControllerModel();
    }

    @Override
    public void uploadBase64Pic(String base64) {
        showLoading();
        uploadControllerModel.uploadBase64Pic(base64,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (myInfoView != null)
                            myInfoView.uploadBase64PicSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (myInfoView != null)
                            myInfoView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (myInfoView != null)
                            myInfoView.dealError(volleyError);
                    }
                });
    }

    @Override
    public void avatar(String url) {
        showLoading();
        ucModel.avatar(url, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (myInfoView != null)
                    myInfoView.avatarSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (myInfoView != null)
                    myInfoView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (myInfoView != null)
                    myInfoView.dealError(volleyError);
            }
        });
    }

    //查询派单前实名认证、承兑商身份认证、设置交易密码、设置收款方式状态
    public void findAuthenticationStatus() {
        showLoading();
        acceptanceMerchantControllerModel.findAuthenticationStatusUsingGET(new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (myInfoView != null) {
                    myInfoView.findAuthenticationStatusSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (myInfoView != null) {
                    myInfoView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (myInfoView != null) {
                    myInfoView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void getUserInfo() {
        memberControllerModel.getUserInfo(new ResponseCallBack.SuccessListener<User>() {
            @Override
            public void onResponse(User response) {
                hideLoading();
                if (myInfoView != null)
                    myInfoView.getUserInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (myInfoView != null)
                    myInfoView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (myInfoView != null)
                    myInfoView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (myInfoView != null)
            myInfoView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (myInfoView != null)
            myInfoView.hideLoading();
    }

    @Override
    public void destory() {
        myInfoView = null;
    }
}
