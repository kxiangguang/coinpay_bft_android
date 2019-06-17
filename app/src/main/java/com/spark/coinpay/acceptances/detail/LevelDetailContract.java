package com.spark.coinpay.acceptances.detail;

import com.spark.library.acp.model.AcceptMerchantApplyMarginType;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.HttpErrorEntity;

/**
 * 承兑商
 */

public class LevelDetailContract {
    interface LevelDetailView extends BaseContract.BaseView {

        void getSelfLevelInfoSuccess(AcceptMerchantFrontVo acceptMerchantFrontVo);

        void getSelfLevelInfoError(HttpErrorEntity httpErrorEntity);

        void getAcceptancesProcessInfoSuccess(AcceptMerchantApplyMarginType acceptMerchantApplyMarginType);

    }

    interface LevelDetailPresenter extends BaseContract.BasePresenter {

        void getSelfLevelInfo();

        void getAcceptancesProcessInfo(int type);
    }


    interface ApplySurrenderView extends BaseContract.BaseView {

        void acceptMerchantCanelSuccess(String obj);

        void acceptMerchantCanelError(HttpErrorEntity httpErrorEntity);

    }

    interface ApplySurrenderPresenter extends BaseContract.BasePresenter {

        void acceptMerchantCanel(String reason);
    }
}
