package com.spark.coinpay.bind_phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulebase.widget.TimeCount;
import com.spark.moduleuc.entity.CountryEntity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定手机号和修改手机号界面
 */
public class BindPhoneActivity extends BaseActivity implements BindPhoneContract.BindPhoneView {
    @BindView(R.id.tvAreaCode)
    TextView tvAreaCode;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.tvBind)
    TextView tvBind;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.llChgPhone)
    LinearLayout llChgPhone;
    @BindView(R.id.tvChgAreaCode)
    TextView tvChgAreaCode;
    @BindView(R.id.etChgPhone)
    EditText etChgPhone;
    @BindView(R.id.llPhone)
    LinearLayout llPhone;
    @BindView(R.id.oldPhoneName)
    TextView oldPhone;
    @BindView(R.id.llChgCode)
    LinearLayout llChgCode;
    @BindView(R.id.etNewCode)
    EditText etNewCode;
    @BindView(R.id.tvNewGetCode)
    TextView tvNewGetCode;
    private CountryEntity country;
    private TimeCount timeCount;
    private TimeCount timeCountNew;
    private String strAreaCode = "86";
    private boolean isChg;
    private String phone;
    private BindPhonePresenterImpl presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected void initData() {
        super.initData();
        tvTitle.setText(getString(R.string.str_chg_phone));
        presenter = new BindPhonePresenterImpl(this);
        timeCount = new TimeCount(60000, 1000, tvGetCode);
        timeCountNew = new TimeCount(60000, 1000, tvNewGetCode);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");
            isChg = bundle.getBoolean("isChg");
            if (isChg) {
                tvPhone.setText(getString(R.string.str_handset_tail_number) + phone.substring(7, 11) + getString(R.string.str_receive_message_code));
                tvPhone.setVisibility(View.GONE);
                llChgPhone.setVisibility(View.VISIBLE);
                llChgCode.setVisibility(View.VISIBLE);
                llPhone.setVisibility(View.VISIBLE);
                tvBind.setText(getString(R.string.str_change_bind_phone_num));
                tvTitle.setText(getString(R.string.str_chg_phone));
                etPhone.setText(phone);
                etPhone.setEnabled(false);
            }
        }
    }

    @OnClick({R.id.tvGetCode, R.id.tvBind, R.id.tvNewGetCode})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvGetCode: // 发送验证码
                getCode();
                break;
            case R.id.tvNewGetCode: // 发送新手机验证码
                getCodeNew();
                break;
            case R.id.tvBind:
                bindOrChgPhone();
                break;
        }
    }

    /**
     * 获取旧手机验证码
     */
    private void getCode() {
        if (isChg) {
            presenter.sendChangePhoneCode("4");
        } else {
            String phone = StringUtils.getText(etPhone);
            if (StringUtils.isEmpty(phone)) {
                ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_phone_number));
            } else {
                presenter.sendCode(phone, strAreaCode, "3");
            }
        }
    }

    /**
     * 获取新手机验证码
     */
    private void getCodeNew() {
        String phone = StringUtils.getText(etChgPhone);
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_new_phone_number));
        } else {
            presenter.sendCode(phone, strAreaCode, "4");
        }
    }

    /**
     * 绑定手机号/修改手机号
     */
    private void bindOrChgPhone() {
        String phone = "";
        if (isChg)
            phone = StringUtils.getText(etChgPhone);
        else
            phone = StringUtils.getText(etPhone);
        String password = StringUtils.getText(etPwd);
        String code = StringUtils.getText(etCode);
        String newCode = StringUtils.getText(etNewCode);
        if (StringUtils.isEmpty(password, phone, code)) {
            ToastUtils.showToast(activity, getString(R.string.str_incomplete_information));
        } else {
            if (isChg) {
                presenter.changePhone(phone, password, code, newCode);
            } else {
                presenter.bindPhone(phone, password, code);
            }
        }
    }


    @Override
    public void bindPhoneSuccess(String obj) {
        ToastUtils.showToast(activity, obj);
        Intent intent = new Intent();
        intent.putExtra("newPhone", etChgPhone.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void sendChangePhoneCodeSuccess(String obj) {
        ToastUtils.showToast(activity, obj);
        timeCount.start();
        tvGetCode.setEnabled(false);
    }

    @Override
    public void changePhoneSuccess(String obj) {
        ToastUtils.showToast(activity, obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void sendCodeSuccess(String obj) {
//        if (type == 2) { // 这个是发送新验证码的返回
//            timeCountNew.start();
//            tvNewGetCode.setEnabled(false);
//        } else {
//            timeCount.start();
//            tvGetCode.setEnabled(false);
//        }
    }
}
