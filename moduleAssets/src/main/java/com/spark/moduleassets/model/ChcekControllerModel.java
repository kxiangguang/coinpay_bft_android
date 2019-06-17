package com.spark.moduleassets.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.library.ac.api.AssetControllerApi;
import com.spark.library.ac.api.CheckControllerApi;
import com.spark.library.ac.model.AssetTransferDto;
import com.spark.library.ac.model.AssetWithdrawDto;
import com.spark.library.ac.model.MemberWithdrawAddressDto;
import com.spark.library.ac.model.MessageResult;
import com.spark.library.ac.model.MessageResultListCoin;
import com.spark.library.ac.model.MessageResultListMemberWalletVo;
import com.spark.library.ac.model.MessageResultListMemberWithdrawAddress;
import com.spark.library.ac.model.MessageResultPageMemberTransactionVo;
import com.spark.library.ac.model.QueryCondition;
import com.spark.library.ac.model.QueryParamMemberTransactionVo;
import com.spark.moduleassets.AcUrls;
import com.spark.moduleassets.entity.Address;
import com.spark.moduleassets.entity.AssetRecord;
import com.spark.moduleassets.entity.Coin;
import com.spark.moduleassets.entity.ExtractInfo;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * 资产
 */

public class ChcekControllerModel {
    private CheckControllerApi checkControllerApi;

    public ChcekControllerModel() {
        checkControllerApi = new CheckControllerApi();
        checkControllerApi.setBasePath(AcUrls.getInstance().getHost());
    }

    /**
     * 用户选择地址后校验提币地址是否内部地址
     */
    public void checkAddress(final String address, final ResponseCallBack.SuccessListener<MessageResult> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkControllerApi.checkAddressUsingGET(address, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response);
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(error);
                    }
                });
            }
        }).start();

    }


}
