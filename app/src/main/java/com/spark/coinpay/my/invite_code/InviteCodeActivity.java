package com.spark.coinpay.my.invite_code;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 填写邀请码
 */
public class InviteCodeActivity extends BaseActivity implements InviteCodeContract.View {

    @BindView(R.id.etAccountPwd)
    EditText etAccountPwd;
    @BindView(R.id.tvSet)
    TextView tvSet;

    private InviteCodePresenterImpl presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_invite_code;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.str_my_set_invite_code_bind);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new InviteCodePresenterImpl(this);
    }

    @OnClick({R.id.tvSet})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvSet:
                checkInput();
                break;
        }
    }

    /**
     * 检查
     */
    @Override
    protected void checkInput() {
        String jyPassword = etAccountPwd.getText().toString().trim();
        if (StringUtils.isEmpty(jyPassword) ){
            ToastUtils.showToast(activity, R.string.incomplete_information);
        } else {
            presenter.accountPwd(jyPassword);
        }
    }

    @Override
    public void accountPwdSuccess(String obj) {
        ToastUtils.showToast(this, obj);
        setResult(RESULT_OK);
        finish();
    }

}
