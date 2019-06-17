package com.spark.coinpay.my.forgot_pwd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.entity.Captcha;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.StringFormatUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulebase.widget.TimeCount;
import com.spark.moduleuc.UcUrls;
import com.spark.moduleuc.entity.CountryEntity;

import org.json.JSONObject;

import butterknife.OnClick;

import static com.spark.modulebase.base.BaseConstant.CAPTCH;
import static com.spark.modulebase.base.BaseConstant.CAPTCH2;

/**
 * 忘记密码/重置登录密码
 */
public class ForgotPwdActivity extends BaseActivity implements ForgotPwdContract.ForgotPwdView {
    private TextView tvTag;
    private EditText etPhone;
    private TextView tvLoginTag;
    private EditText etEmail;
    private EditText etCode;
    private TextView tvGetCode;
    private EditText etPassword;
    private EditText etRenewPassword;
    private TextView tvConfirm;
    private TimeCount timeCount;
    private ForgotPwdPresenterImpl presenter;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    boolean isEmail = false;
    private String strAreaCode = "86";
    private boolean isCaptch;
    private String cid;
    private boolean isFromSafe = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                CountryEntity country = (CountryEntity) data.getSerializableExtra("getCountry");
                if (country != null) {
                    strAreaCode = country.getAreaCode();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.cancelUtils();
            gt3GeetestUtils = null;
        }
        presenter.destory();
    }


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_forgot_pwd;
    }


    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        findView();
        ivBack.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String phone = bundle.getString("phone");
            if (StringUtils.isNotEmpty(phone)) {
                etPhone.setText(phone);
            }
            isFromSafe = bundle.getBoolean("isFromSafe", false);
            if (isFromSafe) {
                tvLoginTag.setVisibility(View.INVISIBLE);
            } else {
                tvLoginTag.setVisibility(View.VISIBLE);
            }
        }
    }

    private void findView() {
        tvTag = findViewById(R.id.tvTag);
        etPhone = findViewById(R.id.etPhone);
        tvLoginTag = findViewById(R.id.tvLoginTag);
        etEmail = findViewById(R.id.etEmail);
        etCode = findViewById(R.id.etCode);
        tvGetCode = findViewById(R.id.tvGetCode);
        etPassword = findViewById(R.id.etPassword);
        etRenewPassword = findViewById(R.id.etRenewPassword);
        tvConfirm = findViewById(R.id.tvConfirm);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new ForgotPwdPresenterImpl(this);
        timeCount = new TimeCount(60000, 1000, tvGetCode);
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        String htm = "<font color=#6C6E8A>" + getString(R.string.str_please_login_have_account) + "</font>" + getString(R.string.str_please_login) + ">>";
        tvLoginTag.setText(Html.fromHtml(htm));
    }


    @Override
    protected void setListener() {
        super.setListener();
        etEmail.addTextChangedListener(new MyTextWatcher());
        etPhone.addTextChangedListener(new MyTextWatcher());
        etCode.addTextChangedListener(new MyTextWatcher());
        etPassword.addTextChangedListener(new MyTextWatcher());
        etRenewPassword.addTextChangedListener(new MyTextWatcher());
    }

    /**
     * 切换验证方式
     */
    private void doSwitchWay() {
        if (!isEmail) {
            isEmail = true;
            tvGoto.setText(getString(R.string.str_reset_phone_pwd));
            tvTag.setText(getString(R.string.str_reset_email_pwd));
            etEmail.setVisibility(View.VISIBLE);
            etPhone.setVisibility(View.GONE);
        } else {
            isEmail = false;
            tvGoto.setText(getString(R.string.str_reset_email_pwd));
            tvTag.setText(getString(R.string.str_reset_phone_pwd));
            etEmail.setVisibility(View.GONE);
            etPhone.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.tvGetCode, R.id.tvConfirm, R.id.tvLoginTag})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvGetCode:
                getCode();
                break;
            case R.id.tvConfirm:
                checkInput();
                break;
            case R.id.tvLoginTag:
                finish();
                break;
        }
    }

    @Override
    protected void checkInput() {
        super.checkInput();
        String phone = StringUtils.getText(etPhone);
        String email = StringUtils.getText(etEmail);
        String code = StringUtils.getText(etCode);
        String password = StringUtils.getText(etPassword);
        String passwordRe = StringUtils.getText(etRenewPassword);
        if (StringUtils.isEmpty(phone) && !isEmail) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_phone_number));
        } else if (isEmail && StringUtils.isEmpty(email)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_email));
        } else if (isEmail && !StringFormatUtils.isEmail(email)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input_correct) + getString(R.string.str_email));
        } else if (StringUtils.isEmpty(code)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_code));
        } else if (StringUtils.isEmpty(password)) {
            ToastUtils.showToast(activity, getString(R.string.str_login_password));
        } else if (StringUtils.isEmpty(passwordRe)) {
            ToastUtils.showToast(activity, getString(R.string.str_please) + getString(R.string.str_confirm_pwd));
        } else if (!password.equals(passwordRe)) {
            ToastUtils.showToast(activity, getString(R.string.str_pwd_diff));
        } else {
            //第一步 校验短信验证码
            presenter.checkPhoneCode(code);
        }
    }

    @Override
    public void checkPhoneCodeSuccess(String response) {
        //第二步 修改登录密码
        String phone = StringUtils.getText(etPhone);
        String password = StringUtils.getText(etPassword);
        presenter.updateForget(strAreaCode + phone, password);
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        if (!isEmail) {
            String phone = StringUtils.getText(etPhone);
            if (StringUtils.isEmpty(phone)) {
                ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_phone_number));
            } else if (!StringFormatUtils.isMobile(phone)) {
                ToastUtils.showToast(activity, getString(R.string.str_please_input_correct) + getString(R.string.str_phone_number));
            } else {
                presenter.getPhoneCode(strAreaCode + phone);
            }
        } else {
            String email = StringUtils.getText(etEmail);
            if (StringUtils.isEmpty(email)) {
                ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_email));
            } else if (!StringFormatUtils.isEmail(email)) {
                ToastUtils.showToast(activity, getString(R.string.str_please_input_correct) + getString(R.string.str_email));
            } else {
                isCaptch = true;
                presenter.captch();
            }
        }

    }

    @Override
    public void getPhoneCodeSuccess(String obj) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        ToastUtils.showToast(activity, obj);
        if (!isEmail) {
            timeCount.start();
            tvGetCode.setEnabled(false);
        }
    }

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
                    presenter.getPhoneCode(strAreaCode + StringUtils.getText(etPhone), checkData, cid);
                }
            }
        });
        gt3GeetestUtils.setDialogTouch(true);
    }

    @Override
    public void updateForgetSuccess(String obj) {
        ToastUtils.showToast(activity, obj);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (isEmail) {
            bundle.putString("account", StringUtils.getText(etEmail));
        } else {
            bundle.putString("account", StringUtils.getText(etPhone));
        }
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestClose();
            gt3GeetestUtils = null;
        }
        int code = httpErrorEntity.getCode();
        String msg = httpErrorEntity.getMessage();
        if (code == CAPTCH && StringUtils.isNotEmpty(msg) && msg.contains("captcha")) {
            cid = httpErrorEntity.getCid();
            gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
            presenter.captch();
        } else if (code == CAPTCH2 && StringUtils.isNotEmpty(msg) && msg.contains("Captcha")) {//解决验证码失效问题
            ToastUtils.showToast(getResources().getString(R.string.str_code_error));
        } else {
            ToastUtils.showToast(activity, httpErrorEntity.getMessage());
        }
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String password = etPassword.getText().toString().trim();
            String rePwd = etRenewPassword.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String code = etCode.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            if ((StringUtils.isEmpty(password, rePwd, phone, code) && !isEmail) || (StringUtils.isEmpty(password, rePwd, email, code) && isEmail)) {
                tvConfirm.setBackgroundResource(R.drawable.shape_bg_normal_corner_grey_enabled);
                tvConfirm.setEnabled(false);
            } else {
                tvConfirm.setBackgroundResource(R.drawable.ripple_btn_global_option_corner_selector);
                tvConfirm.setEnabled(true);
            }
        }
    }

    @Override
    public void codeSuccess(String obj) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        ToastUtils.showToast(activity, obj);
        if (!isEmail) {
            timeCount.start();
            tvGetCode.setEnabled(false);
        }
    }
}
