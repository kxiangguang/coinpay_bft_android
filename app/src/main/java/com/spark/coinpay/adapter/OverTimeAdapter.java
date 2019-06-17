package com.spark.coinpay.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 超时单
 */

public class OverTimeAdapter extends BaseQuickAdapter<OrderInTransit, BaseViewHolder> {

    public OverTimeAdapter(@LayoutRes int layoutResId, @Nullable List<OrderInTransit> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInTransit item) {
        int direction = item.getDirection();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("m分s秒");

        if (item.getActualAmount() == null) {
            helper.setText(R.id.tvCount, MathUtils.subZeroAndDot(item.getAmount() + ""));
        } else {
            helper.setText(R.id.tvCount, MathUtils.subZeroAndDot(item.getActualAmount() + ""));
        }

        helper.setText(R.id.tvOrderCreateTime, simpleDateFormat.format(item.getCreateTime()))
                .setText(R.id.tvPayCount, MathUtils.subZeroAndDot(item.getNumber() + ""))
                .setText(R.id.tvRate, MathUtils.subZeroAndDot(item.getPrice() + ""))
                .setText(R.id.tvPayRefer, item.getPayRefer());


        if (direction == 2) {//买币
            helper.setImageResource(R.id.ivType, R.mipmap.icon_duichu);
            helper.setText(R.id.tvTypeCNY, R.string.str_duichu_cny);
            helper.setTextColor(R.id.tvCount, MyApplication.getAppContext().getResources().getColor(R.color.font_red));
            helper.setText(R.id.tvDirection, String.format(MyApplication.getMyApplication().getString(R.string.str_get_usdt), item.getCoinId()));
        } else {//卖币
            helper.setImageResource(R.id.ivType, R.mipmap.icon_duiru);
            helper.setText(R.id.tvTypeCNY, R.string.str_duiru_cny);
            helper.setTextColor(R.id.tvCount, MyApplication.getAppContext().getResources().getColor(R.color.font_green));
            helper.setText(R.id.tvDirection, String.format(MyApplication.getMyApplication().getString(R.string.str_pay_usdt), item.getCoinId()));
        }
        if (StringUtils.isEmpty(item.getActualPayment())) {
            helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_wait_pay_tag));
            helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_need_release));
            helper.setText(R.id.tvRealeaseTime, "");
            if (direction == 2) {
                helper.setText(R.id.tvPayTime, simpleDateFormat2.format(item.getTimeLimit() * 1000) + "后订单取消");
            } else {
                helper.setText(R.id.tvPayTime, "");
            }
            helper.setGone(R.id.llRelease, false);
            helper.setText(R.id.tvWaitPay, MyApplication.getAppContext().getString(R.string.str_wait_pay_tag));

        } else {
            helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_already_pay));
            helper.setText(R.id.tvPayTime, simpleDateFormat.format(item.getPayTime()));
            helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_need_release_out));
            helper.setText(R.id.tvRealeaseTime, "");
            helper.setGone(R.id.llRelease, true);
            helper.setText(R.id.tvWaitPay, MyApplication.getAppContext().getString(R.string.str_need_release_out));
        }

        //按钮监听
        helper.addOnClickListener(R.id.tvRelease);
        helper.addOnClickListener(R.id.ivPayReferCopy);
    }

}
