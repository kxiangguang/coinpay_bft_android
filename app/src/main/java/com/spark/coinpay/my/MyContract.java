package com.spark.coinpay.my;

import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.Dict;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.entity.User;

import java.util.List;

/**
 * 我的
 */
public class MyContract {
    public interface MyView extends BaseContract.BaseView {

        void getLevelListSuccess(List<Dict> list);

        void getLevelListError(HttpErrorEntity httpErrorEntity);

        void getSelfLevelInfoSuccess(AcceptMerchantFrontVo acceptMerchantFrontVo);

        void getSelfLevelInfoError(HttpErrorEntity httpErrorEntity);

        void findAuthenticationStatusSuccess(String response);

        void getUserInfoSuccess(User user);

    }

    interface MyPresenter extends BaseContract.BasePresenter {

        void getLevelList();

        void getSelfLevelInfo();

        void findAuthenticationStatus();

        void getUserInfo();
    }
}
