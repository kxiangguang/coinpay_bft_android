package com.spark.coinpay.bind_phone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 显示手机号码
 */
public class PhoneViewActivity extends BaseActivity {
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvEdit)
    TextView tvEdit;
    private String phone;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                String phone = data.getStringExtra("newPhone");
                if (!TextUtils.isEmpty(phone)) tvPhone.setText(phone);
            }
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_phone_view;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tvEdit)
    void edit() {
        showActivity(ChangeLeadActivity.class, getIntent().getExtras(), 1);
    }

    @Override
    protected void initData() {
        super.initData();
        tvTitle.setText(getString(R.string.str_phone_validate));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");
            tvPhone.setText(phone);
        }
    }
}
