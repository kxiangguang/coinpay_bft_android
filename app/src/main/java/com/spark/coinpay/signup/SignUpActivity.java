package com.spark.coinpay.signup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;


import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.coinpay.R;
import com.spark.coinpay.acceptances.process.AcceptancesProcessTextActivity;
import com.spark.coinpay.entity.Captcha;
import com.spark.coinpay.utils.FormatDataUtils;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.view.GetImgDialog;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.StringFormatUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulebase.widget.TimeCount;
import com.spark.moduleuc.UcUrls;
import com.spark.moduleuc.entity.CountryEntity;
import com.spark.coinpay.country.CountryActivity;

import org.json.JSONObject;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.modulebase.base.BaseConstant.CAPTCH;
import static com.spark.modulebase.base.BaseConstant.CAPTCH2;

/**
 * 注册
 */
public class SignUpActivity extends BaseActivity implements SignUpContract.SignView {
    public static final String TAG = SignUpActivity.class.getSimpleName();
    public static String token = "";
    @BindView(R.id.tvArea)
    TextView tvArea;
    private TextView tvTag;
    private EditText etPhone;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etCode;
    private EditText etPromoCode;
    private EditText etPassword;
    private TextView tvCountry;
    private TextView tvGetCode;
    private TextView tvSignUp;
    private EditText etComfirmPassword;
    private TextView tvLoginTag;
    private CheckBox checkbox;
    private CountryEntity country;
    private TimeCount timeCount;
    private SignUpPresenterImpl presenter;
    boolean isEmail = false;
    private String strCountry = "China";
    private String strAreaCode = "86";
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String cid;

    String phone;
    String username;
    String code;
    String password;
    String confirmPassword;
    String email;
    String inviteCode;
    private GetImgDialog mPhoneVertifyDialog;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                country = (CountryEntity) data.getSerializableExtra("getCountry");
                if (country != null) {
                    tvCountry.setText(FormatDataUtils.getViewNameByCode(country, activity));
                    strCountry = country.getEnName();
                    strAreaCode = country.getAreaCode();
                    tvArea.setText("+" + country.getAreaCode() + ">");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        findView();
        ivBack.setVisibility(View.VISIBLE);
        tvGoto.setVisibility(View.GONE);
        tvGoto.setText(getString(R.string.str_email_sign_up));
    }

    /**
     * findview
     */
    private void findView() {
        tvTag = findViewById(R.id.tvTag);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etCode = findViewById(R.id.etCode);
        etPromoCode = findViewById(R.id.etPromoCode);
        etPassword = findViewById(R.id.etPassword);
        tvCountry = findViewById(R.id.tvCountry);
        tvGetCode = findViewById(R.id.tvGetCode);
        tvSignUp = findViewById(R.id.tvSignUp);
        etComfirmPassword = findViewById(R.id.etComfirmPassword);
        tvLoginTag = findViewById(R.id.tvLoginTag);
        String htm = "<font color=#6C6E8A>" + getString(R.string.str_please_login_have_account) + "</font>" + getString(R.string.str_please_login) + ">>";
        tvLoginTag.setText(Html.fromHtml(htm));
        checkbox = findViewById(R.id.checkbox);
    }

    @Override
    protected void initData() {
        super.initData();
        timeCount = new TimeCount(60000, 1000, tvGetCode);
        presenter = new SignUpPresenterImpl(this);
    }

