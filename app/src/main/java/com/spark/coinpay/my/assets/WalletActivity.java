package com.spark.coinpay.my.assets;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.spark.coinpay.R;
import com.spark.coinpay.adapter.MyfragmentPagerAdpter;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的资产
 */
public class WalletActivity extends BaseActivity {
    @BindView(R.id.vpAssets)
    ViewPager vpAssets;
    @BindView(R.id.llTabSwitch)
    LinearLayout llTabSwitch;
    @BindView(R.id.tvLeft)
    TextView tvLeft;
    @BindView(R.id.tvRight)
    TextView tvRight;
    private MyfragmentPagerAdpter adapter;
    private List<BaseFragment> list = new ArrayList<>();
    private BaseAccountFragment baseAccountFragment;
    private TradeAccountFragment tradeAccountFragment;
    private double baseBalance = 0;
    private double tradeBalance = 0;

    public void setBaseBalance(double baseBalance) {
        this.baseBalance = baseBalance;
    }

    public void setTradeBalance(double tradeBalance) {
        this.tradeBalance = tradeBalance;
    }

    public double getBaseBalance() {
        return baseBalance;
    }

    public double getTradeBalance() {
        return tradeBalance;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        llTabSwitch.setVisibility(View.VISIBLE);
        tvLeft.setText(getString(R.string.str_base_account));
        tvLeft.setSelected(true);
        tvRight.setText(getString(R.string.str_trade_account));
    }

    @Override
    protected void initData() {
        super.initData();
        initPager();
    }

    private void initPager() {
        if (baseAccountFragment == null) {
            baseAccountFragment = new BaseAccountFragment();
        }
        if (tradeAccountFragment == null) {
            tradeAccountFragment = new TradeAccountFragment();
        }
        list.add(baseAccountFragment);
        list.add(tradeAccountFragment);
        adapter = new MyfragmentPagerAdpter(getSupportFragmentManager(), list);
        vpAssets.setAdapter(adapter);
        vpAssets.setCurrentItem(0);
    }

    public List<BaseFragment> getList() {
        return list;
    }

    @Override
    protected void setListener() {
        super.setListener();
        vpAssets.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvLeft.setSelected(true);
                        tvRight.setSelected(false);
                        break;
                    case 1:
                        tvLeft.setSelected(false);
                        tvRight.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @OnClick({R.id.tvLeft, R.id.tvRight})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvLeft:
                vpAssets.setCurrentItem(0);
                break;
            case R.id.tvRight:
                vpAssets.setCurrentItem(1);
                break;
        }
    }

}
