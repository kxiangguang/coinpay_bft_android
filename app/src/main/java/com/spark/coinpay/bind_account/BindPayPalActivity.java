package com.spark.coinpay.bind_account;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.entity.PayWaySetting;
import com.spark.library.acp.model.MessageResult;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定PayPal
 */
public class BindPayPalActivity extends BaseActivity implements BindAccountContract.PaypalView {
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.etWarn)
    EditText etWarn;
    @BindView(R.id.etDay)
    EditText etDay;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    private BindPaypalPresenterImpl presenter;
    private PayWaySetting payWaySetting;
    private boolean isUpdate = false;//添加或者更新

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_paypal;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        User user = MyApplication.getAppContext().getCurrentUser();
        if (user != null && StringUtils.isNotEmpty(user.getRealName())) {
            etName.setText(user.getRealName());
        }
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new BindPaypalPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payWaySetting = (PayWaySetting) bundle.getSerializable("data");
            if (payWaySetting != null) {
                isUpdate = true;
                tvConfirm.setText(getResources().getString(R.string.str_text_change));
                etAccount.setText(payWaySetting.getPayAddress());
                etWarn.setText(payWaySetting.getPayNotes());
                etDay.setText(MathUtils.subZeroAndDot(payWaySetting.getDayLimit() + ""));
            }
        }
        if (isUpdate) {
            tvTitle.setText(getString(R.string.str_text_change) + getString(R.string.str_paypal));
        } else {
            tvTitle.setText(getString(R.string.str_account_seting) + getString(R.string.str_paypal));
        }
    }

    @OnClick({R.id.tvConfirm})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvConfirm:
                confirm();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    private void confirm() {
        String account = etAccount.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        String warn = etWarn.getText().toString();
        String dayLimit = etDay.getText().toString();
        if (StringUtils.isEmpty(account, pwd, dayLimit)) {
            ToastUtils.showToast(this, R.string.incomplete_information);
        } else {
            if (isUpdate) {
                presenter.doUpdatePaypal(payWaySetting.getId(), GlobalConstant.PAYPAL, account, getString(R.string.str_paypal), "", pwd, warn, dayLimit);
            } else {
                presenter.doBindPaypal(GlobalConstant.PAYPAL, account, getString(R.string.str_paypal), "", pwd, warn, dayLimit);
            }

        }
    }


    @Override
    public void doBindPaypalSuccess(MessageResult response) {
        ToastUtils.showToast(this, response.getMessage());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doUpdatePaypalSuccess(MessageResult response) {
        ToastUtils.showToast(this, response.getMessage());
        setResult(RESULT_OK);
        finish();
    }

}
