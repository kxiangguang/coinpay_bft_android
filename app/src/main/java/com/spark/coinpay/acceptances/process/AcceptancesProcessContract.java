package com.spark.coinpay.acceptances.process;

import com.spark.library.acp.model.AcceptMerchantApplyMarginType;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.Dict;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.HttpErrorEntity;

import java.util.List;

/**
 * 认证类型详情
 */

public class AcceptancesProcessContract {
    interface AcceptancesProcessView extends BaseContract.BaseView {
        void getAcceptancesProcessInfoSuccess(AcceptMerchantApplyMarginType acceptMerchantApplyMarginType);

        void doAuthencationSuccess(String response);
    }

    interface AcceptancesProcessPresenter extends BaseContract.BasePresenter {
        void getAcceptancesProcessInfo(int type);

        void doAuthencation(Long id, String assetImg, String detail, String tradeDataImg);
    }
}
