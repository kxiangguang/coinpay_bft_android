package com.spark.coinpay.my.forgot_pwd;


import com.android.volley.VolleyError;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulelogin.model.CasLoginModel;
import com.spark.moduleuc.CaptchaGetControllerModel;
import com.spark.moduleuc.MemberControllerModel;

import org.json.JSONObject;

/**
 * 忘记密码
 */

public class ForgotPwdPresenterImpl implements ForgotPwdContract.ForgotPwdPresenter {
    private ForgotPwdContract.ForgotPwdView forgotPwdView;
    private CaptchaGetControllerModel captchaGetControllerModel;
    private MemberControllerModel memberControllerModel;
    private CasLoginModel casLoginModel;

    public ForgotPwdPresenterImpl(ForgotPwdContract.ForgotPwdView forgotPwdView) {
        this.forgotPwdView = forgotPwdView;
        captchaGetControllerModel = new CaptchaGetControllerModel();
        memberControllerModel = new MemberControllerModel();
        casLoginModel = new CasLoginModel();
    }

    @Override
    public void getPhoneCode(String phone) {
        captchaGetControllerModel.getCodeByPhone(phone, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.getPhoneCodeSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(volleyError);
            }
        });
    }


    @Override
    public void captch() {
        showLoading();
        captchaGetControllerModel.doCaptch(new ResponseCallBack.SuccessListener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.captchSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(volleyError);
            }
        });
    }


    @Override
    public void getPhoneCode(String phone, String check, String cid) {
        showLoading();
        captchaGetControllerModel.getCodeByPhone(phone, check, cid, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.codeSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(volleyError);
            }
        });
    }

    @Override
    public void checkPhoneCode(String code) {
        showLoading();
        casLoginModel.phoneCodeCheck(code, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.checkPhoneCodeSuccess(response.toString());
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(volleyError);
            }
        });
    }

    @Override
    public void updateForget(String phone, String password) {
        showLoading();
        memberControllerModel.doForget(phone, password, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.updateForgetSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (forgotPwdView != null)
                    forgotPwdView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (forgotPwdView != null)
            forgotPwdView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (forgotPwdView != null)
            forgotPwdView.hideLoading();
    }

    @Override
    public void destory() {
        forgotPwdView = null;
    }

}
