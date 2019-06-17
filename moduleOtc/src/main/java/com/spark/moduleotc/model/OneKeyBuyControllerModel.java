package com.spark.moduleotc.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.acp.api.OneKeyBuyControllerApi;
import com.spark.library.acp.model.EnterpriseOrderQueryDto;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OneKeyBuyDto;
import com.spark.library.acp.model.OrderCompleteDto;
import com.spark.library.acp.model.QueryCondition;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.OtcUrls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * 一键买币
 */

public class OneKeyBuyControllerModel {
    private OneKeyBuyControllerApi oneKeyBuyControllerApi;

    public OneKeyBuyControllerModel() {
        this.oneKeyBuyControllerApi = new OneKeyBuyControllerApi();
        this.oneKeyBuyControllerApi.setBasePath(OtcUrls.getInstance().getHost());
    }

    /**
     * 一键买币订单请求入口
     */
    public void createOrder(final OneKeyBuyDto oneKeyBuyDto,
                            final ResponseCallBack.SuccessListener<MessageResult> successListener,
                            final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                oneKeyBuyControllerApi.oneKeyBuyUsingPOST(oneKeyBuyDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        if (response != null) {
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
     * 订单支付完成接口
     */
    public void oneKeyPayComplete(String actualPayment, String orderNo,
                                  final ResponseCallBack.SuccessListener<MessageResult> successListener,
                                  final ResponseCallBack.ErrorListener errorListener) {
        final OrderCompleteDto orderCompleteDto = new OrderCompleteDto();
        orderCompleteDto.setActualPayment(actualPayment);
        orderCompleteDto.setOrderSn(orderNo);
        new Thread(new Runnable() {
            @Override
            public void run() {
                oneKeyBuyControllerApi.payCompleteUsingPOST1(orderCompleteDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        if (response != null) {
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
     * 查询订单进行中/已完成/已取消
     */
    public void findOrderIng(HashMap<String, String> params, final ResponseCallBack.SuccessListener<MessageResult> successListener,
                             final ResponseCallBack.ErrorListener errorListener) {
        final EnterpriseOrderQueryDto enterpriseOrderQueryDto = new EnterpriseOrderQueryDto();
        enterpriseOrderQueryDto.setPageNo(Integer.parseInt(params.get("pageNo")));
        enterpriseOrderQueryDto.setPageSize(Integer.parseInt(params.get("pageSize")));
        enterpriseOrderQueryDto.setQueryType(Integer.parseInt(params.get("queryType")));
        //enterpriseOrderQueryDto.setSortFields("createTime_d");
        //        List<QueryCondition> queryConditions = new ArrayList<>();
        //        QueryCondition queryCondition = new QueryCondition();
        //        if (params.get("queryType") != null) {
        //            queryCondition.setJoin("and");
        //            queryCondition.setOper("in");
        //            queryCondition.setKey("queryType");
        //            queryCondition.setValue(params.get("queryType"));
        //            queryConditions.add(queryCondition);
        //        }
        //        enterpriseOrderQueryDto.setQueryList(queryConditions);
        new Thread(new Runnable() {
            @Override
            public void run() {
                oneKeyBuyControllerApi.payCompleteUsingPOST(enterpriseOrderQueryDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        if (response != null) {
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
     * 取消订单
     */
    public void canceleOrder(final OrderCompleteDto orderCompleteDto,
                             final ResponseCallBack.SuccessListener<MessageResult> successListener,
                             final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                oneKeyBuyControllerApi.orderCancelUsingPOST(orderCompleteDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        if (response != null) {
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
