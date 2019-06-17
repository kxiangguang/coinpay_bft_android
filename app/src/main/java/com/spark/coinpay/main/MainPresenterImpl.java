package com.spark.coinpay.main;

import com.android.volley.VolleyError;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.MessageResultAcceptMerchantTrade;
import com.spark.library.acp.model.MessageResultPageOrderInTransit;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.moduleassets.model.AssetControllerModel;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulelogin.entity.CasLoginEntity;
import com.spark.modulelogin.model.CasLoginModel;
import com.spark.moduleotc.model.AcceptanceMerchantControllerModel;
import com.spark.moduleotc.model.AppVersionModel;
import com.spark.moduleotc.model.OrderModel;

import java.util.HashMap;
import java.util.List;

/**
 * 主界面
 */

public class MainPresenterImpl implements MainContract.MainPresenter {
    private MainContract.MainView mainView;
    private CasLoginModel casLoginModel;
    private AcceptanceMerchantControllerModel acceptanceMerchantControllerModel;
    private OrderModel orderModel;
    private AssetControllerModel assetControllerModel;
    private AppVersionModel appVersionModel;

    public MainPresenterImpl(MainContract.MainView mainView) {
        this.mainView = mainView;
        casLoginModel = new CasLoginModel();
        acceptanceMerchantControllerModel = new AcceptanceMerchantControllerModel();
        orderModel = new OrderModel();
        assetControllerModel = new AssetControllerModel();
        appVersionModel = new AppVersionModel();
    }

    @Override
    public void findAcceptMerchantTrade() {
        showLoading();
        acceptanceMerchantControllerModel.findAcceptMerchantTrade(new ResponseCallBack.SuccessListener<MessageResultAcceptMerchantTrade>() {
            @Override
            public void onResponse(MessageResultAcceptMerchantTrade response) {
                hideLoading();
                if (mainView != null) {
                    mainView.findAcceptMerchantTradeSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mainView != null) {
                    mainView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mainView != null) {
                    mainView.dealError(volleyError);
                }
            }
        });
    }

    //订单承接通知,收到该指令，说明承兑商有订单匹配，此时需要查询在途订单
    @Override
    public void findOrderInTransit() {
        orderModel.findOrderInTransit(new ResponseCallBack.SuccessListener<MessageResultPageOrderInTransit>() {
            @Override
            public void onResponse(MessageResultPageOrderInTransit response) {
                hideLoading();
                if (mainView != null) {
                    mainView.findOrderInTransitSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mainView != null) {
                    mainView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mainView != null) {
                    mainView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void ensureOrderRelease(String businessId, String passWord) {
        showLoading();
        orderModel.ensureOrderRelease(businessId, passWord, new ResponseCallBack.SuccessListener<MessageResult>() {
            @Override
            public void onResponse(MessageResult response) {
                hideLoading();
                if (mainView != null) {
                    mainView.releaseOrderSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mainView != null) {
                    mainView.releaseOrderFail(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mainView != null) {
                    mainView.dealError(volleyError);
                }
            }
        });
    }

    //查询派单前实名认证、承兑商身份认证、设置交易密码、设置收款方式状态
    @Override
    public void findAuthenticationStatus() {
        showLoading();
        acceptanceMerchantControllerModel.findAuthenticationStatusUsingGET(new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (mainView != null) {
                    mainView.findAuthenticationStatusSuccess(response);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mainView != null) {
                    mainView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mainView != null) {
                    mainView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void checkBusinessLogin(String type) {
        showLoading();
        casLoginModel.checkBusinessLogin(type, new ResponseCallBack.SuccessListener<CasLoginEntity>() {
            @Override
            public void onResponse(CasLoginEntity response) {
                hideLoading();
                if (mainView != null)
                    mainView.checkBusinessLoginSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mainView != null)
                    mainView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mainView != null)
                    mainView.dealError(volleyError);
            }
        });
    }

    @Override
    public void doLoginBusiness(String tgc, String type) {
        showLoading();
        casLoginModel.getBussinessTicket(tgc, type, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (mainView != null)
                    mainView.doLoginBusinessSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mainView != null)
                    mainView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mainView != null)
                    mainView.dealError(volleyError);
            }
        });
    }


    @Override
    public void loginOut() {
        showLoading();
        String tgt = SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getTgt();
        casLoginModel.loginOut(tgt, new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (mainView != null)
                    mainView.loginOutSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mainView != null)
                    mainView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mainView != null)
                    mainView.dealError(volleyError);
            }
        });
    }

    @Override
    public void getOrder(boolean isShow, HashMap<String, String> params) {
        if (isShow)
            showLoading();
        orderModel.findOrderIng(params, new ResponseCallBack.SuccessListener<List<OrderInTransit>>() {
            @Override
            public void onResponse(List<OrderInTransit> list) {
                hideLoading();
                if (mainView != null) {
                    mainView.getOrderSuccess(list);
                }
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mainView != null) {
                    mainView.dealError(httpErrorEntity);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mainView != null) {
                    mainView.dealError(volleyError);
                }
            }
        });
    }

    @Override
    public void getWallet(final String type) {
        showLoading();
        assetControllerModel.findWalletUsing(type, new ResponseCallBack.SuccessListener<List<Wallet>>() {
            @Override
            public void onResponse(List<Wallet> response) {
                hideLoading();
                if (mainView != null)
                    mainView.getWalletSuccess(type, response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mainView != null)
                    mainView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mainView != null)
                    mainView.dealError(volleyError);
            }
        });
    }

    @Override
    public void checkVersion() {
        showLoading();
        appVersionModel.getAppVersion(new ResponseCallBack.SuccessListener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                if (mainView != null)
                    mainView.checkVersionSuccess(response);
            }
        }, new ResponseCallBack.ErrorListener() {
            @Override
            public void onErrorResponse(HttpErrorEntity httpErrorEntity) {
                hideLoading();
                if (mainView != null)
                    mainView.dealError(httpErrorEntity);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideLoading();
                if (mainView != null)
                    mainView.dealError(volleyError);
            }
        });
    }

    @Override
    public void showLoading() {
        if (mainView != null)
            mainView.showLoading();
    }

    @Override
    public void hideLoading() {
        if (mainView != null)
            mainView.hideLoading();
    }

    @Override
    public void destory() {
        mainView = null;
    }
}
