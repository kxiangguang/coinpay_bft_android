package com.spark.coinpay.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.library.acp.model.AppealApplyInTransitVo;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;

import java.util.List;

/**
 * 纠纷管理-进行中
 */

public class AppealIngAdapter extends BaseQuickAdapter<AppealApplyInTransitVo, BaseViewHolder> {

    public AppealIngAdapter(@LayoutRes int layoutResId, @Nullable List<AppealApplyInTransitVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppealApplyInTransitVo item) {
        int direction = item.getDirection();

        helper.setText(R.id.tvCount, MathUtils.subZeroAndDot(item.getAmount() + ""))
                .setText(R.id.tvOrderCreateTime, DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, item.getCreateTime()));


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
            helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_need_do));
            helper.setText(R.id.tvRealeaseTime, "");
            if (direction == 2) {
                helper.setText(R.id.tvPayTime, "");
            } else {
                helper.setText(R.id.tvPayTime, "");
            }
        } else {
            helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_already_pay));
            helper.setText(R.id.tvPayTime, DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, item.getPayTime()));
            helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_need_do));
            helper.setText(R.id.tvRealeaseTime, "");
        }


    }
}
