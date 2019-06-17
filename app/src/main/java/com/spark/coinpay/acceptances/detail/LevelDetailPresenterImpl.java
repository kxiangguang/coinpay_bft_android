package com.spark.coinpay.acceptances.detail;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.AcceptMerchantApplyMarginType;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.model.AcceptanceMerchantControllerModel;

/**
 * 承兑商等级详情
 */

public class LevelDetailPresenterImpl implements LevelDetailContract.LevelDetailPresenter {
    private LevelDetailContract.LevelDetailView levelDetailView;
    private AcceptanceMerchantControllerModel acceptanceMerchantControllerModel;

    public LevelDetailPresenterImpl(LevelDetailContract.LevelDetailView levelDetailView) {
        this.levelDetailView = levelDetailView;
        acceptanceMerchantControllerModel = new AcceptanceMerchantControllerModel();

    }

    @Override
    public void getSelfLevelInfo() {
        showLoading();
        acceptanceMerchantControllerModel.getSelfLevelInfo(new ResponseCallBack.SuccessListener<AcceptMerchantFrontVo>() {
            @Override
            public void onResponse(AcceptMerchantFrontVo response) {
                LogUtils.i("response==" + response.toString());
                hideLoading();
                if (levelDetailView != null)
                    levelDetailView.getSelfLevelInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (levelDetailView != null)
                    levelDetailView.getSelfLevelInfoError(httpErrorEntity);
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
    public void getAcceptancesProcessInfo(int type) {
        showLoading();
        acceptanceMerchantControllerModel.getAcceptancesProcessInfo(type, new ResponseCallBack.SuccessListener<AcceptMerchantApplyMarginType>() {
            @Override
            public void onResponse(AcceptMerchantApplyMarginType response) {
                hideLoading();
                if (levelDetailView != null)
                    levelDetailView.getAcceptancesProcessInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (levelDetailView != null)
                    levelDetailView.dealError(httpErrorEntity);
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
