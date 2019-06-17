package com.spark.coinpay.my.assets.record;

import com.android.volley.VolleyError;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.moduleassets.entity.AssetRecord;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 资产明细
 */
public class MyAssetTradeRecordPresenterImpl implements MyAssetTradeRecordContract.MyAssetTradePresenter {
    private MyAssetTradeRecordContract.MyAssetTradeView myAssetTradeView;
    private AssetControllerModel assetControllerModel;

    public MyAssetTradeRecordPresenterImpl(MyAssetTradeRecordContract.MyAssetTradeView myAssetTradeView) {
        this.myAssetTradeView = myAssetTradeView;
        assetControllerModel = new AssetControllerModel();
    }

    @Override
    public void showLoading() {
        if (myAssetTradeView != null)
            myAssetTradeView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (myAssetTradeView != null)
            myAssetTradeView.hideLoading();
    }

    @Override
    public void destory() {
        myAssetTradeView = null;
    }

    @Override
    public void getRecordList(boolean isShow, Integer type, Integer subType, HashMap<String, String> map, String busiType) {
        if (isShow)
            showLoading();
        assetControllerModel.findAssetTransLog(type, subType, map, busiType, new ResponseCallBack.SuccessListener<List<AssetRecord>>() {
            @Override
            public void onResponse(List<AssetRecord> list) {
                LogUtils.i("response==" + list.toString());
                hideLoading();
                if (myAssetTradeView != null)
                    myAssetTradeView.getRecordListSuccess(list);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (myAssetTradeView != null)
                    myAssetTradeView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (myAssetTradeView != null)
                    myAssetTradeView.dealError(volleyError);
            }
        });
    }
}
