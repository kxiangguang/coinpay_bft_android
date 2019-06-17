package com.spark.coinpay.my.assets.extract;


import com.android.volley.VolleyError;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.moduleassets.entity.Address;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;

import java.util.List;

/**
 * 提币地址
 */

public class ExtractAddressPresenterImpl implements ExtractContract.ExtractAddressPresenter {
    private ExtractContract.ExtractAddressView extractView;
    private AssetControllerModel assetControllerModel;

    public ExtractAddressPresenterImpl(ExtractContract.ExtractAddressView extractView) {
        this.extractView = extractView;
        assetControllerModel = new AssetControllerModel();
    }

    @Override
    public void getExtractAddress(String coinName) {
        assetControllerModel.findWalletWithdrawAddress(coinName, new ResponseCallBack.SuccessListener<List<Address>>() {
            @Override
            public void onResponse(List<Address> response) {
                hideLoading();
                if (extractView != null)
                    extractView.getExtractAddressSuccess(response);
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
