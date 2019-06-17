package com.spark.coinpay.my.safe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.my.account_pwd.AccountPwdActivity;
import com.spark.coinpay.my.forgot_pwd.ForgotPwdActivity;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 安全中心
 */
public class SafeActivity extends BaseActivity {

    @BindView(R.id.tvLoginPwd)
    TextView tvLoginPwd;
    @BindView(R.id.llLoginPwd)
    LinearLayout llLoginPwd;
    @BindView(R.id.tvAcountPwd)
    TextView tvAcountPwd;
    @BindView(R.id.llAccountPwd)
    LinearLayout llAccountPwd;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    break;
            }
        }
    }


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_safe;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_my_safe));
    }

    @Override
    protected void initData() {
        super.initData();
        fillViews();
    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.llLoginPwd, R.id.llAccountPwd})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.llLoginPwd://登录密码
                String phone = MyApplication.getAppContext().getCurrentUser().getMobilePhone();
                if (StringUtils.isEmpty(phone)) {
                    ToastUtils.showToast(activity, getString(R.string.binding_phone_first));
                } else {
                    if (phone.startsWith("86")) {
                        phone = phone.substring(2, phone.length());
                    }
                    bundle.putString("phone", phone);
                    bundle.putBoolean("isFromSafe",true);
                    showActivity(ForgotPwdActivity.class, bundle, 1);
                }
                break;
            case R.id.llAccountPwd://交易密码
                int fundsVerified = MyApplication.getAppContext().getCurrentUser().getFundsVerified();
                bundle.putBoolean("isSet", fundsVerified == 1 ? true : false);
                showActivity(AccountPwdActivity.class, bundle, 1);
                break;
        }
    }



    /**
     * 根据安全参数显示
     */
    private void fillViews() {
        int fundsVerified = MyApplication.getAppContext().getCurrentUser().getFundsVerified();
        boolean verified = fundsVerified == 1;
        tvAcountPwd.setText(verified ? R.string.str_had_set : R.string.str_not_set);
        tvLoginPwd.setText(R.string.str_to_edit);
    }


}
