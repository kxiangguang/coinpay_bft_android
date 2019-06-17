package com.spark.coinpay.main.buy;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OneKeyBuyDto;
import com.spark.library.acp.model.OrderCompleteDto;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.model.OneKeyBuyControllerModel;

/**
 * 一键买币
 */
public class BuyCancelPresenterImpl implements BuyContract.CancelPresenter {
    private BuyContract.CancelView cancelView;
    private OneKeyBuyControllerModel oneKeyBuyControllerModel;

    public BuyCancelPresenterImpl(BuyContract.CancelView confirmView) {
        this.cancelView = confirmView;
        oneKeyBuyControllerModel = new OneKeyBuyControllerModel();
    }

    @Override
    public void cancelOrder(OrderCompleteDto orderCompleteDto) {
        showLoading();
        oneKeyBuyControllerModel.canceleOrder(orderCompleteDto, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (cancelView != null)
                    cancelView.cancelSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (cancelView != null)
                    cancelView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (cancelView != null)
                    cancelView.dealError(volleyError);
            }
        });
    }


    @Override
    public void showLoading() {
        if (cancelView != null)
            cancelView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (cancelView != null)
            cancelView.hideLoading();
    }

    @Override
    public void destory() {
        cancelView = null;
    }

}
