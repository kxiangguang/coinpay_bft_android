package com.spark.moduleotc.model;

import com.google.gson.Gson;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.okhttp.SendRemoteDataUtil;
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
 * APP版本更新信息
 */

public class AppVersionModel {
    /**
     * 获取APP版本更新信息
     */
    public void getAppVersion(final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        SendRemoteDataUtil.getInstance().doStringGet(OtcUrls.getInstance().getAppVersionUsingGet(), map, new SendRemoteDataUtil.DataCallback() {
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
                if (httpErrorEntity != null) {
                    if (httpErrorEntity.getCode() == SUCCESS_CODE || httpErrorEntity.getCode() == FAIL_CODE || httpErrorEntity.getCode() == ERROR_CODE) {
                        if (successListener != null) {
                            String response = new Gson().toJson(httpErrorEntity);
                            successListener.onResponse(response);
                        }
                    }
                } else {
                    if (errorListener != null)
                        errorListener.onErrorResponse(httpErrorEntity);
                }

            }
        });
    }

}