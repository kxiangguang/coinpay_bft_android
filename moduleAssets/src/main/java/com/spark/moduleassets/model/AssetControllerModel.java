package com.spark.moduleassets.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.library.ac.api.AssetControllerApi;
import com.spark.library.ac.model.AssetTransferDto;
import com.spark.library.ac.model.AssetWithdrawDto;
import com.spark.library.ac.model.MemberTransactionVo;
import com.spark.library.ac.model.MemberWithdrawAddressDto;
import com.spark.library.ac.model.MessageResult;
import com.spark.library.ac.model.MessageResultListCoin;
import com.spark.library.ac.model.MessageResultListMemberWalletVo;
import com.spark.library.ac.model.MessageResultListMemberWithdrawAddress;
import com.spark.library.ac.model.MessageResultPageMemberTransactionVo;
import com.spark.library.ac.model.QueryCondition;
import com.spark.library.ac.model.QueryParamMemberTransactionVo;
import com.spark.moduleassets.AcUrls;
import com.spark.moduleassets.entity.Address;
import com.spark.moduleassets.entity.AssetRecord;
import com.spark.moduleassets.entity.Coin;
import com.spark.moduleassets.entity.ExtractInfo;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * 资产
 */

public class AssetControllerModel {
    private AssetControllerApi assetControllerApi;

    public AssetControllerModel() {
        assetControllerApi = new AssetControllerApi();
        assetControllerApi.setBasePath(AcUrls.getInstance().getHost());
    }

