package com.spark.coinpay.bind_account;

import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.base.BaseContract;

import java.util.HashMap;

/**
 * 收款方式
 */
public interface BindAccountContract {
    interface BindAccountView extends BaseContract.BaseView {
        void queryPayWayListSuccess(String response);

        void updateSuccess(MessageResult response);
    }

    interface BindAccountPresenter extends BaseContract.BasePresenter {
        void queryPayWayList();

        void doUpdateStatusBank(Long id, Integer status);

        void doDeleteBank(Long id, String tradePassword);
    }

    interface AliView extends BaseContract.BaseView {
        void uploadBase64PicSuccess(String obj);

        void doBindAliOrWechatSuccess(MessageResult obj);

        void doUpdateAliOrWechatSuccess(MessageResult obj);

    }

    interface AliPresenter extends BaseContract.BasePresenter {

        void uploadBase64Pic(String base64);

        void getBindAliOrWechat(String payType, String payAddress, String bankNum, String branch, String tradePassword, String qrCodeUrl, String etWarn, String dayLimit, String accountId, String accountNickname, String realName);

        void getUpdateAliOrWechat(Long id, String payType, String payAddress, String bankNum, String branch, String tradePassword, String qrCodeUrl, String etWarn, String dayLimit, String accountId, String accountNickname, String realName);

    }

    interface BankView extends BaseContract.BaseView {

        void doBindBankSuccess(MessageResult obj);

        void doUpdateBankSuccess(MessageResult obj);

        void uploadBase64PicSuccess(String obj);

    }

    interface BankPresenter extends BaseContract.BasePresenter {

        void doBindBank(String payType, String account, String bank, String branch, String tradePassword, String etWarn, String dayLimit, String qrCodeUrl);

        void doUpdateBank(Long id, String payType, String accout, String bank, String branch, String tradePassword, String etWarn, String dayLimit, String qrCodeUrl);

        void uploadBase64Pic(String base64);
    }

    interface PaypalView extends BaseContract.BaseView {

        void doBindPaypalSuccess(MessageResult response);

        void doUpdatePaypalSuccess(MessageResult response);
    }

    interface PaypalPresenter extends BaseContract.BasePresenter {

        void doBindPaypal(String payType, String account, String bank, String branch, String tradePassword, String etWarn, String dayLimit);

        void doUpdatePaypal(Long id, String payType, String account, String bank, String branch, String tradePassword, String etWarn, String dayLimit);
    }
}
