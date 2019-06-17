package com.spark.moduleuc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.library.uc.api.RegisterControllerApi;
import com.spark.library.uc.model.MessageResult;
import com.spark.library.uc.model.MessageResultListCountry;
import com.spark.library.uc.model.RegisterByEmailDto;
import com.spark.library.uc.model.RegisterByPhoneDto;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.okhttp.SendRemoteDataUtil;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.callback.ResponseCallBack;
import com.spark.modulebase.utils.StringUtils;
import com.spark.moduleuc.entity.CountryEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import static com.spark.modulebase.base.BaseConstant.JSON_ERROR;
import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * 注册
 */

public class RegisterControllerModel {
    private RegisterControllerApi registerControllerApi;

    public RegisterControllerModel() {
        registerControllerApi = new RegisterControllerApi();
        registerControllerApi.setBasePath(UcUrls.getInstance().getHost());
    }

    /**
     * 获取国家列表
     */
    public void getCountry(final ResponseCallBack.SuccessListener<List<CountryEntity>> successListener, final ResponseCallBack.ErrorListener errorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                registerControllerApi.findSupportCountryUsingPOST(new Response.Listener<MessageResultListCountry>() {
                    @Override
                    public void onResponse(MessageResultListCountry response) {
                        LogUtils.i("response==" + response.toString());
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            Gson gson = new Gson();
                            List<CountryEntity> objList = gson.fromJson(gson.toJson(response.getData()), new TypeToken<List<CountryEntity>>() {
                            }.getType());
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
     * 邮箱注册
     *
     * @param username
     * @param password
     * @param country
     * @param promotion
     * @param email
     * @param code
     */
    public void doSighUpByEmail(String username, String password, String country, String promotion, String email, String code,
                                final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        registerControllerApi.addHeader("check", "email:" + email + ":" + code);
        final RegisterByEmailDto registerByEmailDto = new RegisterByEmailDto();
        registerByEmailDto.setCountry(country);
        registerByEmailDto.setEmail(email);
        registerByEmailDto.setPassword(password);
        registerByEmailDto.setPromotion(promotion);
        registerByEmailDto.setTid(AppUtils.getSerialNumber());
        registerByEmailDto.setUsername(username);
        new Thread(new Runnable() {
            @Override
            public void run() {
                registerControllerApi.registerByEmailUsingPOST(registerByEmailDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
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
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 手机注册
     *
     * @param username
     * @param password
     * @param country
     * @param promotion
     * @param phone
     * @param code
     */
    public void doSighUpByPhone(String username, String password, String country, String promotion, String phone, String code,
                                final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        registerControllerApi.addHeader("check", "phone:" + phone + ":" + code);
        final RegisterByPhoneDto registerByPhoneDto = new RegisterByPhoneDto();
        registerByPhoneDto.setCountry(country);
        registerByPhoneDto.setMobilePhone(phone);
        registerByPhoneDto.setPassword(password);
        registerByPhoneDto.setInviteCode(promotion);
//        registerByPhoneDto.setPromotion(promotion);
        registerByPhoneDto.setTid(AppUtils.getSerialNumber());
        registerByPhoneDto.setUsername(username);
        new Thread(new Runnable() {
            @Override
            public void run() {
                registerControllerApi.registerByPhoneUsingPOST(registerByPhoneDto, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        LogUtils.i("response==" + response.toString());
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
     * 校验推荐码
     *
     * @param params
     */
    public void checkInviteCode(String promotionCode, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        SendRemoteDataUtil.getInstance().doStringGet(AgentUrls.getInstance().checkInviteCode() + promotionCode, map, new SendRemoteDataUtil.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                LogUtils.i("response==" + response.toString());
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


    public void getImg(final ResponseCallBack.SuccessListener<InputStream> successListener, final ResponseCallBack.ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        SendRemoteDataUtil.getInstance().doStringGetForImg(UcUrls.getInstance().getImg(), map, new SendRemoteDataUtil.ImgDataCallback() {
            @Override
            public void onDataLoaded(okhttp3.Response response) {
                try {
                    InputStream inputStream = response.body().byteStream();
                    if (successListener != null)
                        successListener.onResponse(inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (errorListener != null)
                        errorListener.onErrorResponse(new HttpErrorEntity(JSON_ERROR, "图片解析异常", "", ""));
                }
            }

            @Override
            public void onDataNotAvailable(HttpErrorEntity httpErrorEntity) {
                if (errorListener != null)
                    errorListener.onErrorResponse(httpErrorEntity);
            }

            @Override
            public void onDataLoaded(Object response) {

            }
        });
    }

}
