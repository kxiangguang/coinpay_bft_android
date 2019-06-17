package com.spark.coinpay.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.LoginStatus;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.entity.Captcha;
import com.spark.coinpay.main.MainActivity;
import com.spark.coinpay.my.forgot_pwd.ForgotPwdActivity;
import com.spark.coinpay.signup.SignUpActivity;
import com.spark.coinpay.view.PhoneVertifyDialog;
import com.spark.moduleassets.AcUrls;
import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.KeyboardUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulebase.utils.StringFormatUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulelogin.CasConstant;
import com.spark.modulelogin.LoginUrls;
import com.spark.moduleotc.OtcUrls;
import com.spark.moduleuc.AgentUrls;
import com.spark.moduleuc.UcUrls;
import com.spark.netty_library.GuardService;
import com.spark.netty_library.WebSocketService;

import org.json.JSONObject;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.modulebase.base.BaseConstant.CAPTCH;
import static com.spark.modulebase.base.BaseConstant.CAPTCH2;
import static com.spark.modulebase.base.BaseConstant.ERROR_CODE;
import static com.spark.modulebase.base.BaseConstant.JSON_ERROR;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_API_ADDRESS;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_APP_NAME;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API_AC;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API_ACP;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API_UC;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_clearCode310;

/**
 * 登录
 */

