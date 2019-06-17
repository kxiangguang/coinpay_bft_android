package com.spark.coinpay.main.buy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.entity.PayData;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OneKeyBuyDto;
import com.spark.library.acp.model.OrderCompleteDto;
import com.spark.moduleassets.entity.ExtractInfo;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.ToastUtils;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 一键买币-第三步-待付款
 */

public class BuyUnPayActivity extends BaseActivity implements BuyContract.CancelView {

    @BindView(R.id.tvlimitTime)
    TextView tvlimitTime;
    @BindView(R.id.tvPayMoney)
    TextView tvPayMoney;
    @BindView(R.id.tvSeller)
    TextView tvSeller;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvOrdersn)
    TextView tvOrdersn;
    @BindView(R.id.tvRemark)
    TextView tvRemark;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.ivPayWay)
    ImageView ivPayWay;
    @BindView(R.id.tvPayType)
    TextView tvPayType;
    @BindView(R.id.tvCopy)
    TextView tvCopy;
    @BindView(R.id.tvPayFinish)
    TextView tvPayFinish;
    @BindView(R.id.llPayLayout)
    LinearLayout llPayLayout;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    private String actualPayment;
    private int type = -1;//1支付宝、2微信、3银行卡、4PAYPAL
    private String payMoney = "";//交易金额
    private String limitTime = "";//剩余时间
    private String payRefer = "";//付款参考号
    private String orderNo = "";
    //倒计时
    private long randomTime;//订单剩余时间（毫秒）
    private PayData curPayData;
    private PayEntity payEntity;
    private BuyCancelPresenterImpl presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_buy_unpay;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    setResult(Activity.RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payEntity = (PayEntity) bundle.getSerializable("data");
            actualPayment = bundle.getString("actualPayment");

            if (payEntity != null) {
                try {
                    tvStatus.setText("等待付款");
                    llPayLayout.setVisibility(View.VISIBLE);

                    List<PayData> payDatas = new Gson().fromJson(payEntity.getPayData(), new TypeToken<List<PayData>>() {
                    }.getType());
                    for (PayData payData : payDatas) {
                        if (actualPayment.equals(payData.getPayType())) {
                            curPayData = payData;
                        }
                    }

                    long endTime = payEntity.getCreateTime() + (int) payEntity.getTimeLimit() * 60 * 1000;
                    long nowTime = new Date().getTime();
                    randomTime = endTime - nowTime;
                    limitTime = DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", "");

                    tvlimitTime.setText(limitTime);
                    tvPayMoney.setText("" + payEntity.getMoney() + " " + payEntity.getLocalCurrency());
                    tvSeller.setText("" + payEntity.getTradeToUsername());
                    tvPrice.setText("" + payEntity.getPrice() + " " + payEntity.getLocalCurrency());
                    tvAmount.setText("" + payEntity.getNumber() + " " + payEntity.getCoinName());
                    tvOrdersn.setText("" + payEntity.getOrderSn());
                    tvTime.setText("" + DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, new Date(payEntity.getCreateTime())));
                    selectType(actualPayment);

                    payMoney = "" + payEntity.getMoney();
                    payRefer = "" + payEntity.getPayRefer();
                    orderNo = "" + payEntity.getOrderSn();

                    if (randomTime > 0) {
                        mTimeHandler.post(runnable);//刷新时间
                    } else {
                        mTimeHandler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    LogUtils.e("");
                }
            }

        }

    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new BuyCancelPresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    private void selectType(String actualPayment) {
        switch (actualPayment) {
            case GlobalConstant.ALIPAY:
                ivPayWay.setImageResource(R.mipmap.icon_ali);
                tvPayType.setText("支付宝");
                type = 1;
                break;
            case GlobalConstant.WECHAT:
                ivPayWay.setImageResource(R.mipmap.icon_wechat);
                tvPayType.setText("微信");
                type = 2;
                break;
            case GlobalConstant.BANK:
                ivPayWay.setImageResource(R.mipmap.icon_bank);
                tvPayType.setText("银行卡");
                type = 3;
                break;
        }
    }

    Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://刷新剩余时间
                    if (randomTime > 0) {
                        randomTime -= 1000;
                        setTime(randomTime);
                    } else {
                        setTime(0);
                        mTimeHandler.removeCallbacks(runnable);
                    }
                    break;
            }
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mTimeHandler.postDelayed(runnable, 1000);
            Message message = new Message();
            message.what = 1;
            mTimeHandler.sendMessage(message);
        }
    };

    /**
     * 设置时间
     *
     * @param randomTime
     */
    private void setTime(long randomTime) {
        limitTime = DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", "");
        if (tvlimitTime != null) {
            tvlimitTime.setText(limitTime);
        }
    }


    @OnClick({R.id.tvPayFinish, R.id.tvCopy})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvPayFinish:
                if (curPayData == null) {
                    ToastUtils.showToast("未找到收款方式");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("type", type);
                bundle.putSerializable("data", curPayData);
                bundle.putString("payMoney", payMoney);
                bundle.putString("limitTime", limitTime);
                bundle.putString("payRefer", payRefer);
                bundle.putString("orderNo", orderNo);
                bundle.putLong("randomTime", randomTime);
                showActivity(BuyPayFinishActivity.class, bundle, 1);
                break;
            case R.id.tvCopy://取消
                OrderCompleteDto orderCompleteDto = new OrderCompleteDto();
                orderCompleteDto.setOrderSn(payEntity.getOrderSn());
                presenter.cancelOrder(orderCompleteDto);
                break;
        }
    }

    @Override
    public void cancelSuccess(MessageResult response) {
        if (response != null && response.getData() != null) {
            setResult(RESULT_OK);
            finish();
        }

    }
}
