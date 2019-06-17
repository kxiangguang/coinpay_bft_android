package com.spark.coinpay.acceptances.level;

import com.spark.library.acp.model.AcceptMerchantApplyMarginType;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.Dict;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.entity.User;

import java.util.List;

/**
 * 承兑商
 */

public class AcceptancesLevelContract {
    public interface AcceptancesLevelView extends BaseContract.BaseView {

        void getLevelListSuccess(List<Dict> list);

        void getLevelListError(HttpErrorEntity httpErrorEntity);

        void getSelfLevelInfoSuccess(AcceptMerchantFrontVo acceptMerchantFrontVo);

        void getSelfLevelInfoError(HttpErrorEntity httpErrorEntity);

        void getWalletSuccess(String type, List<Wallet> list);

        void getAcceptancesProcessInfoSuccess(AcceptMerchantApplyMarginType acceptMerchantApplyMarginType);

    }

    interface AcceptancesLevelPresenter extends BaseContract.BasePresenter {

        void getLevelList();

        void getSelfLevelInfo();

        void getWallet(String type);

        void getAcceptancesProcessInfo(int type);
    }
}
