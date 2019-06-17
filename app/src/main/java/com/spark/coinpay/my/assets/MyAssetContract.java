package com.spark.coinpay.my.assets;


import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultAcceptMerchantTrade;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.entity.AcceptMerchantInfoEntity;
import com.spark.moduleotc.entity.AcceptanceMerchantListEntity;

import java.util.List;

/**
 * 我的资产
 */

public interface MyAssetContract {
    interface MyAssetView extends BaseContract.BaseView {

        void getWalletSuccess(String type, List<Wallet> list);

        void getSelfLevelInfoSuccess(AcceptMerchantFrontVo acceptMerchantFrontVo);

        void getTradeSuccess(AcceptanceMerchantListEntity response);

        void getTradeYesterdaySuccess(MessageResult response);

    }

    interface MyAssetPresenter extends BaseContract.BasePresenter {

        void getWallet(String type);

        void getTradeYesterday(String tradingDay);

        void getSelfLevelInfo();

        void getTrade();

    }
}
