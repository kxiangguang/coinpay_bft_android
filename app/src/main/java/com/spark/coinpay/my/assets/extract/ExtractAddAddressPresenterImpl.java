package com.spark.coinpay.my.assets.extract;


import com.android.volley.VolleyError;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;

/**
 * 添加提币地址
 */

public class ExtractAddAddressPresenterImpl implements ExtractContract.ExtractAddAddressPresenter {
    private ExtractContract.ExtractAddAddressView extractView;
    private AssetControllerModel assetControllerModel;

    public ExtractAddAddressPresenterImpl(ExtractContract.ExtractAddAddressView extractView) {
        this.extractView = extractView;
        assetControllerModel = new AssetControllerModel();
    }

    @Override
    public void addExtractAddress(String address, String coinId, String remark) {
        assetControllerModel.addWalletWithdrawAddressUsing( address,  coinId,  remark, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (extractView != null)
                    extractView.addExtractAddressSuccess(response);
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
