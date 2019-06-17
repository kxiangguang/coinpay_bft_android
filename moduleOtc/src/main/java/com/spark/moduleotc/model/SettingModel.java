package com.spark.moduleotc.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.acp.api.OrderApi;
import com.spark.library.acp.api.SettingApi;
import com.spark.library.acp.model.MemberSetting;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OrderPaymentDto;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.okhttp.SendRemoteDataUtil;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.moduleotc.OtcUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.modulebase.base.BaseConstant.ERROR_CODE;
import static com.spark.modulebase.base.BaseConstant.FAIL_CODE;
import static com.spark.modulebase.base.BaseConstant.JSON_ERROR;
import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * setting
 */

public class SettingModel {

    private SettingApi settingApi;

    public SettingModel() {
        this.settingApi = new SettingApi();
        this.settingApi.setBasePath(OtcUrls.getInstance().getHost());
    }

    /**
     * 获取自动放行配置
     */
    public void getAutoRelease(final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        SendRemoteDataUtil.getInstance().doStringGet(OtcUrls.getInstance().getAutoRelease(), map, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code == SUCCESS_CODE || code == FAIL_CODE || code == ERROR_CODE) {
                        if (successListener != null)
                            successListener.onResponse(response);
                    } else {
                        if (errorListener != null)
                            errorListener.onErrorResponse(new HttpErrorEntity(object));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (errorListener != null)
                        errorListener.onErrorResponse(new HttpErrorEntity(JSON_ERROR, "", "", ""));
                }
            }

            @Override
            public void onDataNotAvailable(HttpErrorEntity httpErrorEntity) {
                if (errorListener != null)
                    errorListener.onErrorResponse(httpErrorEntity);
            }
        });
    }

    /**
     * 修改自动放行配置
     */
    public void updateAutoRelease(int autoReleaseSwitch, final ResponseCallBack.SuccessListener<MessageResult> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final MemberSetting memberSetting = new MemberSetting();
        memberSetting.setAutoReleaseSwitch(autoReleaseSwitch);
        new Thread(new Runnable() {
            @Override
            public void run() {
                settingApi.setAutoReleaseUsingPOST(memberSetting, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null) {
                                successListener.onResponse(response);
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                });
            }
        }).start();

    }

}