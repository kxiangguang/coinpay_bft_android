package com.spark.coinpay.acceptances.detail;

import com.android.volley.VolleyError;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.model.AcceptanceMerchantControllerModel;

/**
 * 承兑商等级详情
 */

public class ApplySurrenderPresenterImpl implements LevelDetailContract.ApplySurrenderPresenter {
    private LevelDetailContract.ApplySurrenderView levelDetailView;
    private AcceptanceMerchantControllerModel acceptanceMerchantControllerModel;

    public ApplySurrenderPresenterImpl(LevelDetailContract.ApplySurrenderView levelDetailView) {
        this.levelDetailView = levelDetailView;
        acceptanceMerchantControllerModel = new AcceptanceMerchantControllerModel();
    }

    @Override
    public void acceptMerchantCanel(String reason) {
        showLoading();
        acceptanceMerchantControllerModel.acceptMerchantCanel(reason, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.i("response==" + response.toString());
                hideLoading();
                if (levelDetailView != null)
                    levelDetailView.acceptMerchantCanelSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (levelDetailView != null)
                    levelDetailView.acceptMerchantCanelError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (levelDetailView != null)
                    levelDetailView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (levelDetailView != null)
            levelDetailView.showLoading();

    }

    @Override
    public void hideLoading() {
        if (levelDetailView != null)
            levelDetailView.hideLoading();
    }

    @Override
    public void destory() {
        levelDetailView = null;
    }
}
