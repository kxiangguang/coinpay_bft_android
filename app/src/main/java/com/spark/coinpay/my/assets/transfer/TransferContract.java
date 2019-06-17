package com.spark.coinpay.my.assets.transfer;


import com.spark.library.ac.model.AssetTransferDto;
import com.spark.moduleassets.entity.Coin;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资金划转
 */

public interface TransferContract {
    interface TransferView extends BaseContract.BaseView {
        void doWithDrawSuccess(String response);
    }

    interface TransferPresenter extends BaseContract.BasePresenter {
        void doWithDraw(BigDecimal amount, String coinName, AssetTransferDto.FromEnum from, AssetTransferDto.ToEnum to, String tradePassword);

    }
}
