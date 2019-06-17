package com.spark.coinpay.my.safe;


import com.android.volley.VolleyError;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulelogin.model.CasLoginModel;
import com.spark.moduleuc.UcModel;
import com.spark.moduleuc.entity.SafeSetting;


/**
 * Created by Administrator on 2017/9/25.
 */

public class SafePresenterImpl implements SafeContract.SafePresenter {
    private SafeContract.SafeView safeView;
    private UcModel ucModel;
    private CasLoginModel casLoginModel;

    public SafePresenterImpl(SafeContract.SafeView SafeView) {
        this.safeView = SafeView;
        ucModel = new UcModel();
        casLoginModel = new CasLoginModel();
    }

    @Override
    public void safeSetting() {
        showLoading();
        ucModel.safeSetting(new ResponseCallBack.SuccessListener<SafeSetting>() {
            @Override
            public void onResponse(SafeSetting response) {
                hideLoading();
                if (safeView != null)
                    safeView.safeSettingSuccess(response);

            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (safeView != null)
                    safeView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (safeView != null)
                    safeView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (safeView != null)
            safeView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (safeView != null)
            safeView.hideLoading();
    }

    @Override
    public void destory() {
        safeView = null;
    }
}
