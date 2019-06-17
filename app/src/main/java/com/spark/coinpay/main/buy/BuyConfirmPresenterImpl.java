package com.spark.coinpay.main.buy;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OneKeyBuyDto;
import com.spark.moduleassets.entity.Wallet;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.OneKeyBuyControllerModel;

import java.util.List;

/**
 * 一键买币
 */
public class BuyConfirmPresenterImpl implements BuyContract.ConfirmPresenter {
    private BuyContract.ConfirmView confirmView;
    private OneKeyBuyControllerModel oneKeyBuyControllerModel;

    public BuyConfirmPresenterImpl(BuyContract.ConfirmView confirmView) {
        this.confirmView = confirmView;
        oneKeyBuyControllerModel = new OneKeyBuyControllerModel();
    }

    @Override
    public void createOrder(OneKeyBuyDto oneKeyBuyDto) {
        showLoading();
        oneKeyBuyControllerModel.createOrder(oneKeyBuyDto, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (confirmView != null)
                    confirmView.createOrderSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (confirmView != null)
                    confirmView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (confirmView != null)
                    confirmView.dealError(volleyError);
            }
        });
    }


    @Override
    public void showLoading() {
        if (confirmView != null)
            confirmView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (confirmView != null)
            confirmView.hideLoading();
    }

    @Override
    public void destory() {
        confirmView = null;
    }

}
