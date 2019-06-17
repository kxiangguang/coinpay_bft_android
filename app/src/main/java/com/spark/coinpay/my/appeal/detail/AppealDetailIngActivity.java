package com.spark.coinpay.my.appeal.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.entity.PayData;
import com.spark.modulebase.utils.MathUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.coinpay.my.appeal.AppealIngFragment.appealApplyInTransit;


/**
 * 申诉单详情-进行中
 */

public class AppealDetailIngActivity extends BaseActivity {


    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvDo)
    TextView tvDo;
    @BindView(R.id.tvAppealNum)
    TextView tvAppealNum;
    @BindView(R.id.tvCreateTime)
    TextView tvCreateTime;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.tvDoTime)
    TextView tvDoTime;
    @BindView(R.id.tvResult)
    TextView tvResult;
    @BindView(R.id.tvTypeTag)
    TextView tvTypeTag;
    @BindView(R.id.tvTypeTag2)
    TextView tvTypeTag2;
    @BindView(R.id.tvTypeValue)
    TextView tvTypeValue;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvRateTag)
    TextView tvRateTag;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvOrderCreateTime)
    TextView tvOrderCreateTime;
    @BindView(R.id.tvOrderConfirmTime)
    TextView tvOrderConfirmTime;
    @BindView(R.id.tvPayReferTag)
    TextView tvPayReferTag;
    @BindView(R.id.tvPayRefer)
    TextView tvPayRefer;
    @BindView(R.id.tvOrderNumTag)
    TextView tvOrderNumTag;
    @BindView(R.id.tvOrderNum)
    TextView tvOrderNum;
    @BindView(R.id.tvRemarkTag)
    TextView tvRemarkTag;
    @BindView(R.id.tvRemark)
    TextView tvRemark;
    @BindView(R.id.tvPayTypeTag)
    TextView tvPayTypeTag;
    @BindView(R.id.ivPayWay)
    ImageView ivPayWay;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.llPayType)
    LinearLayout llPayType;
    @BindView(R.id.ivAppealNumCopy)
    ImageView ivAppealNumCopy;
    @BindView(R.id.ivPayReferCopy)
    ImageView ivPayReferCopy;
    @BindView(R.id.ivOrderNumCopy)
    ImageView ivOrderNumCopy;

    private List<PayData> payDatas;
    private String[] bankNames;
    private PayData curPayData;

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.str_detail);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_appeal_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        if (appealApplyInTransit != null) {
            payDatas = new Gson().fromJson(appealApplyInTransit.getPayData(), new TypeToken<List<PayData>>() {
            }.getType());
            if (payDatas != null && payDatas.size() > 0) {
                bankNames = new String[payDatas.size()];
                for (int i = 0; i < payDatas.size(); i++) {
                    String payType = payDatas.get(i).getPayType();
                    switch (payType) {
                        case GlobalConstant.ALIPAY:
                            bankNames[i] = "支付宝";
                            break;
                        case GlobalConstant.WECHAT:
                            bankNames[i] = "微信";
                            break;
                        case GlobalConstant.BANK:
                            if (payDatas.get(i).getPayAddress().length() > 4) {
                                bankNames[i] = payDatas.get(i).getBank() + "(" + payDatas.get(i).getPayAddress().substring(payDatas.get(i).getPayAddress().length() - 4) + ")";
                            }
                            break;
                        case GlobalConstant.PAYPAL:
                            bankNames[i] = "PAYPAL";
                            break;
                    }
                }
            }
            initContent();
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    private void initContent() {
        int direction = appealApplyInTransit.getDirection();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (appealApplyInTransit.getStatus() != null) {//状态0-未处理 1-已处理
            if (appealApplyInTransit.getStatus() == 0) {
                tvDo.setText("未处理");
                tvDoTime.setText("");
            } else {
                tvDo.setText("已处理");
                tvDoTime.setText(simpleDateFormat.format(appealApplyInTransit.getDealWithTime()));
            }
        } else {
            tvDo.setText("");
            tvDoTime.setText("");
        }
        tvAppealNum.setText(appealApplyInTransit.getId() + "");
        tvCreateTime.setText(simpleDateFormat.format(appealApplyInTransit.getCreateTime()));
        tvContent.setText(appealApplyInTransit.getContent());
        if (appealApplyInTransit.getIsSuccess() != null) {//申诉结果0 败诉 1胜诉 2已关闭
            if (appealApplyInTransit.getIsSuccess() == 0) {
                tvResult.setText(MyApplication.getAppContext().getString(R.string.str_appeal_success));
            } else if (appealApplyInTransit.getIsSuccess() == 1) {
                tvResult.setText(MyApplication.getAppContext().getString(R.string.str_appeal_fail));
            } else if (appealApplyInTransit.getIsSuccess() == 2) {
                tvResult.setText(MyApplication.getAppContext().getString(R.string.str_appeal_close));
            }
        } else {
            tvResult.setText("");
        }


        tvTypeValue.setText(MathUtils.subZeroAndDot(appealApplyInTransit.getAmount() + ""));
        tvAmount.setText(MathUtils.subZeroAndDot(appealApplyInTransit.getNumber() + ""));
        tvRate.setText(MathUtils.subZeroAndDot(appealApplyInTransit.getPrice() + ""));
        tvOrderCreateTime.setText(simpleDateFormat.format(appealApplyInTransit.getOrderCreateTime()));
        if (appealApplyInTransit.getReleaseTime() != null) {
            tvOrderConfirmTime.setText(simpleDateFormat.format(appealApplyInTransit.getReleaseTime()));
        } else {
            tvOrderConfirmTime.setText("");
        }

        tvPayRefer.setText(appealApplyInTransit.getPayRefer());
        tvOrderNum.setText(appealApplyInTransit.getOrderId());
        tvRemark.setText("请不要备注任何信息");

        if (payDatas != null && payDatas.size() > 0) {
            String bankName = bankNames[0];
            tvBankName.setText(bankName);
            curPayData = payDatas.get(0);
            String payType = curPayData.getPayType();
            switch (payType) {
                case GlobalConstant.ALIPAY:
                    ivPayWay.setImageResource(R.mipmap.icon_ali);
                    break;
                case GlobalConstant.WECHAT:
                    ivPayWay.setImageResource(R.mipmap.icon_wechat);
                    break;
                case GlobalConstant.BANK:
                    ivPayWay.setImageResource(R.mipmap.icon_bank);
                    break;
                case GlobalConstant.PAYPAL:
                    ivPayWay.setImageResource(R.mipmap.icon_paypal);
                    break;
            }
        }

        if (direction == 1) {//卖币
            tvType.setText(getResources().getString(R.string.str_duiru));
            tvTypeTag2.setText(getResources().getString(R.string.str_duiru_cny));
            tvTypeTag.setText(String.format(getString(R.string.str_pay_usdt), appealApplyInTransit.getCoinId()));
            tvPayTypeTag.setText(getString(R.string.str_pay_mode));
        } else if (direction == 2) {//买币
            tvType.setText(getResources().getString(R.string.str_duichu));
            tvTypeTag2.setText(getResources().getString(R.string.str_duichu_cny));
            tvTypeTag.setText(String.format(getString(R.string.str_get_usdt), appealApplyInTransit.getCoinId()));
            tvPayTypeTag.setText(getString(R.string.str_pay_way));
        }

    }

    @OnClick({R.id.ivAppealNumCopy, R.id.ivPayReferCopy, R.id.ivOrderNumCopy})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivAppealNumCopy://复制
                copyText(tvAppealNum.getText().toString());
                break;
            case R.id.ivPayReferCopy://复制
                copyText(tvPayRefer.getText().toString());
                break;
            case R.id.ivOrderNumCopy://复制
                copyText(tvOrderNum.getText().toString());
                break;
        }
    }
}
