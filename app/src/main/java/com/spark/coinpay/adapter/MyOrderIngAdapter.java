package com.spark.coinpay.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.event.TimeDownFinishEvent;
import com.spark.library.acp.model.OrderInTransit;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 我的订单-进行中
 */

public class MyOrderIngAdapter extends BaseQuickAdapter<OrderInTransit, BaseViewHolder> {

    public MyOrderIngAdapter(@LayoutRes int layoutResId, @Nullable List<OrderInTransit> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInTransit item) {
        int direction = item.getDirection();

        if (item.getActualAmount() == null) {
            helper.setText(R.id.tvCount, MathUtils.subZeroAndDot(item.getAmount() + ""));
        } else {
            helper.setText(R.id.tvCount, MathUtils.subZeroAndDot(item.getActualAmount() + ""));
        }

        helper.setText(R.id.tvOrderCreateTime, DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, item.getCreateTime()));

        if (direction == 2) {//买币
            helper.setImageResource(R.id.ivType, R.mipmap.icon_duichu);
            helper.setText(R.id.tvTypeCNY, R.string.str_duichu_cny);
            helper.setTextColor(R.id.tvCount, MyApplication.getAppContext().getResources().getColor(R.color.font_red));
            helper.setGone(R.id.llRelease, false);
        } else {//卖币
            helper.setImageResource(R.id.ivType, R.mipmap.icon_duiru);
            helper.setText(R.id.tvTypeCNY, R.string.str_duiru_cny);
            helper.setTextColor(R.id.tvCount, MyApplication.getAppContext().getResources().getColor(R.color.font_green));
            helper.setGone(R.id.llRelease, true);
        }
        if (StringUtils.isEmpty(item.getActualPayment())) {
            helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_wait_pay));
            helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_need_release));
            helper.setText(R.id.tvRealeaseTime, "");
            if (direction == 2) {
                long endTime = item.getCreateTime().getTime() + item.getTimeLimit() * 1000;
                long nowTime = new Date().getTime();
                long randomTime = endTime - nowTime;
                LogUtils.e("时间差111：" + randomTime);
                if (randomTime > 0) {
                    setTime(1, randomTime, helper);
                } else {
                    setTime(1, 0, helper);
                    EventBus.getDefault().post(new TimeDownFinishEvent(randomTime));
                }
            } else {
                long endTime = item.getCreateTime().getTime() + item.getTimeLimit() * 1000;
                long nowTime = new Date().getTime();
                long randomTime = endTime - nowTime;
                LogUtils.e("时间差11111111：" + randomTime);
                if (randomTime > 0) {
                    setTime(4, randomTime, helper);
                } else {
                    setTime(4, 0, helper);
                    EventBus.getDefault().post(new TimeDownFinishEvent(randomTime));
                }
                helper.setGone(R.id.llRelease, false);
            }
        } else {
            helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_already_pay));
            helper.setText(R.id.tvPayTime, DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, item.getPayTime()));

            if (item.getStatus() != null) {
                //订单状态 0-已取消 1-未付款 2-已付款 3-已完成 4-申诉中 5.超时单
                if (item.getStatus() == 5) {//超时单
                    helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_need_release_out));
                    helper.setText(R.id.tvRealeaseTime, "");
//                    long endTime = item.getCreateTime().getTime() + item.getTimeLimit() * 1000;
//                    long nowTime = new Date().getTime();
//                    long randomTime = endTime - nowTime;
//                    //LogUtils.e("时间差222：" + randomTime);
//                    if (randomTime > 0) {
//                        setTime(3, randomTime, helper);
//                    } else {
//                        setTime(3, 0, helper);
//                        EventBus.getDefault().post(new TimeDownFinishEvent(randomTime));
//                    }
                } else {
                    helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_need_release));
                    long endTime = item.getCreateTime().getTime() + item.getTimeLimit() * 1000;
                    long nowTime = new Date().getTime();
                    long randomTime = endTime - nowTime;
                    LogUtils.e("时间差222：" + randomTime);
                    if (randomTime > 0) {
                        setTime(2, randomTime, helper);
                    } else {
                        setTime(2, 0, helper);
                        EventBus.getDefault().post(new TimeDownFinishEvent(randomTime));
                    }
                }
            } else {
                helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_need_release));
                helper.setText(R.id.tvRealeaseTime, "");
            }
        }

    }

    /**
     * 设置时间
     *
     * @param position
     * @param randomTime
     * @param helper
     */
    private void setTime(int position, long randomTime, BaseViewHolder helper) {
        if (helper != null) {
            if (position == 1) {//1待付款 2待放行
                helper.setText(R.id.tvPayTime, DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_3));
            } else if (position == 2) {
                helper.setText(R.id.tvRealeaseTime, DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_4));
            } else if (position == 3) {
                helper.setText(R.id.tvRealeaseTime, DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_4));
            } else if (position == 4) {
                helper.setText(R.id.tvPayTime, DateUtils.getTimeDown(randomTime, DateUtils.DATE_FORMAT_2, "", DateUtils.DATE_FORMAT_4));
            }
        }
    }

}