    /**
     * 切换注册方式
     */
    private void doSwitchWay() {
        if (!isEmail) { // 当前为手机注册，切换为邮箱注册
            isEmail = true;
            tvTitle.setText(getString(R.string.str_email_sign_up));
            tvGoto.setText(getString(R.string.str_phone_sign_up));
            tvTag.setText(getString(R.string.str_email_sign_up));
            etEmail.setVisibility(View.VISIBLE);
            etPhone.setVisibility(View.GONE);
        } else {
            isEmail = false;
            tvTitle.setText(getString(R.string.str_phone_sign_up));
            tvGoto.setText(getString(R.string.str_email_sign_up));
            tvTag.setText(getString(R.string.str_phone_sign_up));
            etEmail.setVisibility(View.GONE);
            etPhone.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.tvGetCode, R.id.tvSignUp, R.id.tvLoginTag, R.id.tvCountry, R.id.tvAgreement})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvGetCode:
                getCode();
                break;
            case R.id.tvSignUp:
                checkInput();
                break;
            case R.id.tvLoginTag:
                finish();
                break;
            case R.id.tvCountry:
                showActivity(CountryActivity.class, null, 1);
                break;
            case R.id.tvAgreement:
                showActivity(AcceptancesProcessTextActivity.class, null);
                break;

        }

    }

    @Override
    protected void checkInput() {
        super.checkInput();
        if (!checkbox.isChecked()) {
            ToastUtils.showToast(activity, getString(R.string.str_agree_tag));
            return;
        }
        phone = StringUtils.getText(etPhone);
        username = StringUtils.getText(etUsername);
        code = StringUtils.getText(etCode);
        password = StringUtils.getText(etPassword);
        confirmPassword = StringUtils.getText(etComfirmPassword);
        email = StringUtils.getText(etEmail);
        inviteCode = StringUtils.getText(etPromoCode).replaceAll(" ", "");

        if (StringUtils.isNotEmpty(inviteCode)) {
            if (!inviteCode.startsWith("A")) {
                ToastUtils.showToast("邀请码不正确");
                return;
            }
        }

        if (StringUtils.isEmpty(phone) && !isEmail) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_phone_number));
        } else if (isEmail && StringUtils.isEmpty(email)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_email));
        } else if (isEmail && !StringFormatUtils.isEmail(email)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input_correct) + getString(R.string.str_email));
        } else if (StringUtils.isEmpty(code)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_code));
        } else if (StringUtils.isEmpty(password)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_pwd_login));
        } else if (StringUtils.isEmpty(confirmPassword)) {
            ToastUtils.showToast(activity, getString(R.string.str_please) + getString(R.string.str_confirm_pwd));
        } else if (!password.equals(confirmPassword)) {
            ToastUtils.showToast(activity, getString(R.string.str_pwd_diff));
        } else {
            if (StringUtils.isNotEmpty(inviteCode)) {
                presenter.checkInviteCode(inviteCode);
            } else {
                doSignUp(phone, username, code, password, email, inviteCode);
            }
        }
    }

    /**
     * 注册
     *
     * @param phone
     * @param username
     * @param code
     * @param password
     * @param email
     */
    private void doSignUp(String phone, String username, String code, String password, String email, String inviteCode) {
        if (!isEmail) {
            presenter.sighUpByPhone(username, password, strCountry, inviteCode, strAreaCode + phone, code);
        } else {
            presenter.sighUpByEmail(username, password, strCountry, inviteCode, email, code);
        }
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
                presenter.getEmailCode(email);
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
            timeCount.onFinish();
            timeCount.start();
            tvGetCode.setEnabled(false);
        }
    }

    @Override
    public void checkInviteCodeSuccess(String obj) {
        presenter.sighUpByPhone(username, password, strCountry, inviteCode, strAreaCode + phone, code);
    }

    @Override
    public void checkInviteCodeFail(HttpErrorEntity httpErrorEntity) {
        if (httpErrorEntity != null) {
            ToastUtils.showToast("邀请码不存在");
        }
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestClose();
            gt3GeetestUtils = null;
        }
        int code = httpErrorEntity.getCode();
        String msg = httpErrorEntity.getMessage();
        if (code == CAPTCH && StringUtils.isNotEmpty(httpErrorEntity.getData()) && httpErrorEntity.getData().contains("img")) {
            cid = httpErrorEntity.getCid();
            handler.sendEmptyMessage(1);
        } else if (code == CAPTCH && StringUtils.isNotEmpty(msg) && msg.contains("captcha")) {
            cid = httpErrorEntity.getCid();
            gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
            presenter.captch();
        } else if (code == CAPTCH2 && StringUtils.isNotEmpty(httpErrorEntity.getData()) && httpErrorEntity.getData().contains("img")) {
            ToastUtils.showToast("图片验证码不正确或已失效");
            cid = httpErrorEntity.getCid();
            handler.sendEmptyMessage(1);
        } else if (code == CAPTCH2 && StringUtils.isNotEmpty(msg) && msg.contains("Captcha")) {//解决验证码失效问题
            ToastUtils.showToast(getResources().getString(R.string.str_code_error));
            cid = httpErrorEntity.getCid();
            gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
            //presenter.captch();
        } else {
            ToastUtils.showToast(activity, httpErrorEntity.getMessage());
        }
    }

    @Override
    public void sighUpSuccess(String obj) {
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
    protected void setListener() {
        super.setListener();
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvSignUp.setEnabled(true);
                    tvSignUp.setBackgroundResource(R.drawable.ripple_btn_global_option_corner_selector);
                    tvSignUp.setTextColor(getResources().getColor(R.color.white));
                } else {
                    tvSignUp.setEnabled(false);
                    tvSignUp.setBackgroundResource(R.drawable.shape_bg_normal_corner_grey_enabled);
                    tvSignUp.setTextColor(getResources().getColor(R.color.font_main_content));
                }
            }
        });
    }

    /**
     * 展示发送短信验证码弹框
     */
    private void showPhoneVertifyDialog(Bitmap bitmap) {
        if (mPhoneVertifyDialog == null) {
            mPhoneVertifyDialog = new GetImgDialog(this);
            mPhoneVertifyDialog.setClickListener(new GetImgDialog.ClickLister() {

                @Override
                public void onConfirm(String imgCode) {
                    if (StringUtils.isEmpty(imgCode)) {
                        ToastUtils.showToast("图片验证码不能为空");
                    } else {
                        String checkData = "img::" + imgCode;
                        presenter.getPhoneCode(strAreaCode + StringUtils.getText(etPhone), checkData, cid);
                    }
                    mPhoneVertifyDialog.dismiss();
                }

                @Override
                public void reGetImg() {
                    handler.sendEmptyMessage(1);
                }
            });
        }
        mPhoneVertifyDialog.setBitmap(bitmap);
        mPhoneVertifyDialog.show();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    presenter.getImg();
                    break;
                case 2:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    showPhoneVertifyDialog(bitmap);
                    break;
            }
        }
    };

    @Override
    public void getImgSuccess(final InputStream inputStream) {
        if (inputStream != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                }
            }).start();

        }
    }
}
