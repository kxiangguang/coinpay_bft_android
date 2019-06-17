package com.spark.coinpay.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.R;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseConstant;
import com.spark.modulebase.utils.MathUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资产
 */

public class WalletAdapter extends BaseQuickAdapter<Wallet, BaseViewHolder> {

    public WalletAdapter(@LayoutRes int layoutResId, @Nullable List<Wallet> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Wallet item) {
        helper.setText(R.id.tvCoinUnit, item.getCoinId())
                .setText(R.id.tvBuyCanUse, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getBalance(), BaseConstant.MONEY_FORMAT, null)))
                .setText(R.id.tvFrozon, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getFrozenBalance(), BaseConstant.MONEY_FORMAT, null)));
    }
}
