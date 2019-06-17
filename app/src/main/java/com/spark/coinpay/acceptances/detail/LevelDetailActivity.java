package com.spark.coinpay.acceptances.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spark.coinpay.R;
import com.spark.coinpay.acceptances.authentication.AuthenticationActivity;
import com.spark.coinpay.base.BaseActivity;
import com.spark.library.acp.model.AcceptMerchantApplyMarginType;
import com.spark.library.acp.model.AcceptMerchantFrontVo;
import com.spark.modulebase.entity.HttpErrorEntity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 承兑商等级详情
 */
public class LevelDetailActivity extends BaseActivity implements LevelDetailContract.LevelDetailView {
    @BindView(R.id.ivSelf)
    ImageView ivSelf;
    @BindView(R.id.ivAssets)
    ImageView ivAssets;
    @BindView(R.id.llAssets)
    LinearLayout llAssets;
    @BindView(R.id.tvNotice)
    TextView tvNotice;
    @BindView(R.id.tvApply)
    TextView tvApply;
    @BindView(R.id.ivNoticeIcon)
    ImageView ivNoticeIcon;
    @BindView(R.id.llNotice)
    LinearLayout llNotice;

    private LevelDetailPresenterImpl presenter;
    private int status;//承兑商认证状态 0：未认证 1：认证-待审核 2：认证-审核成功 3：认证-审核失败 5：退保-待审核 6：退保-审核失败 7:退保-审核成功 8:退保-已退还保证金
    private int merchantType;//认证类型
    private Long id;//重新认证id
    private String reason;//申请退保原因

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_level_detail;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    //presenter.getSelfLevelInfo();
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
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
        tvTitle.setText(getString(R.string.str_detify));
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new LevelDetailPresenterImpl(this);
    }

    @Override
    protected void loadData() {
        presenter.getSelfLevelInfo();
    }

    @OnClick({R.id.tvApply})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            //承兑商认证状态 0：未认证 1：认证-待审核 2：认证-审核成功 3：认证-审核失败 5：退保-待审核 6：退保-审核失败 7:退保-审核成功 8:退保-已退还保证金
            case R.id.tvApply:
                if (status == 2) {//2：认证-审核成功
                    Bundle bundle = new Bundle();
                    bundle.putString("reason", "");
                    bundle.putInt("status", status);
                    showActivity(ApplySurrenderActivity.class, null, 1);
                } else if (status == 3) {//3：认证-审核失败
                    if (id != 0) {//重新认证
                        Bundle bundle = new Bundle();
                        bundle.putLong("id", id);
                        showActivity(AuthenticationActivity.class, bundle, 1);
                    }
                } else if (status == 6) {//6：退保-审核失败
                    Bundle bundle = new Bundle();
                    bundle.putString("reason", reason);
                    bundle.putInt("status", status);
                    showActivity(ApplySurrenderActivity.class, bundle, 1);
                }
                return;
        }
    }

    @Override
    public void getSelfLevelInfoSuccess(AcceptMerchantFrontVo acceptMerchantFrontVo) {
        Glide.with(this).load(acceptMerchantFrontVo.getAssetImg()).into(ivSelf);
        Glide.with(this).load(acceptMerchantFrontVo.getTradeDataImg()).into(ivAssets);
        llAssets.setEnabled(false);
        status = acceptMerchantFrontVo.getCertifiedBusinessStatus();
        //承兑商认证状态 0：未认证 1：认证-待审核 2：认证-审核成功 3：认证-审核失败 5：退保-待审核 6：退保-审核失败 7:退保-审核成功 8:退保-已退还保证金
        if (status == 2) {
            llNotice.setVisibility(View.VISIBLE);
            ivNoticeIcon.setImageResource(R.mipmap.icon_check_success);
            tvNotice.setText("审核认证已通过!");
            tvApply.setVisibility(View.GONE);
            tvApply.setText(getString(R.string.str_apply));
        } else if (status == 0) {
            llNotice.setVisibility(View.GONE);
            tvNotice.setText("未认证");
            tvApply.setVisibility(View.GONE);
        } else if (status == 1) {
            llNotice.setVisibility(View.VISIBLE);
            ivNoticeIcon.setImageResource(R.mipmap.icon_check);
            tvNotice.setText("认证审核中，请耐心等待...");
            tvApply.setVisibility(View.GONE);
        } else if (status == 3) {
            llNotice.setVisibility(View.VISIBLE);
            ivNoticeIcon.setImageResource(R.mipmap.icon_check_failed);
            tvNotice.setText("认证不通过");
            tvApply.setVisibility(View.VISIBLE);
            tvApply.setText(getString(R.string.str_detify_again));
        } else if (status == 5) {
            llNotice.setVisibility(View.VISIBLE);
            ivNoticeIcon.setImageResource(R.mipmap.icon_check);
            tvNotice.setText("退保审核中，请耐心等待...");
            tvApply.setVisibility(View.GONE);
        } else if (status == 6) {
            llNotice.setVisibility(View.VISIBLE);
            ivNoticeIcon.setImageResource(R.mipmap.icon_check_failed);
            tvNotice.setText("退保-审核不通过");
            tvApply.setVisibility(View.VISIBLE);
            tvApply.setText(getString(R.string.str_apply_again));
            reason = "";
        } else if (status == 7) {
            llNotice.setVisibility(View.VISIBLE);
            ivNoticeIcon.setImageResource(R.mipmap.icon_check_success);
            tvNotice.setText("退保-审核通过");
            tvApply.setVisibility(View.GONE);
        } else if (status == 8) {
            llNotice.setVisibility(View.VISIBLE);
            ivNoticeIcon.setImageResource(R.mipmap.icon_check_success);
            tvNotice.setText("退保-已退还保证金");
            tvApply.setVisibility(View.GONE);
        }

        merchantType = Integer.parseInt(acceptMerchantFrontVo.getMerchantType());
        presenter.getAcceptancesProcessInfo(merchantType);
    }

    @Override
    public void getSelfLevelInfoError(HttpErrorEntity httpErrorEntity) {
        dealError(httpErrorEntity);
    }

    @Override
    public void getAcceptancesProcessInfoSuccess(AcceptMerchantApplyMarginType acceptMerchantApplyMarginType) {
        if (acceptMerchantApplyMarginType != null) {
            id = acceptMerchantApplyMarginType.getId();
        }

    }

}
