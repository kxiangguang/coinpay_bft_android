package com.spark.coinpay.my.assets;

import com.android.volley.VolleyError;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;

import java.util.List;

/**
 * Created by Administrator on 2019/3/5 0005.
 */

public class WalletPresenterImpl implements WalletContract.WalletPresenter {
    private WalletContract.WalletView walletView;
    private AssetControllerModel assetControllerModel;

    public WalletPresenterImpl(WalletContract.WalletView walletView) {
        this.walletView = walletView;
        assetControllerModel = new AssetControllerModel();
    }


    @Override
    public void getWallet(final String type) {
        showLoading();
        assetControllerModel.findWalletUsing(type, new ResponseCallBack.SuccessListener<List<Wallet>>() {
            @Override
            public void onResponse(List<Wallet> response) {
                hideLoading();
                if (walletView != null)
                    walletView.getWalletSuccess(type, response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (walletView != null)
                    walletView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (walletView != null)
                    walletView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (walletView != null)
            walletView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (walletView != null)
            walletView.hideLoading();
    }

    @Override
    public void destory() {
        walletView = null;
    }
}
