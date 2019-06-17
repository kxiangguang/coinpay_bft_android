package com.spark.coinpay.my;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.library.acp.model.Dict;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.LogUtils;
import com.spark.moduleotc.model.AcceptanceMerchantControllerModel;
import com.spark.moduleuc.MemberControllerModel;

import java.util.List;

/**
 * 承兑商
 */

public class MyPresenterImpl implements MyContract.MyPresenter {
    private MyContract.MyView mView;
    private AcceptanceMerchantControllerModel acceptanceMerchantControllerModel;
    private MemberControllerModel memberControllerModel;

    public MyPresenterImpl(MyContract.MyView acceptancesLevelView) {
        this.mView = acceptancesLevelView;
        acceptanceMerchantControllerModel = new AcceptanceMerchantControllerModel();
        memberControllerModel = new MemberControllerModel();
    }

    @Override
    public void getLevelList() {
        showLoading();
        acceptanceMerchantControllerModel.getAcceptanceMerchantList(new ResponseCallBack.SuccessListener<List<Dict>>() {
            @Override
            public void onResponse(List<Dict> response) {
                hideLoading();
                if (mView != null)
                    mView.getLevelListSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mView != null)
                    mView.getLevelListError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mView != null)
                    mView.dealError(volleyError);
            }
        });
    }

    @Override
    public void getSelfLevelInfo() {
        showLoading();
        acceptanceMerchantControllerModel.getSelfLevelInfo(new ResponseCallBack.SuccessListener<AcceptMerchantFrontVo>() {
            @Override
            public void onResponse(AcceptMerchantFrontVo response) {
                LogUtils.i("response==" + response.toString());
                hideLoading();
                if (mView != null)
                    mView.getSelfLevelInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mView != null)
                    mView.getSelfLevelInfoError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mView != null)
                    mView.dealError(volleyError);
            }
        });
    }

    //查询派单前实名认证、承兑商身份认证、设置交易密码、设置收款方式状态
    public void findAuthenticationStatus() {
        showLoading();
        acceptanceMerchantControllerModel.findAuthenticationStatusUsingGET(new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (mView != null) {
                    mView.findAuthenticationStatusSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mView != null) {
                    mView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mView != null) {
                    mView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void getUserInfo() {
        memberControllerModel.getUserInfo(new ResponseCallBack.SuccessListener<User>() {
            @Override
            public void onResponse(User response) {
                hideLoading();
                if (mView != null)
                    mView.getUserInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mView != null)
                    mView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mView != null)
                    mView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (mView != null)
            mView.showLoading();

    }

    @Override
    public void hideLoading() {
        if (mView != null)
            mView.hideLoading();
    }

    @Override
    public void destory() {
        mView = null;
    }
}
