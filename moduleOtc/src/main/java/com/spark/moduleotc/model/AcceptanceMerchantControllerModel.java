package com.spark.moduleotc.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.spark.library.acp.api.AcceptanceMerchantControllerApi;
import com.spark.library.acp.model.AcceptMerchantApplyInTransitDto;
import com.spark.library.acp.model.AcceptMerchantApplyMarginType;
import com.spark.library.acp.model.AcceptMerchantCancelDto;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.AcceptMerchantTrade;
import com.spark.library.acp.model.AcceptMerchantTradeDayDto;
import com.spark.library.acp.model.Dict;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultAcceptMerchantApplyMarginType;
import com.spark.library.acp.model.MessageResultAcceptMerchantFrontVo;
import com.spark.library.acp.model.MessageResultAcceptMerchantTrade;
import com.spark.library.acp.model.MessageResultListDict;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.okhttp.SendRemoteDataUtil;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.OtcUrls;
import com.spark.moduleotc.entity.AcceptMerchantInfoEntity;
import com.spark.moduleotc.entity.AcceptanceMerchantListEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.modulebase.base.BaseConstant.JSON_ERROR;
import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * otc业务模块
 */

public class AcceptanceMerchantControllerModel {
    private AcceptanceMerchantControllerApi acceptanceMerchantControllerApi;

    public AcceptanceMerchantControllerModel() {
        acceptanceMerchantControllerApi = new AcceptanceMerchantControllerApi();
        acceptanceMerchantControllerApi.setBasePath(OtcUrls.getInstance().getHost());
    }

    /**
     * 获取承兑商等级
     */

