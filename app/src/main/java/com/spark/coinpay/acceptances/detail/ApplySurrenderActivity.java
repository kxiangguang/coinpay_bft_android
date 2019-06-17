package com.spark.coinpay.acceptances.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 申请退保
 */
public class ApplySurrenderActivity extends BaseActivity implements LevelDetailContract.ApplySurrenderView {
    @BindView(R.id.etReason)
    EditText etReason;
    @BindView(R.id.ivNoticeIcon)
    ImageView ivNoticeIcon;
    @BindView(R.id.tvNotice)
    TextView tvNotice;
    @BindView(R.id.llNotice)
    LinearLayout llNotice;
    @BindView(R.id.tvReason)
    TextView tvReason;
    @BindView(R.id.tvFailReason)
    TextView tvFailReason;
    @BindView(R.id.llBackReason)
    LinearLayout llBackReason;
    @BindView(R.id.llUpdate)
    LinearLayout llUpdate;
    @BindView(R.id.llAdd)
    LinearLayout llAdd;
    @BindView(R.id.tvApply)
    TextView tvApply;

    private int status;//承兑商认证状态 0：未认证 1：认证-待审核 2：认证-审核成功 3：认证-审核失败 5：退保-待审核 6：退保-审核失败 7:退保-审核成功 8:退保-已退还保证金
    private String reason;//申请退保原因

    private ApplySurrenderPresenterImpl presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_apply_surrender;
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
        tvTitle.setText(getString(R.string.str_apply));
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new ApplySurrenderPresenterImpl(this);
    }

    @Override
    protected void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            status = bundle.getInt("status");
            reason = bundle.getString("reason");
            if (status == 6) {//6：退保-审核失败
                llAdd.setVisibility(View.GONE);
                llUpdate.setVisibility(View.VISIBLE);
                llNotice.setVisibility(View.VISIBLE);
                ivNoticeIcon.setImageResource(R.mipmap.icon_check_failed);
                tvNotice.setText("退保-审核失败");
                tvApply.setVisibility(View.VISIBLE);
                tvApply.setText(getString(R.string.str_apply_again));
                if (reason == null) {
                    reason = "";
                }
                tvReason.setText("");
                tvFailReason.setText(reason);
            } else {
                llAdd.setVisibility(View.VISIBLE);
                llUpdate.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.tvApply})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvApply:
                if (status == 6) {
                    llAdd.setVisibility(View.VISIBLE);
                    llUpdate.setVisibility(View.GONE);
                    status = 0;
                } else {
                    String reason = etReason.getText().toString();
                    if (StringUtils.isNotEmpty(reason)) {
                        presenter.acceptMerchantCanel(reason);
                    } else {
                        ToastUtils.showToast(activity, getString(R.string.hint_appeal_reason));
                    }
                }
                return;
        }
    }

    @Override
    public void acceptMerchantCanelSuccess(String obj) {
        ToastUtils.showToast(ApplySurrenderActivity.this, obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void acceptMerchantCanelError(HttpErrorEntity httpErrorEntity) {
        dealError(httpErrorEntity);
        if (httpErrorEntity != null) {
            ToastUtils.showToast(this, httpErrorEntity.getMessage());
        }
    }

}
