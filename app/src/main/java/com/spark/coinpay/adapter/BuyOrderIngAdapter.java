package com.spark.coinpay.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.R;
import com.spark.coinpay.main.buy.order.BuyOrderEntity;
import com.spark.modulebase.utils.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * 我的订单-进行中
 */

public class BuyOrderIngAdapter extends BaseQuickAdapter<BuyOrderEntity.RecordsBean, BaseViewHolder> {

    public BuyOrderIngAdapter(@LayoutRes int layoutResId, @Nullable List<BuyOrderEntity.RecordsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BuyOrderEntity.RecordsBean item) {

        helper.setText(R.id.tvUserName, item.getTradeToUsername())
                .setText(R.id.tvTime, DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, new Date(item.getCreateTime())))
                .setText(R.id.tvAmount, item.getNumber() + " " + item.getCoinName())
                .setText(R.id.tvMoney, item.getMoney() + " CNY");

        //status 订单状态 0-已取消 1-未付款 2-已付款 3-已完成 4-申诉中
        int status = item.getStatus();
        switch (status) {
            case 0:
                helper.setText(R.id.tvStatus, "已取消");
                break;
            case 1:
                helper.setText(R.id.tvStatus, "未付款");
                break;
            case 2:
                helper.setText(R.id.tvStatus, "已付款");
                break;
            case 3:
                helper.setText(R.id.tvStatus, "已完成");
                break;
            case 4:
                helper.setText(R.id.tvStatus, "申诉中");
                break;
            default:
                helper.setText(R.id.tvStatus, "未知状态");
                break;
        }

    }

}
