package com.spark.coinpay.my.assets.record;


import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.moduleassets.entity.AssetRecord;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseContract;

import java.util.HashMap;
import java.util.List;

/**
 * 资产明细
 */
public interface MyAssetTradeRecordContract {
    interface MyAssetTradeView extends BaseContract.BaseView {

        void getRecordListSuccess(List<AssetRecord> list);

    }

    interface MyAssetTradePresenter extends BaseContract.BasePresenter {

        void getRecordList(boolean isShow, Integer type, Integer subType, HashMap<String, String> params, String busiType);

    }
}
