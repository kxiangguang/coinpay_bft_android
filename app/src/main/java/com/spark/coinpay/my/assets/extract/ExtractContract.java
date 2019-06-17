package com.spark.coinpay.my.assets.extract;


import com.spark.library.ac.model.MessageResult;
import com.spark.moduleassets.entity.Address;
import com.spark.moduleassets.entity.ExtractInfo;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.HttpErrorEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提币
 */

public interface ExtractContract {
    interface ExtractView extends BaseContract.BaseView {
        void getExtractInfoSuccess(List<ExtractInfo> list);

        void walletWithdrawSuccess(String response);

        void checkAddressSuccess(MessageResult response);

        void checkAddressFail(HttpErrorEntity response);
    }

    interface ExtractPresenter extends BaseContract.BasePresenter {
        void getExtractInfo(String coinName);

        void walletWithdraw(String address, BigDecimal amount, String coinName, String tradePassword);

        void checkAddress(String address);
    }


    interface ExtractAddressView extends BaseContract.BaseView {
        void getExtractAddressSuccess(List<Address> list);
    }

    interface ExtractAddressPresenter extends BaseContract.BasePresenter {
        void getExtractAddress(String coinName);
    }


    interface ExtractAddAddressView extends BaseContract.BaseView {
        void addExtractAddressSuccess(String response);
    }

    interface ExtractAddAddressPresenter extends BaseContract.BasePresenter {
        void addExtractAddress(String address, String coinId, String remark);
    }

}
