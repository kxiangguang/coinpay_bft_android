package com.spark.coinpay.my.assets.base;

import com.android.volley.VolleyError;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.model.AcceptanceMerchantControllerModel;

import java.util.List;

/**
 * 我的资产-基本账户
 */
public class MyAssetBasePresenterImpl implements MyAssetBaseContract.MyAssetBasePresenter {
    private MyAssetBaseContract.MyAssetBaseView MyAssetBaseView;
    private AssetControllerModel assetControllerModel;
    private AcceptanceMerchantControllerModel acceptanceMerchantControllerModel;

    public MyAssetBasePresenterImpl(MyAssetBaseContract.MyAssetBaseView MyAssetBaseView) {
        this.MyAssetBaseView = MyAssetBaseView;
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
                if (MyAssetBaseView != null)
                    MyAssetBaseView.getWalletSuccess(type, list);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (MyAssetBaseView != null)
                    MyAssetBaseView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (MyAssetBaseView != null)
                    MyAssetBaseView.dealError(volleyError);
            }
        });
    }


    @Override
    public void showLoading() {
        if (MyAssetBaseView != null)
            MyAssetBaseView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (MyAssetBaseView != null)
            MyAssetBaseView.hideLoading();
    }

    @Override
    public void destory() {
        MyAssetBaseView = null;
    }

    @Override
    public void getRecordList(Integer type, String busiType) {
        showLoading();
        /*assetControllerModel.findAssetTransLog(type, busiType, new ResponseCallBack.SuccessListener<List<AssetRecord>>() {
            @Override
            public void onResponse(List<AssetRecord> list) {
                LogUtils.i("response==" + list.toString());
                hideLoading();
                if (MyAssetBaseView != null)
                    MyAssetBaseView.getRecordListSuccess(list);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (MyAssetBaseView != null)
                    MyAssetBaseView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (MyAssetBaseView != null)
                    MyAssetBaseView.dealError(volleyError);
            }
        });*/
    }
}
