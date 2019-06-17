package com.spark.coinpay.acceptances.process;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.spark.coinpay.R;
import com.spark.coinpay.acceptances.authentication.AuthenticationActivity;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.view.ConfirmIdentifyDialog;
import com.spark.library.acp.model.AcceptMerchantApplyMarginType;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 认证流程
 */
public class AcceptancesProcessActivity extends BaseActivity implements AcceptancesProcessContract.AcceptancesProcessView {
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.tvAddNow)
    TextView tvAddNow;
    @BindView(R.id.tvAmount)
    TextView tvAmount;

    //private ConfirmIdentifyDialog identifyDialog;
    private AcceptancesProcessPresenterImpl presenter;
    private int key;
    private Long id;
    private double buy = 0;//交易账户余额
    private double amount = 0;//保证金金额
    private String coinName = "";//单位

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_acceptances_process;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_detify));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new AcceptancesProcessPresenterImpl(this);
//        identifyDialog = new ConfirmIdentifyDialog(activity);
//        initWebview();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = Integer.parseInt(bundle.getString("key"));
            buy = bundle.getDouble("buy");
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        presenter.getAcceptancesProcessInfo(key);
    }

    @OnClick({R.id.tvAgreement, R.id.tvAddNow})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvAgreement:
                showActivity(AcceptancesProcessTextActivity.class, null);
                break;
            case R.id.tvAddNow:
                if (!checkbox.isChecked()) {
                    ToastUtils.showToast(activity, getString(R.string.str_agree_tag));
                    return;
                }
                if (buy >= amount) {
//                    identifyDialog.setContent(MathUtils.subZeroAndDot(amount + "") + coinName);
//                    identifyDialog.show();
                    presenter.doAuthencation(id, "", "", "");
                } else {
                    ToastUtils.showToast(activity, getString(R.string.str_trade_account_tag));
                }
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
//        identifyDialog.setOnPositiveClickLister(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putLong("id", id);
//                showActivity(AuthenticationActivity.class, bundle, 1);
//                presenter.doAuthencation(id, "", "", "");
//                identifyDialog.dismiss();
//            }
//        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvAddNow.setEnabled(true);
                    tvAddNow.setBackgroundResource(R.drawable.ripple_btn_global_option_corner_selector);
                    tvAddNow.setTextColor(getResources().getColor(R.color.white));
                } else {
                    tvAddNow.setEnabled(false);
                    tvAddNow.setBackgroundResource(R.drawable.shape_bg_normal_corner_grey_enabled);
                    tvAddNow.setTextColor(getResources().getColor(R.color.font_main_content));
                }
            }
        });
    }

    @Override
    public void getAcceptancesProcessInfoSuccess(AcceptMerchantApplyMarginType acceptMerchantApplyMarginType) {
        String htm = "";
        if (acceptMerchantApplyMarginType != null) {
            id = acceptMerchantApplyMarginType.getId();
            if (acceptMerchantApplyMarginType.getAmount() != null) {
                amount = acceptMerchantApplyMarginType.getAmount().doubleValue();
                coinName = acceptMerchantApplyMarginType.getCoinName();
                if (amount == 0) {
                    htm = "3.保证金缴纳：<font color=#FD5858>无</font>；";
                } else {
                    htm = "3.保证金缴纳：<font color=#FD5858>" + MathUtils.subZeroAndDot(amount + "") + " " + coinName + "</font>；";
                }
            }
        } else {
            htm = "3.保证金缴纳：<font color=#FD5858>无</font>；";
        }
        tvAmount.setText(Html.fromHtml(htm));
    }

    @Override
    public void doAuthencationSuccess(String response) {
        ToastUtils.showToast(activity, response);
        setResult(RESULT_OK);
        finish();
    }
}
