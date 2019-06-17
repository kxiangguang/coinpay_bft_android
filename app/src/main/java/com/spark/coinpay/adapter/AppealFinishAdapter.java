package com.spark.coinpay.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.library.acp.model.AppealApplyAchiveVo;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 纠纷管理-已完成
 */

public class AppealFinishAdapter extends BaseQuickAdapter<AppealApplyAchiveVo, BaseViewHolder> {

    public AppealFinishAdapter(@LayoutRes int layoutResId, @Nullable List<AppealApplyAchiveVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppealApplyAchiveVo item) {
        int direction = item.getDirection();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        helper.setText(R.id.tvCount, MathUtils.subZeroAndDot(item.getAmount()+""))
                .setText(R.id.tvOrderCreateTime, simpleDateFormat.format(item.getCreateTime()));

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
            helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_has_finish));
            helper.setText(R.id.tvRealeaseTime, "");
            if (direction == 2) {
                helper.setText(R.id.tvPayTime, "");
            } else {
                helper.setText(R.id.tvPayTime, "");
            }
        } else {
            if (direction == 2) {
                helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_already_pay_m));
            } else {
                helper.setText(R.id.tvPay, MyApplication.getAppContext().getString(R.string.str_already_pay));
            }
            helper.setText(R.id.tvPayTime, simpleDateFormat.format(item.getPayTime()));
            helper.setText(R.id.tvRealease, MyApplication.getAppContext().getString(R.string.str_has_finish));
            if (item.getIsSuccess() != null) {//申诉结果0 败诉 1胜诉 2已关闭
                if (item.getIsSuccess() == 0) {
                    helper.setText(R.id.tvRealeaseTime, MyApplication.getAppContext().getString(R.string.str_appeal_success));
                } else if (item.getIsSuccess() == 1) {
                    helper.setText(R.id.tvRealeaseTime, MyApplication.getAppContext().getString(R.string.str_appeal_fail));
                } else if (item.getIsSuccess() == 2) {
                    helper.setText(R.id.tvRealeaseTime, MyApplication.getAppContext().getString(R.string.str_appeal_success));
                }
            } else {
                helper.setText(R.id.tvRealeaseTime, "");
            }
        }


    }
}
