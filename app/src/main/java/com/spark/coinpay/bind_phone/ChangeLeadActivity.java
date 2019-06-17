package com.spark.coinpay.bind_phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改手机号主界面
 */
public class ChangeLeadActivity extends BaseActivity {
    @BindView(R.id.llCanReceive)
    LinearLayout llCanReceive;
    @BindView(R.id.llCannotReceive)
    LinearLayout llCannotReceive;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_change_lead;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        tvTitle.setText(getString(R.string.str_chg_phone));
    }

    @OnClick({R.id.llCanReceive, R.id.llCannotReceive})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llCannotReceive:
                Toast.makeText(this, getString(R.string.str_unreceive_phone_code), Toast.LENGTH_LONG).show();
                break;
            case R.id.llCanReceive:
                Bundle bundle = getIntent().getExtras();
                bundle.putBoolean("isChg", true);
                showActivity(BindPhoneActivity.class, bundle, 1);
                break;
        }

    }
}
