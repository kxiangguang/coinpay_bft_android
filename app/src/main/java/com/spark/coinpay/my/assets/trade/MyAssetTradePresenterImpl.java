package com.spark.coinpay.my.assets.trade;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.model.AcceptanceMerchantControllerModel;

import java.util.List;

/**
 * Created by Administrator on 2019/3/5 0005.
 */

public class MyAssetTradePresenterImpl implements MyAssetTradeContract.MyAssetTradePresenter {
    private MyAssetTradeContract.MyAssetTradeView MyAssetTradeView;
    private AssetControllerModel assetControllerModel;
    private AcceptanceMerchantControllerModel acceptanceMerchantControllerModel;

    public MyAssetTradePresenterImpl(MyAssetTradeContract.MyAssetTradeView MyAssetTradeView) {
        this.MyAssetTradeView = MyAssetTradeView;
        assetControllerModel = new AssetControllerModel();
        acceptanceMerchantControllerModel = new AcceptanceMerchantControllerModel();
    }


    @Override
    public void getWallet(final String type) {
        showLoading();
        assetControllerModel.findWalletUsing(type, new ResponseCallBack.SuccessListener<List<Wallet>>() {
            @Override
            public void onResponse(List<Wallet> list) {
                LogUtils.i("response==" + list.toString());
                hideLoading();
                if (MyAssetTradeView != null)
                    MyAssetTradeView.getWalletSuccess(type, list);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (MyAssetTradeView != null)
                    MyAssetTradeView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (MyAssetTradeView != null)
                    MyAssetTradeView.dealError(volleyError);
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
                if (MyAssetTradeView != null)
                    MyAssetTradeView.getSelfLevelInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (MyAssetTradeView != null)
                    MyAssetTradeView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (MyAssetTradeView != null)
                    MyAssetTradeView.dealError(volleyError);
            }
        });
    }


    @Override
    public void showLoading() {
        if (MyAssetTradeView != null)
            MyAssetTradeView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (MyAssetTradeView != null)
            MyAssetTradeView.hideLoading();
    }

    @Override
    public void destory() {
        MyAssetTradeView = null;
    }

    @Override
    public void getRecordList(Integer type, String busiType) {
        showLoading();
        /*assetControllerModel.findAssetTransLog(type,busiType,new ResponseCallBack.SuccessListener<List<AssetRecord>>() {
            @Override
            public void onResponse(List<AssetRecord> list) {
                LogUtils.i("response==" + list.toString());
                hideLoading();
                if (MyAssetTradeView != null)
                    MyAssetTradeView.getRecordListSuccess(list);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (MyAssetTradeView != null)
                    MyAssetTradeView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (MyAssetTradeView != null)
                    MyAssetTradeView.dealError(volleyError);
            }
        });*/
    }
}
