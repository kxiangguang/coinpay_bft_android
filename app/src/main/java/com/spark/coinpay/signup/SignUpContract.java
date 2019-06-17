package com.spark.coinpay.signup;


import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.entity.HttpErrorEntity;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * 注册
 */

public interface SignUpContract {
    interface SignView extends BaseContract.BaseView {

        void codeSuccess(String obj);

        void sighUpSuccess(String obj);

        void captchSuccess(JSONObject obj);

        void checkInviteCodeSuccess(String obj);

        void checkInviteCodeFail(HttpErrorEntity httpErrorEntity);

        void getImgSuccess(InputStream obj);
    }

    interface SignPresenter extends BaseContract.BasePresenter {
        void getPhoneCode(String phone, String check, String cid);

        void getPhoneCode(String phone);

        void getEmailCode(String email);

        void captch();

        void sighUpByEmail(String username, String password, String country, String promotion, String email, String code);

        void sighUpByPhone(String username, String password, String country, String promotion, String phone, String code);

        void checkInviteCode(String promotionCode);

        void getImg();

    }

}
