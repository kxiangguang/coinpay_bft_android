package com.spark.coinpay.main.buy;


import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OneKeyBuyDto;
import com.spark.library.acp.model.OrderCompleteDto;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseContract;

import java.util.List;

/**
 * 我的资产
 */

public interface BuyContract {
    interface View extends BaseContract.BaseView {

        void getWalletSuccess(String type, List<Wallet> list);

    }

    interface Presenter extends BaseContract.BasePresenter {

        void getWallet(String type);

    }

    interface ConfirmView extends BaseContract.BaseView {

        void createOrderSuccess(MessageResult response);
    }

    interface ConfirmPresenter extends BaseContract.BasePresenter {

        void createOrder(OneKeyBuyDto oneKeyBuyDto);
    }

    interface CancelView extends BaseContract.BaseView {

        void cancelSuccess(MessageResult response);
    }

    interface CancelPresenter extends BaseContract.BasePresenter {

        void cancelOrder(OrderCompleteDto orderCompleteDto);
    }


}
