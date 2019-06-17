package com.spark.coinpay.my.set;


import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulelogin.model.CasLoginModel;
import com.spark.moduleotc.model.SettingModel;
import com.spark.moduleuc.MemberControllerModel;
import com.spark.moduleuc.UcModel;


/**
 * Created by Administrator on 2017/9/25.
 */

public class SetPresenterImpl implements SetContract.SetPresenter {
    private SetContract.SetView setView;
    private CasLoginModel casLoginModel;
    private SettingModel settingModel;
    private MemberControllerModel memberControllerModel;

    public SetPresenterImpl(SetContract.SetView SetView) {
        this.setView = SetView;
        casLoginModel = new CasLoginModel();
        settingModel = new SettingModel();
        memberControllerModel = new MemberControllerModel();
    }


    @Override
    public void loginOut() {
        showLoading();
        String tgt = SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getTgt();
        casLoginModel.loginOut(tgt, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (setView != null)
                    setView.loginOutSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (setView != null)
                    setView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (setView != null)
                    setView.dealError(volleyError);
            }
        });
    }

    @Override
    public void getAutoRelease() {
        showLoading();
        settingModel.getAutoRelease(new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (setView != null)
                    setView.getAutoReleaseSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (setView != null)
                    setView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (setView != null)
                    setView.dealError(volleyError);
            }
        });
    }

    @Override
    public void updateAutoRelease(int autoReleaseSwitch) {
        showLoading();
        settingModel.updateAutoRelease(autoReleaseSwitch, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (setView != null)
                    setView.updateAutoReleaseSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (setView != null)
                    setView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (setView != null)
                    setView.dealError(volleyError);
            }
        });
    }

    @Override
    public void getUserInfo() {
        memberControllerModel.getUserInfo(new ResponseCallBack.SuccessListener<User>() {
            @Override
            public void onResponse(User response) {
                hideLoading();
                if (setView != null)
                    setView.getUserInfoSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (setView != null)
                    setView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (setView != null)
                    setView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (setView != null)
            setView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (setView != null)
            setView.hideLoading();
    }

    @Override
    public void destory() {
        setView = null;
    }
}