    /**
     * 增加用户某个币种的提币地址信息
     */
    public void addWalletWithdrawAddressUsing(String address, String coinId, String remark,
                                              final ResponseCallBack.SuccessListener<String> successListener,
                                              final ResponseCallBack.ErrorListener errorListener) {
        final MemberWithdrawAddressDto memberWithdrawAddressDto = new MemberWithdrawAddressDto();
        memberWithdrawAddressDto.setAddress(address);
        memberWithdrawAddressDto.setCoinId(coinId);
        memberWithdrawAddressDto.setRemark(remark);
        new Thread(new Runnable() {
            @Override
            public void run() {
                assetControllerApi.addWalletWithdrawAddressUsingPOST(memberWithdrawAddressDto, new Response.Listener<MessageResult>() {
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
     * 删除提币地址信息
     */
    public void delWalletWithdrawAddressUsingGET(final String id,
                                                 final ResponseCallBack.SuccessListener<String> successListener,
                                                 final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                assetControllerApi.delWalletWithdrawAddressUsingGET(id, new Response.Listener<MessageResult>() {
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
     * 查询用户某个业务所有资产交易信息
     */
    public void findWalletUsing(final String type, final ResponseCallBack.SuccessListener<List<Wallet>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                assetControllerApi.findWalletUsingGET(type, new Response.Listener<MessageResultListMemberWalletVo>() {
                    @Override
                    public void onResponse(MessageResultListMemberWalletVo response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            List<Wallet> list = gson.fromJson(gson.toJson(response.getData()), new TypeToken<List<Wallet>>() {
                            }.getType());
                            if (successListener != null)
                                successListener.onResponse(list);
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
     * 查询平台支持的币种信息
     */
    public void findSupportCoin(final ResponseCallBack.SuccessListener<List<Coin>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                assetControllerApi.findSupportCoinUsingGET(new Response.Listener<MessageResultListCoin>() {
                    @Override
                    public void onResponse(MessageResultListCoin response) {
                        LogUtils.i("response==" + response);
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            List<Coin> list = gson.fromJson(gson.toJson(response.getData()), new TypeToken<List<Coin>>() {
                            }.getType());
                            if (successListener != null)
                                successListener.onResponse(list);
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
     * 资金划转
     */
    public void walletWithdraw(BigDecimal amount, String coinName, AssetTransferDto.FromEnum from, AssetTransferDto.ToEnum to, String tradePassword,
                               final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final AssetTransferDto assetTransferDto = new AssetTransferDto();
        assetTransferDto.setAmount(amount);
        assetTransferDto.setCoinName(coinName);
        assetTransferDto.setFrom(from);
        assetTransferDto.setTo(to);
        assetTransferDto.setTradePassword(tradePassword);
        new Thread(new Runnable() {
            @Override
            public void run() {
                assetControllerApi.walletWithdrawUsingPOST(assetTransferDto, new Response.Listener<MessageResult>() {
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
     * 查询平台支持的业务钱包信息
     */
    public void findSupportAsset(final String coinName, final ResponseCallBack.SuccessListener<List<ExtractInfo>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                assetControllerApi.findSupportCoinUsingGET(new Response.Listener<MessageResultListCoin>() {
                    @Override
                    public void onResponse(MessageResultListCoin response) {
                        LogUtils.i("response==" + response);
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            List<ExtractInfo> list = gson.fromJson(gson.toJson(response.getData()), new TypeToken<List<ExtractInfo>>() {
                            }.getType());
                            if (successListener != null)
                                successListener.onResponse(list);
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
     * 查询用户的某个币种的提币地址信息
     */
    public void findWalletWithdrawAddress(final String coinName,
                                          final ResponseCallBack.SuccessListener<List<Address>> successListener,
                                          final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                assetControllerApi.findWalletWithdrawAddressUsingPOST(new Response.Listener<MessageResultListMemberWithdrawAddress>() {
                    @Override
                    public void onResponse(MessageResultListMemberWithdrawAddress response) {
                        LogUtils.i("response==" + response);
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            List<Address> list = gson.fromJson(gson.toJson(response.getData()), new TypeToken<List<Address>>() {
                            }.getType());
                            if (successListener != null) {
                                if (StringUtils.isNotEmpty(coinName)) {
                                    successListener.onResponse(getAddressByCoinName(coinName, list));
                                } else {
                                    successListener.onResponse(list);
                                }
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

    /**
     * 用户提币请求
     */
    public void walletWithdraw(String address, BigDecimal amount, String coinName, String tradePassword,
                               final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final AssetWithdrawDto assetWithdrawDto = new AssetWithdrawDto();
        assetWithdrawDto.setAddress(address);
        assetWithdrawDto.setAmount(amount);
        assetWithdrawDto.setCoinName(coinName);
        assetWithdrawDto.setTradePassword(tradePassword);
        new Thread(new Runnable() {
            @Override
            public void run() {
                assetControllerApi.walletWithdrawUsingPOST1(assetWithdrawDto, new Response.Listener<MessageResult>() {
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

    private List<Address> getAddressByCoinName(String coinName, List<Address> list) {
        List<Address> newList = new ArrayList<>();
        for (Address address : list) {
            if (coinName.equals(address.getCoinId())) {
                newList.add(address);
            }
        }
        return newList;
    }


    /**
     * 查询用户某个业务所有资产交易信息
     */
    /**
     * @param type业务类型        0充值1.提现2转到合约账户，3.币币交易，4.法币买入，5.法币卖出，6.活动奖励，7.推广奖励，8.分红，9.投票，10.人工充值，11配对
     *                        12.缴纳商家认证保证金，13.退回商家认证保证金，15.法币充值，16.币币兑换，17.渠道推广，19.注册奖励，20.实名奖励，21.推广注册奖励，22.推广实名奖励，23.推广法币交易奖励，24.推广币币交易奖励，25.空投，26.锁仓 31.资金划转-资金转入 32.资金划转-资金转出
     * @param busiType
     * @param successListener
     * @param errorListener
     */
    public void findAssetTransLog(final Integer type, Integer subType, HashMap<String, String> params, final String busiType, final ResponseCallBack.SuccessListener<List<AssetRecord>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final QueryParamMemberTransactionVo queryParamOrderInTransit = new QueryParamMemberTransactionVo();
        queryParamOrderInTransit.setPageIndex(Integer.parseInt(params.get("pageNo")));
        queryParamOrderInTransit.setPageSize(Integer.parseInt(params.get("pageSize")));
        queryParamOrderInTransit.setSortFields("createTime_d");
        List<QueryCondition> queryConditions = new ArrayList<>();
        QueryCondition queryCondition = new QueryCondition();
        if (type != null) {
            queryCondition.setJoin("and");
            queryCondition.setOper("in");
            queryCondition.setKey("type");
            queryCondition.setValue(type);
            queryConditions.add(queryCondition);
        }
        if (subType != null) {
            queryCondition = new QueryCondition();
            queryCondition.setJoin("and");
            queryCondition.setOper("in");
            queryCondition.setKey("subType");
            queryCondition.setValue(subType);
            queryConditions.add(queryCondition);
        }
        queryCondition = new QueryCondition();
        queryCondition.setJoin("and");
        queryCondition.setOper("!=");
        queryCondition.setKey("amount");
        queryCondition.setValue(0);
        queryConditions.add(queryCondition);
        queryParamOrderInTransit.setQueryList(queryConditions);
        new Thread(new Runnable() {
            @Override
            public void run() {
                assetControllerApi.findWalletTranslogUsingPOST(queryParamOrderInTransit, busiType, new Response.Listener<MessageResultPageMemberTransactionVo>() {
                    @Override
                    public void onResponse(MessageResultPageMemberTransactionVo response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null) {
                                Gson gson = new Gson();
                                List<AssetRecord> list = gson.fromJson(gson.toJson(response.getData().getRecords()), new TypeToken<List<AssetRecord>>() {
                                }.getType());
                                successListener.onResponse(list);
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
