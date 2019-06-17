package com.spark.coinpay.my.assets.transfer;

import com.android.volley.VolleyError;
import com.spark.library.ac.model.AssetTransferDto;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;

import java.math.BigDecimal;

/**
 * 资金划转
 */

public class TransferPresenterImp implements TransferContract.TransferPresenter {
    private TransferContract.TransferView transferView;
    private AssetControllerModel assetControllerModel;

    public TransferPresenterImp(TransferContract.TransferView transferView) {
        this.transferView = transferView;
        assetControllerModel = new AssetControllerModel();
    }


    @Override
    public void doWithDraw(BigDecimal amount, String coinName, AssetTransferDto.FromEnum from, AssetTransferDto.ToEnum to, String tradePassword) {
        showLoading();
        assetControllerModel.walletWithdraw(amount, coinName, from, to, tradePassword,
                new ResponseCallBack.SuccessListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        if (assetControllerModel != null)
                            transferView.doWithDrawSuccess(response);
                    }
                }, new ResponseCallBack.ErrorListener() {
                    @Override
                    public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                        hideLoading();
                        if (assetControllerModel != null)
                            transferView.dealError(httpErrorEntity);
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideLoading();
                        if (assetControllerModel != null)
                            transferView.dealError(volleyError);
                    }
                });
    }

    @Override
    public void showLoading() {
        if (transferView != null)
            transferView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (transferView != null)
            transferView.hideLoading();
    }

    @Override
    public void destory() {
        transferView = null;
    }
}
