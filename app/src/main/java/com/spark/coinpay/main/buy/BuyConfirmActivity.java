package com.spark.coinpay.main.buy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.library.acp.model.MessageResult;
import com.spark.library.acp.model.OneKeyBuyDto;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 一键买币-第二步-确认购买
 */

public class BuyConfirmActivity extends BaseActivity implements BuyContract.ConfirmView {

    @BindView(R.id.tvPayMoney)
    TextView tvPayMoney;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvUse)
    TextView tvUse;
    @BindView(R.id.tvPayFinish)
    TextView tvPayFinish;

    private String money;
    private String actualPayment;
    private String price;
    private String coinName;
    private String amount;
    private int buyType;
    private String address;
    private String memberId;

    private BuyConfirmPresenterImpl presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_buy_confirm;
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
        tvTitle.setText("确认购买");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            money = bundle.getString("money");
            actualPayment = bundle.getString("actualPayment");
            price = bundle.getString("price");
            coinName = bundle.getString("coinName");
            amount = bundle.getString("amount");
            buyType = bundle.getInt("buyType");
            address = bundle.getString("address");
            memberId = bundle.getString("memberId");

            tvPayMoney.setText(money + " CNY");
            tvAmount.setText(price + " CNY/" + coinName);
            tvUse.setText(amount + " " + coinName);
            switch (actualPayment) {//1支付宝、2微信、3银行卡、4PAYPAL
                case GlobalConstant.ALIPAY://1支付宝
                    tvMessage.setText(R.string.str_ali);
                    break;
                case GlobalConstant.WECHAT://2微信
                    tvMessage.setText(R.string.str_wechat);
                    break;
                case GlobalConstant.BANK://3银行卡
                    tvMessage.setText(getString(R.string.str_bank));
                    break;
            }

        }

    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new BuyConfirmPresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @OnClick({R.id.tvPayFinish})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvPayFinish:
                OneKeyBuyDto tradeDto = new OneKeyBuyDto();
                if (buyType == 1) {//1按数量购买  2按金额购买
                    tradeDto.setTradeType(0);
                    tradeDto.setAmount(new BigDecimal(amount));
                    tradeDto.setCoinName(coinName);
                    tradeDto.setCurrency(GlobalConstant.CNY);
                    tradeDto.setMoney(BigDecimal.ZERO);
                    tradeDto.setActualPayment(actualPayment);
                    tradeDto.setAcceptanceMemberId(Long.parseLong(memberId));
                    tradeDto.setAddress(address);
                } else {
                    tradeDto.setTradeType(1);
                    tradeDto.setAmount(BigDecimal.ZERO);
                    tradeDto.setCoinName(coinName);
                    tradeDto.setCurrency(GlobalConstant.CNY);
                    tradeDto.setMoney(new BigDecimal(money));
                    tradeDto.setActualPayment(actualPayment);
                    tradeDto.setAcceptanceMemberId(Long.parseLong(memberId));
                    tradeDto.setAddress(address);
                }
                presenter.createOrder(tradeDto);
                break;
        }
    }

    @Override
    public void createOrderSuccess(MessageResult response) {
        if (response != null && response.getData() != null) {
            String gson = new Gson().toJson(response.getData());
            PayEntity payEntity = new Gson().fromJson(gson, PayEntity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", payEntity);
            bundle.putString("actualPayment", actualPayment);
            showActivity(BuyUnPayActivity.class, bundle, 1);
        }

    }

}
