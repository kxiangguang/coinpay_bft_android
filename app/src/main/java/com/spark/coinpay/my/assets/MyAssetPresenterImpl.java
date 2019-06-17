package com.spark.coinpay.my.assets;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.MessageResult;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.entity.AcceptanceMerchantListEntity;
import com.spark.moduleotc.model.AcceptanceMerchantControllerModel;

import java.util.List;

/**
 * 我的资产
 */
public class MyAssetPresenterImpl implements MyAssetContract.MyAssetPresenter {
    private MyAssetContract.MyAssetView myAssetView;
    private AssetControllerModel assetControllerModel;
    private AcceptanceMerchantControllerModel acceptanceMerchantControllerModel;

    public MyAssetPresenterImpl(MyAssetContract.MyAssetView MyAssetView) {
        this.myAssetView = MyAssetView;
        assetControllerModel = new AssetControllerModel();
        acceptanceMerchantControllerModel = new AcceptanceMerchantControllerModel();
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
    public void getTradeYesterday(final String tradingDay) {
        showLoading();
        acceptanceMerchantControllerModel.getAcceptMerchantTradeDayUsingPOST(tradingDay, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (myAssetView != null)
                    myAssetView.getTradeYesterdaySuccess(response);
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
    public void getTrade() {
        showLoading();
        acceptanceMerchantControllerModel.getTradeUsingGet(new ResponseCallBack.SuccessListener<AcceptanceMerchantListEntity>() {
            @Override
            public void onResponse(AcceptanceMerchantListEntity response) {
                hideLoading();
                if (myAssetView != null)
                    myAssetView.getTradeSuccess(response);
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

    @Override
    public void getSelfLevelInfo() {
        showLoading();
        acceptanceMerchantControllerModel.getSelfLevelInfo(new ResponseCallBack.SuccessListener<AcceptMerchantFrontVo>() {
            @Override
            public void onResponse(AcceptMerchantFrontVo response) {
                LogUtils.i("response==" + response.toString());
                hideLoading();
                if (myAssetView != null)
                    myAssetView.getSelfLevelInfoSuccess(response);
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
}
