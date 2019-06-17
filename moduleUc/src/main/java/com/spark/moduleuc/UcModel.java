package com.spark.moduleuc;

import com.google.gson.Gson;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.okhttp.SendRemoteDataUtil;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.moduleuc.entity.SafeSetting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.modulebase.base.BaseConstant.JSON_ERROR;
import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * 账户设置参数
 */

public class UcModel {
    /**
     * 获取账户设置参数
     */
    public void safeSetting(final ResponseCallBack.SuccessListener<SafeSetting> successListener, final ResponseCallBack.ErrorListener errorListener) {
        SendRemoteDataUtil.getInstance().doStringPost(UcUrls.getInstance().getSafeSettingUrl(), new HashMap<String, String>(),
                new SendRemoteDataUtil.DataCallback() {
                    @Override
                    public void onDataLoaded(Object obj) {
                        String response = (String) obj;
                        try {
                            JSONObject object = new JSONObject(response);
                            int code = StringUtils.getCode(object);
                            if (code == SUCCESS_CODE) {
                                SafeSetting safeSetting = new Gson().fromJson(object.getJSONObject("data").toString(), SafeSetting.class);
                                if (successListener != null)
                                    successListener.onResponse(safeSetting);
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
     * 上传图片
     *
     * @param strBase64
     */
    public void uploadBase64Pic(String strBase64, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("base64Data", strBase64);
        SendRemoteDataUtil.getInstance().doStringPost(UcUrls.getInstance().getUploadPicUrl(), map, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code == SUCCESS_CODE) {
                        if (successListener != null)
                            successListener.onResponse(object.optString("data"));
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
     * 上传头像
     *
     * @param url
     */
    public void avatar(final String url, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("avatar", url);
        SendRemoteDataUtil.getInstance().doStringPost(UcUrls.getInstance().getAvatarUrl(), map, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code == SUCCESS_CODE) {
                        if (successListener != null)
                            successListener.onResponse(StringUtils.getMessage(object));
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
     * 绑定手机号
     *
     * @param phone
     * @param password
     * @param code
     */
    public void bindPhone(String phone, String password, final String code, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);
        map.put("code", code);
        SendRemoteDataUtil.getInstance().doStringPost(UcUrls.getInstance().getBindPhoneUrl(), map, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code == SUCCESS_CODE) {
                        if (successListener != null)
                            successListener.onResponse(StringUtils.getMessage(object));
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
     * 获取当前绑定手机的手机号码
     *
     * @param type
     */
    public void sendChangePhoneCode(String type, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", type);
        SendRemoteDataUtil.getInstance().doStringPost(UcUrls.getInstance().getSendCodeAfterLoginUrl(), map, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code == SUCCESS_CODE) {
                        if (successListener != null)
                            successListener.onResponse(StringUtils.getMessage(object));
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
     * 修改手机号
     *
     * @param params
     */
    public void changePhone(String phone, String password, String code, String newCode, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);
        map.put("oldCode", code);
        map.put("newCode", newCode);
        SendRemoteDataUtil.getInstance().doStringPost(UcUrls.getInstance().getChangePhoneUrl(), map, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    int code = StringUtils.getCode(object);
                    if (code == SUCCESS_CODE) {
                        if (successListener != null)
                            successListener.onResponse(StringUtils.getMessage(object));
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


}