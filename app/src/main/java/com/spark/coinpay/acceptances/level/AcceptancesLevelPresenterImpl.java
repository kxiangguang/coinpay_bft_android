package com.spark.coinpay.acceptances.level;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.AcceptMerchantApplyMarginType;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.Dict;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.model.AcceptanceMerchantControllerModel;

import java.util.List;

/**
 * 承兑商
 */

public class AcceptancesLevelPresenterImpl implements AcceptancesLevelContract.AcceptancesLevelPresenter {
    private AcceptancesLevelContract.AcceptancesLevelView acceptancesLevelView;
    private AcceptanceMerchantControllerModel acceptanceMerchantControllerModel;
    private AssetControllerModel assetControllerModel;

    public AcceptancesLevelPresenterImpl(AcceptancesLevelContract.AcceptancesLevelView acceptancesLevelView) {
        this.acceptancesLevelView = acceptancesLevelView;
        acceptanceMerchantControllerModel = new AcceptanceMerchantControllerModel();
        assetControllerModel = new AssetControllerModel();
    }

    @Override
    public void getLevelList() {
        showLoading();
        acceptanceMerchantControllerModel.getAcceptanceMerchantList(new ResponseCallBack.SuccessListener<List<Dict>>() {
            @Override
            public void onResponse(List<Dict> response) {
                hideLoading();
                if (acceptancesLevelView != null)
                    acceptancesLevelView.getLevelListSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (acceptancesLevelView != null)
                    acceptancesLevelView.getLevelListError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (acceptancesLevelView != null)
                    acceptancesLevelView.dealError(volleyError);
            }
        });
    }

    @Override
    public void getSelfLevelInfo() {
        showLoading();
        acceptanceMerchantControllerModel.getSelfLevelInfo(new ResponseCallBack.SuccessListener<AcceptMerchantFrontVo>() {
            @Override
            public void onResponse(AcceptMerchantFrontVo response) {
                LogUtils.i("response==" + response.toString());
                hideLoading();
                if (acceptancesLevelView != null)
                    acceptancesLevelView.getSelfLevelInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (acceptancesLevelView != null)
                    acceptancesLevelView.getSelfLevelInfoError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (acceptancesLevelView != null)
                    acceptancesLevelView.dealError(volleyError);
            }
        });
    }

    @Override
    public void getWallet(final String type) {
        showLoading();
        assetControllerModel.findWalletUsing(type, new ResponseCallBack.SuccessListener<List<Wallet>>() {
            @Override
            public void onResponse(List<Wallet> response) {
                hideLoading();
                if (acceptancesLevelView != null)
                    acceptancesLevelView.getWalletSuccess(type, response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (acceptancesLevelView != null)
                    acceptancesLevelView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (acceptancesLevelView != null)
                    acceptancesLevelView.dealError(volleyError);
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
                if (acceptancesLevelView != null)
                    acceptancesLevelView.getAcceptancesProcessInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (acceptancesLevelView != null)
                    acceptancesLevelView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (acceptancesLevelView != null)
                    acceptancesLevelView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (acceptancesLevelView != null)
            acceptancesLevelView.showLoading();

    }

    @Override
    public void hideLoading() {
        if (acceptancesLevelView != null)
            acceptancesLevelView.hideLoading();
    }

    @Override
    public void destory() {
        acceptancesLevelView = null;
    }
}
