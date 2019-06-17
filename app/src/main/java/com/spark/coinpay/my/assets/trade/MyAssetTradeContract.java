package com.spark.coinpay.my.assets.trade;


import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.moduleassets.entity.AssetRecord;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseContract;

import java.util.List;

/**
 * 我的资产
 */

public interface MyAssetTradeContract {
    interface MyAssetTradeView extends BaseContract.BaseView {

        void getWalletSuccess(String type, List<Wallet> list);

        void getRecordListSuccess(List<AssetRecord> list);

        void getSelfLevelInfoSuccess(AcceptMerchantFrontVo acceptMerchantFrontVo);

    }

    interface MyAssetTradePresenter extends BaseContract.BasePresenter {

        void getWallet(String type);

        void getRecordList(Integer type, String busiType);

        void getSelfLevelInfo();

    }
}
