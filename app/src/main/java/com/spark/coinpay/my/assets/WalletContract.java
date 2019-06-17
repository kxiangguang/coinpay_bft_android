package com.spark.coinpay.my.assets;


import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.User;

import java.util.List;

/**
 * 钱包
 */

public interface WalletContract {
    interface WalletView extends BaseContract.BaseView {
        void getWalletSuccess(String type, List<Wallet> list);
    }

    interface WalletPresenter extends BaseContract.BasePresenter {

        void getWallet(String type);

    }
}
