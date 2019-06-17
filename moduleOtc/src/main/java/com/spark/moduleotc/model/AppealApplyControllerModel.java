package com.spark.moduleotc.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.acp.api.AppealApi;
import com.spark.library.acp.model.AppealApplyAchiveVo;
import com.spark.library.acp.model.AppealApplyInTransitVo;
import com.spark.library.acp.model.MessageResultPageAppealApplyAchiveVo;
import com.spark.library.acp.model.MessageResultPageAppealApplyInTransitVo;
import com.spark.library.acp.model.QueryCondition;
import com.spark.library.acp.model.QueryParam;
import com.spark.library.acp.model.QueryParamAppealApplyAchiveVo;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.moduleotc.OtcUrls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * 纠纷管理
 */
public class AppealApplyControllerModel {
    private AppealApi appealApi;

    public AppealApplyControllerModel() {
        this.appealApi = new AppealApi();
        this.appealApi.setBasePath(OtcUrls.getInstance().getHost());
    }


    /**
     * 查询进行中申诉单
     */
    public void findOrderIng(HashMap<String, String> params,final ResponseCallBack.SuccessListener<List<AppealApplyInTransitVo>> successListener,
                                final ResponseCallBack.ErrorListener errorListener) {
        final QueryParam queryParamOrderInTransit = new QueryParam();
        queryParamOrderInTransit.setPageIndex(Integer.parseInt(params.get("pageNo")));
        queryParamOrderInTransit.setPageSize(Integer.parseInt(params.get("pageSize")));
        queryParamOrderInTransit.setSortFields("createTime_d");
        List<QueryCondition> queryConditions = new ArrayList<>();
        /*QueryCondition queryCondition = new QueryCondition();
        queryCondition.setJoin("and");
        queryCondition.setOper("in");
        queryCondition.setKey("status");
        queryCondition.setValue(params.get("status"));
        queryConditions.add(queryCondition);*/
        queryParamOrderInTransit.setQueryList(queryConditions);
        new Thread(new Runnable() {
            @Override
            public void run() {
                appealApi.listAppealInTransitUsingPOST(queryParamOrderInTransit, new Response.Listener<MessageResultPageAppealApplyInTransitVo>() {
                    @Override
                    public void onResponse(MessageResultPageAppealApplyInTransitVo response) {
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
     * 查询已完成申诉单、已取消申诉单
     */
    public void findOrderFinish(HashMap<String, String> params,final ResponseCallBack.SuccessListener<List<AppealApplyAchiveVo>> successListener,
                                final ResponseCallBack.ErrorListener errorListener) {
        final QueryParamAppealApplyAchiveVo queryParamOrderInTransit = new QueryParamAppealApplyAchiveVo();
        queryParamOrderInTransit.setPageIndex(Integer.parseInt(params.get("pageNo")));
        queryParamOrderInTransit.setPageSize(Integer.parseInt(params.get("pageSize")));
        queryParamOrderInTransit.setSortFields("createTime_d");
        List<QueryCondition> queryConditions = new ArrayList<>();
        QueryCondition queryCondition = new QueryCondition();
        queryCondition.setJoin("and");
        queryCondition.setOper("in");
        queryCondition.setKey("a.status");
        queryCondition.setValue(params.get("status"));
        queryConditions.add(queryCondition);
        queryParamOrderInTransit.setQueryList(queryConditions);
        new Thread(new Runnable() {
            @Override
            public void run() {
                appealApi.listAppealAchiveUsingPOST(queryParamOrderInTransit, new Response.Listener<MessageResultPageAppealApplyAchiveVo>() {
                    @Override
                    public void onResponse(MessageResultPageAppealApplyAchiveVo response) {
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
