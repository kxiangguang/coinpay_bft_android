package com.spark.coinpay.acceptances.authentication;

import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.Dict;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.HttpErrorEntity;

import java.util.List;

/**
 * 承兑商申请
 */
public class AuthentucatuinContract {
    interface AuthentucatuinView extends BaseContract.BaseView {

        void doUpLoadSuccess(String response);

        void doAuthencationSuccess(String response);

    }

    interface AuthentucatuinPresenter extends BaseContract.BasePresenter {

        void doUpLoad(String base64);

        void doAuthencation(Long id, String assetImg, String detail, String tradeDataImg);
    }
}
