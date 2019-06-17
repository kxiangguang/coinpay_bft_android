package com.spark.coinpay.main.buy;

import com.android.volley.VolleyError;
import com.spark.moduleassets.entity.Wallet;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;

import java.util.List;

/**
 * 一键买币
 */
public class BuyPresenterImpl implements BuyContract.Presenter {
    private BuyContract.View myAssetView;
    private AssetControllerModel assetControllerModel;

    public BuyPresenterImpl(BuyContract.View MyAssetView) {
        this.myAssetView = MyAssetView;
        assetControllerModel = new AssetControllerModel();
    }

    @Override
    public void getWallet(final String type) {
        showLoading();
        assetControllerModel.findWalletUsing(type, new ResponseCallBack.SuccessListener<List<Wallet>>() {
            @Override
            public void onResponse(List<Wallet> response) {
                hideLoading();
                if (myAssetView != null)
                    myAssetView.getWalletSuccess(type, response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (myAssetView != null)
                    myAssetView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (myAssetView != null)
                    myAssetView.dealError(volleyError);
            }
        });
    }


    @Override
    public void showLoading() {
        if (myAssetView != null)
            myAssetView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (myAssetView != null)
            myAssetView.hideLoading();
    }

    @Override
    public void destory() {
        myAssetView = null;
    }

}
