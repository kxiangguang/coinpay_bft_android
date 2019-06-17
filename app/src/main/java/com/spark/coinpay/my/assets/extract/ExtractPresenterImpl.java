package com.spark.coinpay.my.assets.extract;


import com.android.volley.VolleyError;
import com.spark.library.ac.model.MessageResult;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.moduleassets.entity.ExtractInfo;
import com.spark.moduleassets.model.ChcekControllerModel;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提币
 */

public class ExtractPresenterImpl implements ExtractContract.ExtractPresenter {
    private ExtractContract.ExtractView extractView;
    private AssetControllerModel assetControllerModel;
    private ChcekControllerModel chcekControllerModel;

    public ExtractPresenterImpl(ExtractContract.ExtractView extractView) {
        this.extractView = extractView;
        assetControllerModel = new AssetControllerModel();
        chcekControllerModel = new ChcekControllerModel();
    }

    @Override
    public void walletWithdraw(String address, BigDecimal amount, String coinName, String tradePassword) {
        showLoading();
        assetControllerModel.walletWithdraw(address, amount, coinName, tradePassword, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (extractView != null)
                    extractView.walletWithdrawSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(volleyError);
            }
        });
    }


    @Override
    public void getExtractInfo(String coinName) {
        showLoading();
        assetControllerModel.findSupportAsset(coinName, new ResponseCallBack.SuccessListener<List<ExtractInfo>>() {
            @Override
            public void onResponse(List<ExtractInfo> response) {
                hideLoading();
                if (extractView != null)
                    extractView.getExtractInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(volleyError);
            }
        });
    }

    @Override
    public void checkAddress(String address) {
        showLoading();
        chcekControllerModel.checkAddress(address, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (extractView != null)
                    extractView.checkAddressSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (extractView != null)
                    extractView.checkAddressFail(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (extractView != null)
                    extractView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (extractView != null)
            extractView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (extractView != null)
            extractView.hideLoading();
    }

    @Override
    public void destory() {
        extractView = null;
    }
}
