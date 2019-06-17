package com.spark.moduleotc.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.acp.api.OrderApi;
import com.spark.library.acp.model.MemberPayTypeDto;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultPageOrderAchive;
import com.spark.library.acp.model.MessageResultPageOrderInTransit;
import com.spark.library.acp.model.OrderAchive;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.library.acp.model.OrderPaymentDto;
import com.spark.library.acp.model.QueryCondition;
import com.spark.library.acp.model.QueryParamOrderAchive;
import com.spark.library.acp.model.QueryParamOrderInTransit;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.OtcUrls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * 订单业务
 */

public class OrderModel {
    private OrderApi orderApi;

    public OrderModel() {
        this.orderApi = new OrderApi();
        this.orderApi.setBasePath(OtcUrls.getInstance().getHost());
    }

    /**
     * 放行订单
     */
    public void ensureOrderRelease(final String businessId, final String tradePassword, final ResponseCallBack.SuccessListener<MessageResult> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                orderApi.ensureOrderReleaseUsingPOST(businessId, tradePassword, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
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

    /**
     * 取消订单
     */
    public void orderRefuse(final String businessId, final String tradePassword, final ResponseCallBack.SuccessListener<MessageResult> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                orderApi.orderRefuseUsingPOST(businessId, tradePassword, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
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

    /**
     * 查询在途订单
     */
    public void findOrderInTransit(final ResponseCallBack.SuccessListener<MessageResultPageOrderInTransit> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final QueryParamOrderInTransit queryParamOrderInTransit = new QueryParamOrderInTransit();
        queryParamOrderInTransit.setPageIndex(1);
        queryParamOrderInTransit.setPageSize(1);
        queryParamOrderInTransit.setSortFields("createTime_d");
        List<QueryCondition> queryConditions = new ArrayList<>();
        QueryCondition queryCondition = new QueryCondition();
        queryCondition.setJoin("and");
        queryCondition.setOper("in");
        queryCondition.setKey("status");
        queryCondition.setValue("1,2,12");
        queryConditions.add(queryCondition);
        queryParamOrderInTransit.setQueryList(queryConditions);
        new Thread(new Runnable() {
            @Override
            public void run() {
                orderApi.findOrderInTransitUsingPOST(queryParamOrderInTransit, new Response.Listener<MessageResultPageOrderInTransit>() {
                    @Override
                    public void onResponse(MessageResultPageOrderInTransit response) {
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


    /**
     * 确认订单支付完成
     */
    public void payComplete(String actualPayment, String orderNo, String tradePassword,
                            final ResponseCallBack.SuccessListener<MessageResult> successListener,
                            final ResponseCallBack.ErrorListener errorListener) {
        final OrderPaymentDto orderPaymentDto = new OrderPaymentDto();
        orderPaymentDto.setActualPayment(actualPayment);
        orderPaymentDto.setOrderNo(orderNo);
        orderPaymentDto.setTradePassword(tradePassword);
        new Thread(new Runnable() {
            @Override
            public void run() {
                orderApi.payCompleteUsingPOST2(orderPaymentDto, new Response.Listener<MessageResult>() {
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

    /**
     * 查询进行中订单、超时单
     */
    public void findOrderIng(HashMap<String, String> params, final ResponseCallBack.SuccessListener<List<OrderInTransit>> successListener,
                             final ResponseCallBack.ErrorListener errorListener) {
        final QueryParamOrderInTransit queryParamOrderInTransit = new QueryParamOrderInTransit();
        queryParamOrderInTransit.setPageIndex(Integer.parseInt(params.get("pageNo")));
        queryParamOrderInTransit.setPageSize(Integer.parseInt(params.get("pageSize")));
        queryParamOrderInTransit.setSortFields("createTime_d");
        List<QueryCondition> queryConditions = new ArrayList<>();
        QueryCondition queryCondition = new QueryCondition();
        if (params.get("status") != null) {
            queryCondition.setJoin("and");
            queryCondition.setOper("in");
            queryCondition.setKey("status");
            queryCondition.setValue(params.get("status"));
            queryConditions.add(queryCondition);
        }
        if (params.get("direction") != null && !params.get("direction").equals("0")) {
            queryCondition = new QueryCondition();
            queryCondition.setJoin("and");
            queryCondition.setOper("in");
            queryCondition.setKey("direction");
            queryCondition.setValue(params.get("direction"));
            queryConditions.add(queryCondition);
        }
        queryParamOrderInTransit.setQueryList(queryConditions);
        new Thread(new Runnable() {
            @Override
            public void run() {
                orderApi.findOrderInTransitUsingPOST(queryParamOrderInTransit, new Response.Listener<MessageResultPageOrderInTransit>() {
                    @Override
                    public void onResponse(MessageResultPageOrderInTransit response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null) {
                                successListener.onResponse(response.getData().getRecords());
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
     * 查询已完成订单、已取消订单
     */
    public void findOrderFinish(HashMap<String, String> params, final ResponseCallBack.SuccessListener<List<OrderAchive>> successListener,
                                final ResponseCallBack.ErrorListener errorListener) {
        final QueryParamOrderAchive queryParamOrderInTransit = new QueryParamOrderAchive();
        queryParamOrderInTransit.setPageIndex(Integer.parseInt(params.get("pageNo")));
        queryParamOrderInTransit.setPageSize(Integer.parseInt(params.get("pageSize")));
        queryParamOrderInTransit.setSortFields("createTime_d");
        List<QueryCondition> queryConditions = new ArrayList<>();
        QueryCondition queryCondition = new QueryCondition();
        if (params.get("status") != null) {
            queryCondition.setJoin("and");
            queryCondition.setOper("in");
            queryCondition.setKey("status");
            queryCondition.setValue(params.get("status"));
            queryConditions.add(queryCondition);
        }
        if (params.get("direction") != null && !params.get("direction").equals("0")) {
            queryCondition = new QueryCondition();
            queryCondition.setJoin("and");
            queryCondition.setOper("in");
            queryCondition.setKey("direction");
            queryCondition.setValue(params.get("direction"));
            queryConditions.add(queryCondition);
        }
        queryParamOrderInTransit.setQueryList(queryConditions);
        new Thread(new Runnable() {
            @Override
            public void run() {
                orderApi.findOrderOverUsingPOST(queryParamOrderInTransit, new Response.Listener<MessageResultPageOrderAchive>() {
                    @Override
                    public void onResponse(MessageResultPageOrderAchive response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null) {
                                successListener.onResponse(response.getData().getRecords());
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
