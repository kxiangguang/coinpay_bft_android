package com.spark.moduleotc.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.acp.api.PayControllerApi;
import com.spark.library.acp.model.MemberPayTypeDto;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.okhttp.SendRemoteDataUtil;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.OtcUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;

import static com.spark.modulebase.base.BaseConstant.JSON_ERROR;
import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * 收款方式
 */
public class PayControllerModel {
    private PayControllerApi payControllerApi;

    public PayControllerModel() {
        this.payControllerApi = new PayControllerApi();
        this.payControllerApi.setBasePath(OtcUrls.getInstance().getHost());
    }


    /**
     * 添加
     *
     * @param payType
     * @param payAddress
     * @param bank
     * @param branch
     * @param tradePassword
     * @param qrCodeUrl
     * @param listener
     * @param errorListener
     */
    public void doBindBank(String payType, String payAddress, String bank, String branch, String tradePassword, String qrCodeUrl, String etWarn, String dayLimit, String accountId, String accountNickname, String realName,
                           final ResponseCallBack.SuccessListener<MessageResult> listener, final ResponseCallBack.ErrorListener errorListener) {
        final MemberPayTypeDto memberPayTypeDto = new MemberPayTypeDto();
        memberPayTypeDto.setPayType(payType);
        memberPayTypeDto.setPayAddress(payAddress);
        memberPayTypeDto.setBank(bank);
        memberPayTypeDto.setBranch(branch);
        memberPayTypeDto.setTradePwd(tradePassword);
        memberPayTypeDto.setQrCodeUrl(qrCodeUrl);
        memberPayTypeDto.setPayNotes(etWarn);
        memberPayTypeDto.setDayLimit(new BigDecimal(dayLimit));
        memberPayTypeDto.setExpandField(accountId);
        memberPayTypeDto.setNickname(accountNickname);
        memberPayTypeDto.setRealName(realName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                payControllerApi.addUsingPOST(memberPayTypeDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response);
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

    /**
     * 查询收款方式
     *
     * @param listener
     * @param errorListener
     */
    public void queryListUsingGET(final ResponseCallBack.SuccessListener<String> listener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                SendRemoteDataUtil.getInstance().doStringGet(OtcUrls.getInstance().queryPayWayListUrl(), params, new SendRemoteDataUtil.DataCallback() {
                    @Override
                    public void onDataLoaded(Object obj) {
                        try {
                            JSONObject jsonObject = new JSONObject(obj.toString());
                            int code = jsonObject.optInt("code");
                            String data = jsonObject.optString("data");
                            String url = jsonObject.optString("url");
                            if (code == SUCCESS_CODE) {
                                if (listener != null) {
                                    listener.onResponse(obj.toString());
                                }
                            } else {
                                if (errorListener != null) {
                                    errorListener.onErrorResponse(new HttpErrorEntity(code, data, url, ""));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (errorListener != null) {
                                errorListener.onErrorResponse(new HttpErrorEntity(JSON_ERROR, "", "", ""));
                            }
                        }
                    }

                    @Override
                    public void onDataNotAvailable(HttpErrorEntity httpErrorEntity) {
                        if (errorListener != null) {
                            errorListener.onErrorResponse(httpErrorEntity);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 修改
     *
     * @param id
     * @param payType
     * @param payAddress
     * @param bank
     * @param branch
     * @param tradePassword
     * @param qrCodeUrl
     * @param listener
     * @param errorListener
     */
    public void doUpdateBank(final Long id, String payType, String payAddress, String bank, String branch, String tradePassword, String qrCodeUrl, String etWarn, String dayLimit, String accountId, String accountNickname, String realName,
                             final ResponseCallBack.SuccessListener<MessageResult> listener, final ResponseCallBack.ErrorListener errorListener) {
        final MemberPayTypeDto memberPayTypeDto = new MemberPayTypeDto();
        memberPayTypeDto.setPayType(payType);
        memberPayTypeDto.setPayAddress(payAddress);
        memberPayTypeDto.setBank(bank);
        memberPayTypeDto.setBranch(branch);
        memberPayTypeDto.setTradePwd(tradePassword);
        memberPayTypeDto.setQrCodeUrl(qrCodeUrl);
        memberPayTypeDto.setPayNotes(etWarn);
        memberPayTypeDto.setDayLimit(new BigDecimal(dayLimit));
        memberPayTypeDto.setExpandField(accountId);
        memberPayTypeDto.setNickname(accountNickname);
        memberPayTypeDto.setRealName(realName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                payControllerApi.updateUsingPOST(id, memberPayTypeDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response);
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

    /**
     * 修改状态
     *
     * @param id
     * @param status
     * @param listener
     * @param errorListener
     */
    public void doUpdateStatusBank(final Long id, final Integer status, final ResponseCallBack.SuccessListener<MessageResult> listener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                payControllerApi.updateStatusUsingGET(id, status, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response);
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

    /**
     * 删除
     *
     * @param id
     * @param status
     * @param listener
     * @param errorListener
     */
    public void doDeleteBank(final Long id, final String pws, final ResponseCallBack.SuccessListener<MessageResult> listener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                payControllerApi.deleteUsingPOST(id, pws, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (listener != null) {
                                listener.onResponse(response);
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
