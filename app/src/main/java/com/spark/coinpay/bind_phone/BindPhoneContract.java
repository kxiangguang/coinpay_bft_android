package com.spark.coinpay.bind_phone;


import com.spark.modulebase.base.BaseContract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface BindPhoneContract {
    interface BindPhoneView extends BaseContract.BaseView {
        void bindPhoneSuccess(String obj);

        void sendChangePhoneCodeSuccess(String obj);

        void changePhoneSuccess(String obj);

        void sendCodeSuccess(String obj);

    }

    interface BindPhonePresenter extends BaseContract.BasePresenter {
        void bindPhone(String phone, String password, String code);

        void sendCode(String phone, String strAreaCode, String type);

        void sendChangePhoneCode(String type);

        void changePhone(String phone, String password, String code, String newCode);
    }
}
