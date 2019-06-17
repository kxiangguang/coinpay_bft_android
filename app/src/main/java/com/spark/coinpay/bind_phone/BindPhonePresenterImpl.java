package com.spark.coinpay.bind_phone;


import com.android.volley.VolleyError;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleuc.CaptchaGetControllerModel;
import com.spark.moduleuc.UcModel;


/**
 * Created by Administrator on 2017/9/25.
 */

public class BindPhonePresenterImpl implements BindPhoneContract.BindPhonePresenter {
    private BindPhoneContract.BindPhoneView bindPhoneView;
    private UcModel ucModel;
    private CaptchaGetControllerModel captchaGetControllerModel;

    public BindPhonePresenterImpl(BindPhoneContract.BindPhoneView bindPhoneView) {
        this.bindPhoneView = bindPhoneView;
        ucModel = new UcModel();
        captchaGetControllerModel = new CaptchaGetControllerModel();
    }

    @Override
    public void bindPhone(String phone, String password, String code) {
        showLoading();
        ucModel.bindPhone(phone, password, code, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (bindPhoneView != null)
                    bindPhoneView.bindPhoneSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (bindPhoneView != null)
                    bindPhoneView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (bindPhoneView != null)
                    bindPhoneView.dealError(volleyError);
            }
        });
    }

    @Override
    public void sendCode(String phone, String strAreaCode, String type) {
        showLoading();
        captchaGetControllerModel.getCodeByPhone(phone, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (bindPhoneView != null)
                    bindPhoneView.sendCodeSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (bindPhoneView != null)
                    bindPhoneView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (bindPhoneView != null)
                    bindPhoneView.dealError(volleyError);
            }
        });
    }

    @Override
    public void sendChangePhoneCode(String type) {
        showLoading();
        ucModel.sendChangePhoneCode(type, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (bindPhoneView != null)
                    bindPhoneView.sendChangePhoneCodeSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (bindPhoneView != null)
                    bindPhoneView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (bindPhoneView != null)
                    bindPhoneView.dealError(volleyError);
            }
        });
    }

    @Override
    public void changePhone(String phone, String password, String code, String newCode) {
        showLoading();
        ucModel.changePhone(phone, password, code, newCode, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (bindPhoneView != null)
                    bindPhoneView.changePhoneSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (bindPhoneView != null)
                    bindPhoneView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (bindPhoneView != null)
                    bindPhoneView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (bindPhoneView != null)
            bindPhoneView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (bindPhoneView != null)
            bindPhoneView.hideLoading();
    }

    @Override
    public void destory() {
        bindPhoneView = null;
    }
}
