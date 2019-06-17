package com.spark.coinpay.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.library.acp.model.OrderAchive;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 我的订单-已取消
 */

public class MyOrderCancelAdapter extends BaseQuickAdapter<OrderAchive, BaseViewHolder> {

    public MyOrderCancelAdapter(@LayoutRes int layoutResId, @Nullable List<OrderAchive> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderAchive item) {
        int direction = item.getDirection();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (item.getActualAmount() == null) {
            helper.setText(R.id.tvCount, MathUtils.subZeroAndDot(item.getAmount() + ""));
        } else {
            helper.setText(R.id.tvCount, MathUtils.subZeroAndDot(item.getActualAmount() + ""));
        }

        helper.setText(R.id.tvOrderCreateTime, simpleDateFormat.format(item.getCreateTime()));

        if (direction == 2) {//买币
            helper.setImageResource(R.id.ivType, R.mipmap.icon_duichu);
            helper.setText(R.id.tvTypeCNY, R.string.str_duichu_cny);
            helper.setTextColor(R.id.tvCount, MyApplication.getAppContext().getResources().getColor(R.color.font_red));
            helper.setGone(R.id.llRelease, false);
        } else {//卖币
            helper.setImageResource(R.id.ivType, R.mipmap.icon_duiru);
            helper.setText(R.id.tvTypeCNY, R.string.str_duiru_cny);
            helper.setTextColor(R.id.tvCount, MyApplication.getAppContext().getResources().getColor(R.color.font_green));
            helper.setGone(R.id.llRelease, false);
        }
        if (StringUtils.isEmpty(item.getActualPayment())) {
            helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_wait_pay));
            helper.setText(R.id.tvPayTime, MyApplication.getAppContext().getString(R.string.str_already_cancel));
            helper.setGone(R.id.llRelease, false);
        } else {
            if (direction == 2) {
                helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_already_pay_m));
            } else {
                helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_already_pay));
            }
            helper.setText(R.id.tvPayTime, simpleDateFormat.format(item.getPayTime()));
            helper.setGone(R.id.llRelease, true);
        }
        if (item.getReleaseTime() != null) {
            helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_already_release));
            helper.setText(R.id.tvRealeaseTime, simpleDateFormat.format(item.getReleaseTime()));
        } else {
            helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_need_release));
            helper.setText(R.id.tvRealeaseTime, MyApplication.getAppContext().getString(R.string.str_already_cancel));
        }

    }
}
