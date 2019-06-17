package com.spark.coinpay.base;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.uc.api.CaptchaGetControllerApi;
import com.spark.library.uc.model.MessageResult;
import com.spark.modulelogin.LoginUrls;

import org.json.JSONObject;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2019/3/2 0002.
 */

public class GlobalCaptcPresenterImpl implements GlobalCaptchContract.GlobalCaptchPresenter {
    private GlobalCaptchContract.GlobalCaptchView globalCaptchView;
    private CaptchaGetControllerApi captchaGetControllerApi;

    public GlobalCaptcPresenterImpl(GlobalCaptchContract.GlobalCaptchView globalCaptchView) {
        this.globalCaptchView = globalCaptchView;
        captchaGetControllerApi = new CaptchaGetControllerApi();
        captchaGetControllerApi.setBasePath(LoginUrls.getInstance().getHost());
    }

    @Override
    public void captch() {
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                captchaGetControllerApi.getGeeCaptchaUsingGET(new Response.Listener<Object>() {
                    @Override
                    public void onResponse(Object response) {
                        hideLoading();
                        if (globalCaptchView != null)
                            globalCaptchView.captchSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideLoading();
                        if (globalCaptchView != null)
                            globalCaptchView.dealError(error);
                    }
                });
            }
        });
    }

    @Override
    public void showLoading() {
        if (globalCaptchView != null)
            globalCaptchView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (globalCaptchView != null)
            globalCaptchView.hideLoading();
    }

    @Override
    public void destory() {
        globalCaptchView = null;
    }
}