public class LoginActivity extends BaseActivity implements LoginContract.LoginView {
    public static final int RETURN_LOGIN = 0;

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvForgetPas)
    TextView tvForgetPas;
    @BindView(R.id.ivEye)
    ImageView ivEye;
    @BindView(R.id.tvSign)
    TextView tvSign;
    @BindView(R.id.tvApiAddress)
    TextView tvApiAddress;

    private LoginPresenterImpl presenter;
    private String gtc;
    private boolean isCasLogin;
    private PhoneVertifyDialog mPhoneVertifyDialog;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String cid;
    private String strAreaCode = "86";
    private LoginStatus loginStatus;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            String username = bundle.getString("account");
            etUsername.setText(username);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivEye.setTag(false);
        initApiAddress();
    }

    /**
     * 是否是线上环境:true线上环境/false混合环境
     */
    private static final boolean isOnline = true;

    private void initApiAddress() {
        if (isOnline) {
            tvApiAddress.setVisibility(View.GONE);

//            String appName = "欧沃";
//            String apiAddress = "caymanex.pro";

//            String appName = "合众承兑";
//            String apiAddress = "555hub.com";

//            String appName = "币承兑";
//            String apiAddress = "bittoppayment.top";

//            String appName = "亿易商贸";
//            String apiAddress = "dbipay.com";

//            String appName = "通支付";
//                        String apiAddress = "tongzhifu.vip";

//            String appName = "鑫众支付";
//            String apiAddress = "brick.asia";

//            String appName = "币承兑";
//            String apiAddress = "bp.wxmarket.cn";

//            String appName = "Money承兑";
//            String apiAddress = "money123.vip";

            String appName = "蚂蚁搬砖";
            String apiAddress = "1688up.net";

//            String appName = "网思";
//            String apiAddress = "51ws.vip";


            //保存API地址
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_API_ADDRESS, apiAddress);
            //保存APP名称
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_APP_NAME, appName);
            //是否填写API
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_WRITE_API, false);
            //保存UC地址
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_WRITE_API_UC, "");
            //保存AC地址
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_WRITE_API_AC, "");
            //保存ACP地址
            SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_WRITE_API_ACP, "");

            GlobalConstant.isTest = false;
            GuardService.isTest = false;
            WebSocketService.isTest = false;

            GlobalConstant.CUR_HOST = apiAddress;
            WebSocketService.setHost(apiAddress);
            GuardService.setHost(apiAddress);

            LoginUrls.getInstance().setHost(GlobalConstant.getHost() + "/" + GlobalConstant.TYPE_UC);
            LoginUrls.getInstance().setHostBusiness(GlobalConstant.getHost());
            LoginUrls.getInstance().setHostLogin(GlobalConstant.getHostLogin());
            AcUrls.getInstance().setHost(GlobalConstant.getHostAC());
            UcUrls.getInstance().setHost(GlobalConstant.getHostUC());
            OtcUrls.getInstance().setHost(GlobalConstant.getHostOTC());
            AgentUrls.getInstance().setHost(GlobalConstant.getHostAgent());
        } else {
            tvApiAddress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        loginStatus = new LoginStatus();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String username = bundle.getString("username");
            if (StringUtils.isNotEmpty(username)) {
                etUsername.setText(username);
                etPassword.requestFocus();
            }
        } else {
            //显示手机号码
            String username = SharedPreferencesUtil.getInstance(activity).getStringParam(SharedPreferencesUtil.SP_KEY_LOGIN_ACCOUNT);
            if (StringUtils.isNotEmpty(username)) {
                etUsername.setText(username);
                etPassword.requestFocus();
            }
        }
        String htm = "<font color=#6C6E8A>" + getString(R.string.str_users_please_sigh_in_before) + "</font>" + getString(R.string.str_users_please_sigh_in_after) + ">>";
        tvSign.setText(Html.fromHtml(htm));
        presenter = new LoginPresenterImpl(this);
    }

    @OnClick({R.id.tvForgetPas, R.id.tvLogin, R.id.ivEye, R.id.tvSign, R.id.tvApiAddress})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvForgetPas:
                if (!isOnline) {
                    String apiAddress = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_API_ADDRESS);
                    if (StringUtils.isEmpty(apiAddress)) {
                        ToastUtils.showToast("请先设置API地址");
                        return;
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putString("phone", etUsername.getText().toString());
                showActivity(ForgotPwdActivity.class, bundle, 1);
                break;
            case R.id.tvLogin:
                checkInput();
                break;
            case R.id.ivEye:
                isShowVisible();
                break;
            case R.id.tvSign:
                if (!isOnline) {
                    String apiAddress = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_API_ADDRESS);
                    if (StringUtils.isEmpty(apiAddress)) {
                        ToastUtils.showToast("请先设置API地址");
                        return;
                    }
                }
                showActivity(SignUpActivity.class, null, 1);
                break;
            case R.id.tvApiAddress:
                showActivity(ApiActivity.class, null);
                break;
        }
    }

    /**
     * 是否可见
     */
    private void isShowVisible() {
        boolean isVisible = (boolean) ivEye.getTag();
        if (!isVisible) {
            isVisible = true;
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivEye.setImageResource(R.mipmap.icon_eye_open);
        } else {
            isVisible = false;
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivEye.setImageResource(R.mipmap.icon_eye_close);
        }
        ivEye.setTag(isVisible);
    }

    /**
     * 检查用户名和密码后进行登录
     */
    @Override
    protected void checkInput() {
        super.checkInput();
        MyApplication.getAppContext().deleteCurrentUser();
        MyApplication.getAppContext().getCookieManager().getCookieStore().removeAll();

        if (!isOnline) {
            boolean isTest = SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_WRITE_API);
            if (!isTest) {
                String apiAddress = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_API_ADDRESS);
                if (StringUtils.isEmpty(apiAddress)) {
                    ToastUtils.showToast("请先设置API地址");
                    return;
                }

                GlobalConstant.isTest = false;
                GuardService.isTest = false;
                WebSocketService.isTest = false;

                GlobalConstant.CUR_HOST = apiAddress;
                WebSocketService.setHost(apiAddress);
                GuardService.setHost(apiAddress);

                LoginUrls.getInstance().setHost(GlobalConstant.getHost() + "/" + GlobalConstant.TYPE_UC);
                LoginUrls.getInstance().setHostBusiness(GlobalConstant.getHost());
                LoginUrls.getInstance().setHostLogin(GlobalConstant.getHostLogin());
                AcUrls.getInstance().setHost(GlobalConstant.getHostAC());
                UcUrls.getInstance().setHost(GlobalConstant.getHostUC());
                OtcUrls.getInstance().setHost(GlobalConstant.getHostOTC());
                AgentUrls.getInstance().setHost(GlobalConstant.getHostAgent());
            } else {
                String uc = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_WRITE_API_UC);
                String ac = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_WRITE_API_AC);
                String acp = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_WRITE_API_ACP);

                GlobalConstant.isTest = true;
                GuardService.isTest = true;
                WebSocketService.isTest = true;

                GlobalConstant.CUR_HOST_UC = uc;
                GlobalConstant.CUR_HOST_AC = ac;
                GlobalConstant.CUR_HOST_ACP = acp;
                WebSocketService.setHost(acp);
                GuardService.setHost(acp);

                LoginUrls.getInstance().setHost(GlobalConstant.getHost() + "/" + GlobalConstant.TYPE_UC);
                LoginUrls.getInstance().setHostBusiness(GlobalConstant.getHost());
                LoginUrls.getInstance().setHostLogin(GlobalConstant.getHostLogin());
                AcUrls.getInstance().setHost(GlobalConstant.getHostAC());
                UcUrls.getInstance().setHost(GlobalConstant.getHostUC());
                OtcUrls.getInstance().setHost(GlobalConstant.getHostOTC());
                AgentUrls.getInstance().setHost(GlobalConstant.getHostAgent());
            }
        }

        presenter = new LoginPresenterImpl(this);

        String username = StringUtils.getText(etUsername);
        String password = StringUtils.getText(etPassword);
        if (StringUtils.isEmpty(username)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_username));
        } else if (StringUtils.isEmpty(password)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_pwd));
        } else if (!StringFormatUtils.isMobile(username)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input_correct) + getString(R.string.str_phone_number));
        } else {
            KeyboardUtils.hideSoftInput(activity);
            presenter.casLogin(strAreaCode + username, password, "true");
        }
    }

    /**
     * 极验证captcha成功回调
     *
     * @param obj
     */
    @Override
    public void captchSuccess(JSONObject obj) {
        gt3GeetestUtils.gtSetApi1Json(obj);
        gt3GeetestUtils.getGeetest(activity, UcUrls.getInstance().getHost() + "/captcha/mm/gee", null, null, new GT3GeetestBindListener() {
            @Override
            public boolean gt3SetIsCustom() {
                return true;
            }

            @Override
            public void gt3GetDialogResult(boolean status, String result) {
                if (status) {
                    Captcha captcha = new Gson().fromJson(result, Captcha.class);
                    String checkData = "gee::" + captcha.getGeetest_challenge() + "$" + captcha.getGeetest_validate() + "$" + captcha.getGeetest_seccode();
                    presenter.checkCaptcha(checkData, cid);
                }
            }
        });
        gt3GeetestUtils.setDialogTouch(true);
    }

    @Override
    public void casLoginSuccess(Object o) {
        gtc = (String) o;
        presenter.doUcLogin(gtc, GlobalConstant.TYPE_UC);
        isCasLogin = true;
        //保存手机号码
        String username = StringUtils.getText(etUsername);
        SharedPreferencesUtil.getInstance(activity).setParam(activity, SharedPreferencesUtil.SP_KEY_LOGIN_ACCOUNT, username);

        SharedPreferencesUtil.getInstance(activity).setParam(activity, SP_KEY_clearCode310, "Code310");
    }

    /**
     * 极限验证验证成功回调
     *
     * @param o
     */
    @Override
    public void codeSuccess(Object o) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        if (mPhoneVertifyDialog != null && mPhoneVertifyDialog.isShowing()) {
            mPhoneVertifyDialog.setStart();
        }
    }

    @Override
    public void ucLoginSuccess(String response) {
        if (GlobalConstant.TYPE_UC.equals(response)) {
            loginStatus.setUcLogin(true);
            presenter.doUcLogin(gtc, GlobalConstant.TYPE_AC);
        } else if (GlobalConstant.TYPE_AC.equals(response)) {
            loginStatus.setAcLogin(true);
            presenter.doUcLogin(gtc, GlobalConstant.TYPE_OTC);
        } else if (GlobalConstant.TYPE_OTC.equals(response)) {
            loginStatus.setOtcLogin(true);
            isCasLogin = false;
            saveOtcSid();
            MyApplication.getMyApplication().setLoginStatus(loginStatus);
            //获取用户信息
            presenter.getUserInfo();
        }
    }

    private void saveOtcSid() {
        CookieManager cookieManager = MyApplication.getAppContext().getCookieManager();
        CookieStore cookieStore = cookieManager.getCookieStore();
        List<HttpCookie> cookies = cookieStore.getCookies();
        for (HttpCookie cookie : cookies) {
            if ("otcsid".equals(cookie.getName())) {
                SharedPreferencesUtil.getInstance(MyApplication.getAppContext()).setOtcSid(BaseApplication.getAppContext(), cookie.getValue());
                break;
            }
        }
    }

    /**
     * 获取用户信息成功
     *
     * @param user
     */
    @Override
    public void getUserInfoSuccess(User user) {
        if (user != null) {
            user.setLogin(true);
            user.setGtc(gtc);
            BaseApplication.getAppContext().setCurrentUser(user);
        }
        setResult(RESULT_OK);
        //ToastUtils.showToast(activity, getString(R.string.str_login_success));
        showActivity(MainActivity.class, null);
        finish();
    }

    @Override
    public void checkPhoneCodeSuccess(String response) {
        if (isCasLogin) {
            presenter.doUcLogin(gtc, GlobalConstant.TYPE_UC);
        } else {
            checkInput();
        }
    }

    @Override
    public void checkCaptchaSuccess(String response) {
        if (isCasLogin) {
            presenter.doUcLogin(gtc, GlobalConstant.TYPE_UC);
        } else {
            checkInput();
        }
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        if (httpErrorEntity != null) {
            int code = httpErrorEntity.getCode();
            String msg = httpErrorEntity.getMessage();
            if (code == CasConstant.LOGIN_ERROR && !isCasLogin) {
                ToastUtils.showToast(activity, getString(R.string.str_login_error));
            } else if (code == JSON_ERROR) {
                ToastUtils.showToast(activity, getString(R.string.str_json_error));
            } else if (code == CAPTCH) {
                vertify(httpErrorEntity);
            } else if (code == CAPTCH2 && StringUtils.isNotEmpty(msg) && msg.contains("Captcha")) {//解决验证码失效问题
                ToastUtils.showToast(getResources().getString(R.string.str_code_error));
                showPhoneVertifyDialog();
            } else if (code == ERROR_CODE) {
                String message = httpErrorEntity.getMessage();
                if (StringUtils.isNotEmpty(message)) {
                    ToastUtils.showToast(this, httpErrorEntity.getCode() + httpErrorEntity.getMessage());
                } else {
                    ToastUtils.showToast(this, httpErrorEntity.getCode() + "");
                }
            } else {
                String message = httpErrorEntity.getMessage();
                if (StringUtils.isNotEmpty(message)) {
                    ToastUtils.showToast(this, message);
                } else {
                    ToastUtils.showToast(this, R.string.request_error);
                }
            }
        }
    }

    /**
     * 进行验证（根据data字段来判断，data=="phone"为短信验证，data=="gee"为极验证）
     *
     * @param httpErrorEntity
     */
    private void vertify(HttpErrorEntity httpErrorEntity) {
        String data = httpErrorEntity.getData();
        cid = httpErrorEntity.getCid();
        if ("phone".equals(data)) {
            showPhoneVertifyDialog();
        } else if ("gee".equals(data)) {
            geeVertify(httpErrorEntity);
        } else {
            ToastUtils.showToast(activity, httpErrorEntity.getMessage());
        }
    }

    /**
     * 极验证
     *
     * @param httpErrorEntity
     */
    private void geeVertify(HttpErrorEntity httpErrorEntity) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestClose();
            gt3GeetestUtils = null;
        }
        cid = httpErrorEntity.getCid();
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        presenter.captch();
    }

    /**
     * 展示发送短信验证码弹框
     */
    private void showPhoneVertifyDialog() {
        final String username = StringUtils.getText(etUsername);
        mPhoneVertifyDialog = new PhoneVertifyDialog(this);
        mPhoneVertifyDialog.withWidthScale(0.8f);
        mPhoneVertifyDialog.setClickListener(new PhoneVertifyDialog.ClickLister() {
            @Override
            public void onCancel() {
                mPhoneVertifyDialog.dismiss();
            }

            @Override
            public void onSendVertifyCode() {
                presenter.getPhoneCode(strAreaCode + username);
            }

            @Override
            public void onConfirm() {
                presenter.checkPhoneCode(mPhoneVertifyDialog.getVertifyCode());
                mPhoneVertifyDialog.dismiss();
            }
        });
        mPhoneVertifyDialog.show();
    }

}