    public void getAcceptanceMerchantList(final ResponseCallBack.SuccessListener<List<Dict>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                acceptanceMerchantControllerApi.listAcceptMerchantTypeUsingGET(new Response.Listener<MessageResultListDict>() {
                    @Override
                    public void onResponse(MessageResultListDict response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            List<Dict> objList = response.getData();
                            if (successListener != null)
                                successListener.onResponse(objList);
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

    /**
     * 查找商户的认证等级
     */
    public void getSelfLevelInfo(final ResponseCallBack.SuccessListener<AcceptMerchantFrontVo> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                acceptanceMerchantControllerApi.findAcceptMerchantUsingGET(new Response.Listener<MessageResultAcceptMerchantFrontVo>() {
                    @Override
                    public void onResponse(MessageResultAcceptMerchantFrontVo response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            AcceptMerchantFrontVo acceptMerchantFrontVo = response.getData();
                            if (successListener != null)
                                successListener.onResponse(acceptMerchantFrontVo);
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

    /**
     * 认证申请
     */
    public void doApply(Long id, String assetImg, String detail, String tradeDataImg,
                        final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        if (id == null) {
            return;
        }
        final AcceptMerchantApplyInTransitDto acceptMerchantApplyInTransitDto = new AcceptMerchantApplyInTransitDto();
        acceptMerchantApplyInTransitDto.setApplyMarginId(id.intValue());
        acceptMerchantApplyInTransitDto.setAssetImg(assetImg);
        acceptMerchantApplyInTransitDto.setDetail(detail);
        acceptMerchantApplyInTransitDto.setTradeDataImg(tradeDataImg);
        LogUtils.e("认证申请参数:id==" + id + ",assetImg==" + assetImg + ",tradeDataImg==" + tradeDataImg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                acceptanceMerchantControllerApi.acceptMerchantApplyUsingPOST(acceptMerchantApplyInTransitDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.e("认证申请:" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getMessage());
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

    /**
     * 认证类型详情
     */
    public void getAcceptancesProcessInfo(final int type, final ResponseCallBack.SuccessListener<AcceptMerchantApplyMarginType> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                acceptanceMerchantControllerApi.listAcceptMerchantTypeUsingGET1(type, new Response.Listener<MessageResultAcceptMerchantApplyMarginType>() {
                    @Override
                    public void onResponse(MessageResultAcceptMerchantApplyMarginType response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            AcceptMerchantApplyMarginType acceptMerchantApplyMarginType = response.getData();
                            if (successListener != null)
                                successListener.onResponse(acceptMerchantApplyMarginType);
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

    /**
     * 认证商家退出申请
     */
    public void acceptMerchantCanel(String reason, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final AcceptMerchantCancelDto acceptMerchantCancelDto = new AcceptMerchantCancelDto();
        acceptMerchantCancelDto.setReason(reason);
        new Thread(new Runnable() {
            @Override
            public void run() {
                acceptanceMerchantControllerApi.acceptMerchantCanelUsingPOST(acceptMerchantCancelDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse(response.getMessage());
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

    /**
     * 查看自己商家交易信息
     */
    public void findAcceptMerchantTrade(final ResponseCallBack.SuccessListener<MessageResultAcceptMerchantTrade> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                acceptanceMerchantControllerApi.findAcceptMerchantTradeUsingGET(new Response.Listener<MessageResultAcceptMerchantTrade>() {
                    @Override
                    public void onResponse(MessageResultAcceptMerchantTrade response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null) {
                                successListener.onResponse(response);
                            }
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

    //查询派单前实名认证、承兑商身份认证、设置交易密码、设置收款方式状态
    public void findAuthenticationStatusUsingGET(final ResponseCallBack.SuccessListener<String> listener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                SendRemoteDataUtil.getInstance().doStringGetWithHead(OtcUrls.getInstance().findAuthenticationStatusUrl(), params, new SendRemoteDataUtil.DataCallback() {
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

    //我的资产-查询昨日佣金
    public void getTradeDayUsingPost(final String tradingDay, final ResponseCallBack.SuccessListener<AcceptMerchantInfoEntity> listener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put("tradingDay", tradingDay);
                SendRemoteDataUtil.getInstance().doStringPost(OtcUrls.getInstance().getTradeDayUsingPost(), params, new SendRemoteDataUtil.DataCallback() {
                    @Override
                    public void onDataLoaded(Object obj) {
                        try {
                            JSONObject jsonObject = new JSONObject(obj.toString());
                            int code = jsonObject.optInt("code");
                            String data = jsonObject.optString("message");
                            String url = jsonObject.optString("url");
                            if (code == SUCCESS_CODE) {
                                if (listener != null) {
                                    AcceptMerchantInfoEntity result = new Gson().fromJson(obj.toString(), AcceptMerchantInfoEntity.class);
                                    listener.onResponse(result);
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
     * 我的资产-查询累计佣金和待结算佣金
     */
    public void getTradeUsingGet(final ResponseCallBack.SuccessListener<AcceptanceMerchantListEntity> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                SendRemoteDataUtil.getInstance().doStringGet(OtcUrls.getInstance().getTradeUsingGet(), params, new SendRemoteDataUtil.DataCallback() {
                    @Override
                    public void onDataLoaded(Object obj) {
                        try {
                            JSONObject jsonObject = new JSONObject(obj.toString());
                            int code = jsonObject.optInt("code");
                            String data = jsonObject.optString("data");
                            String url = jsonObject.optString("url");
                            if (code == SUCCESS_CODE) {
                                if (successListener != null) {
                                    AcceptanceMerchantListEntity result = new Gson().fromJson(obj.toString(), AcceptanceMerchantListEntity.class);
                                    successListener.onResponse(result);
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
     * 我的资产-查询昨日佣金
     */
    public void getAcceptMerchantTradeDayUsingPOST(final String tradingDay, final ResponseCallBack.SuccessListener<MessageResult> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final AcceptMerchantTradeDayDto acceptMerchantTradeDayDto = new AcceptMerchantTradeDayDto();
        acceptMerchantTradeDayDto.setTradingDay(Integer.parseInt(tradingDay));
        new Thread(new Runnable() {
            @Override
            public void run() {
                acceptanceMerchantControllerApi.getAcceptMerchantTradeDayUsingPOST(acceptMerchantTradeDayDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
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
