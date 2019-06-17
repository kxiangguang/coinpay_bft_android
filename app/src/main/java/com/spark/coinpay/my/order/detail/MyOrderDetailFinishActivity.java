package com.spark.coinpay.my.order.detail;

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
import com.spark.coinpay.pay.PayActivity;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.coinpay.my.order.OrderFinishFragment.orderAchive;

/**
 * 我的订单详情-已完成
 */

public class MyOrderDetailFinishActivity extends BaseActivity {

    @BindView(R.id.ivType)
    ImageView ivType;
    @BindView(R.id.tvDirection)
    TextView tvDirection;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvOrderCreateTimeTag)
    TextView tvOrderCreateTimeTag;
    @BindView(R.id.tvOrderCreateTime)
    TextView tvOrderCreateTime;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvPayTime)
    TextView tvPayTime;
    @BindView(R.id.tvTypeTag)
    TextView tvTypeTag;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvRateTag)
    TextView tvRateTag;
    @BindView(R.id.tvRate)
    TextView tvRate;
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
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.ivDrop)
    ImageView ivDrop;
    @BindView(R.id.ivPayWay)
    ImageView ivPayWay;
    @BindView(R.id.tvSure)
    TextView tvSure;
    @BindView(R.id.llPayType)
    LinearLayout llPayType;
    @BindView(R.id.llRelease)
    LinearLayout llRelease;
    @BindView(R.id.tvRealease)
    TextView tvRealease;
    @BindView(R.id.tvRealeaseTime)
    TextView tvRealeaseTime;
    @BindView(R.id.ivPayReferCopy)
    ImageView ivPayReferCopy;
    @BindView(R.id.ivOrderNumCopy)
    ImageView ivOrderNumCopy;
    @BindView(R.id.tvOrderAmount)
    TextView tvOrderAmount;
    @BindView(R.id.tvOrderAmountRandom)
    TextView tvOrderAmountRandom;

    private List<PayData> payDatas;
    private String[] bankNames;
    private String orderId;//取消订单、放行、支付使用
    private boolean isPay = true;//1确认放行/2去支付
    private PayData curPayData;
    private int type = -1;//1支付宝、2微信、3银行卡、4PAYPAL

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.str_order_detail);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        if (orderAchive != null) {
            payDatas = new Gson().fromJson(orderAchive.getPayData(), new TypeToken<List<PayData>>() {
            }.getType());
            orderId = orderAchive.getOrderId();
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
            if (orderAchive.getDirection() == 2) {//1确认放行/2去支付
                isPay = true;
            } else {
                isPay = false;
            }
            initContent();
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    private void initContent() {
        int direction = orderAchive.getDirection();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        tvOrderCreateTime.setText(simpleDateFormat.format(orderAchive.getCreateTime()));
        tvPrice.setText(MathUtils.subZeroAndDot(orderAchive.getNumber() + ""));

        tvRate.setText(MathUtils.subZeroAndDot(orderAchive.getPrice() + ""));
        tvPayRefer.setText(orderAchive.getPayRefer());
        tvOrderNum.setText(orderAchive.getOrderId());
        tvRemark.setText("请不要备注任何信息");
        if (payDatas != null && payDatas.size() > 0) {
            String bankName = bankNames[0];
            tvBankName.setText(bankName);
            curPayData = payDatas.get(0);
            String payType = curPayData.getPayType();
            switch (payType) {
                case GlobalConstant.ALIPAY:
                    ivPayWay.setImageResource(R.mipmap.icon_ali);
                    type = 1;
                    break;
                case GlobalConstant.WECHAT:
                    ivPayWay.setImageResource(R.mipmap.icon_wechat);
                    type = 2;
                    break;
                case GlobalConstant.BANK:
                    ivPayWay.setImageResource(R.mipmap.icon_bank);
                    type = 3;
                    break;
                case GlobalConstant.PAYPAL:
                    ivPayWay.setImageResource(R.mipmap.icon_paypal);
                    type = 4;
                    break;
            }
        }
        tvSure.setVisibility(View.GONE);
        ivDrop.setVisibility(View.GONE);

        if (direction == 1) {//卖币
            tvSure.setText(R.string.str_sure_release);
            ivType.setImageResource(R.mipmap.icon_duiru);
            tvDirection.setText(R.string.str_duiru_cny);
            tvAmount.setTextColor(getResources().getColor(R.color.font_green));
            tvTypeTag.setText(String.format(getString(R.string.str_pay_usdt), orderAchive.getCoinId()));
            tvPayTypeTag.setText(getString(R.string.str_pay_mode));
            llRelease.setVisibility(View.VISIBLE);
        } else if (direction == 2) {//买币
            tvSure.setText(R.string.str_pay);
            ivType.setImageResource(R.mipmap.icon_duichu);
            tvDirection.setText(R.string.str_duichu_cny);
            tvAmount.setTextColor(getResources().getColor(R.color.font_red));
            tvTypeTag.setText(String.format(getString(R.string.str_get_usdt), orderAchive.getCoinId()));
            tvPayTypeTag.setText(getString(R.string.str_pay_way));
            llRelease.setVisibility(View.GONE);
        }

        if (StringUtils.isEmpty(orderAchive.getActualPayment())) {
            tvStatus.setText(getString(R.string.str_wait_pay));
            if (direction == 2) {
                tvPayTime.setText("");
            } else {
                tvPayTime.setText("");
            }
        } else {
            if (direction == 2) {
                tvStatus.setText(getString(R.string.str_already_pay_m));
            } else {
                tvStatus.setText(getString(R.string.str_already_pay));
            }
            tvPayTime.setText(simpleDateFormat.format(orderAchive.getPayTime()));
        }

        if (orderAchive.getReleaseTime() != null) {
            tvRealeaseTime.setText(simpleDateFormat.format(orderAchive.getReleaseTime()));
        } else {
            tvRealeaseTime.setText("");
        }

        if (orderAchive.getStatus() != null && orderAchive.getStatus() == 10) {
            tvRealease.setText(getString(R.string.str_already_release_force));
        } else if (orderAchive.getStatus() != null && orderAchive.getStatus() == 7) {
            tvRealease.setText(getString(R.string.str_already_release_cancel_force));
        } else {
            tvRealease.setText(getString(R.string.str_already_release));
        }

        if (orderAchive.getActualAmount() == null) {
            tvAmount.setText(MathUtils.subZeroAndDot(orderAchive.getAmount() + ""));
            tvOrderAmount.setText(MathUtils.subZeroAndDot(orderAchive.getAmount() + ""));
            BigDecimal minuse = orderAchive.getAmount().subtract(orderAchive.getAmount());
            tvOrderAmountRandom.setText(String.format(getResources().getString(R.string.str_order_random_cny), MathUtils.subZeroAndDot(minuse + "")));
        } else {
            tvAmount.setText(MathUtils.subZeroAndDot(orderAchive.getActualAmount() + ""));
            tvOrderAmount.setText(MathUtils.subZeroAndDot(orderAchive.getAmount() + ""));
            BigDecimal minuse = orderAchive.getAmount().subtract(orderAchive.getActualAmount());
            tvOrderAmountRandom.setText(String.format(getResources().getString(R.string.str_order_random_cny), MathUtils.subZeroAndDot(minuse + "")));
        }

    }

    @OnClick({R.id.ivPayReferCopy, R.id.ivOrderNumCopy})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivPayReferCopy://复制
                copyText(tvPayRefer.getText().toString());
                break;
            case R.id.ivOrderNumCopy://复制
                copyText(tvOrderNum.getText().toString());
                break;
        }
    }


}
