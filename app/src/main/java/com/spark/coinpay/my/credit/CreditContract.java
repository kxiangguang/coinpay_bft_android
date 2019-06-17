package com.spark.coinpay.my.credit;


import com.spark.modulebase.base.BaseContract;
import com.spark.moduleuc.entity.Credit;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface CreditContract {
    interface View extends BaseContract.BaseView {

        void uploadBase64PicSuccess(String obj);

        void doCreditSuccess(String obj);

        void getCreditInfoSuccess(Credit.DataBean obj);

    }

    interface Presenter extends BaseContract.BasePresenter {

        void uploadBase64Pic(String base64);

        void credit(Long certifiedType, String idCardNumber, String identityCardImgFront, String identityCardImgInHand, String identityCardImgReverse, String realName);

        void getCreditInfo();
    }
}
