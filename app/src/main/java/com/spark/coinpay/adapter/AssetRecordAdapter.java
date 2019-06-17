package com.spark.coinpay.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.library.ac.model.MessageResultPageMemberTransactionVo;
import com.spark.moduleassets.entity.AssetRecord;
import com.spark.moduleassets.entity.Wallet;
import com.spark.modulebase.base.BaseConstant;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.MathUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资产记录
 */

public class AssetRecordAdapter extends BaseQuickAdapter<AssetRecord, BaseViewHolder> {

    public AssetRecordAdapter(@LayoutRes int layoutResId, @Nullable List<AssetRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AssetRecord item) {
        //   subType
        //        <item>保证金缴纳</item>1
        //        <item>保证金退还</item>2
        //        <item>交易赔付</item>3
        //        <item>卖币</item>4
        //        <item>买币</item>5
        //        <item>充币</item>6
        //        <item>提币</item>7
        //        <item>返佣</item>8
        //        <item>交易赔付</item>9

        //   type
        //        <item>充币</item>1
        //        <item>提币</item>2
        //        <item>转入</item>3
        //        <item>转出</item>4
        //        <item>资金交易-扣除</item>5
        //        <item>资金交易-增加</item>6
        int type = 6;//5资金交易-扣除 6资金交易-增加
        switch (item.getSubType()) {
            case 1:
                helper.setText(R.id.tvType, "保证金缴纳");
                type = 5;
                break;
            case 2:
                helper.setText(R.id.tvType, "保证金退还");
                type = 6;
                break;
            case 3:
                helper.setText(R.id.tvType, "保证金赔付");
                type = 5;
                break;
            case 4:
                helper.setText(R.id.tvType, "卖币");
                type = 5;
                break;
            case 5:
                helper.setText(R.id.tvType, "买币");
                type = 6;
                break;
            case 6:
                helper.setText(R.id.tvType, "充币");
                type = 6;
                break;
            case 7:
                helper.setText(R.id.tvType, "提币");
                type = 5;
                break;
            case 8:
                helper.setText(R.id.tvType, "返佣");
                type = 6;
                break;
            case 9:
                helper.setText(R.id.tvType, "交易赔付");
                type = 5;
                break;
            default:
                switch (item.getType()) {
                    case 1:
                        helper.setText(R.id.tvType, "充币");
                        type = 6;
                        break;
                    case 2:
                        helper.setText(R.id.tvType, "提币");
                        type = 5;
                        break;
                    case 3:
                        helper.setText(R.id.tvType, "转入");
                        type = 6;
                        break;
                    case 4:
                        helper.setText(R.id.tvType, "转出");
                        type = 5;
                        break;
                    case 5:
                        helper.setText(R.id.tvType, "资金交易-扣除");
                        type = 5;
                        break;
                    case 6:
                        helper.setText(R.id.tvType, "资金交易-增加");
                        type = 6;
                        break;
                    default:
                        helper.setText(R.id.tvType, "未知类型");
                        type = 6;
                        break;
                }
                break;
        }
        if (type == 5) {
            helper.setTextColor(R.id.tvType, MyApplication.getAppContext().getResources().getColor(R.color.font_red));
            helper.setText(R.id.tvBuyCanUse, "- " + MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getAmount(), BaseConstant.MONEY_FORMAT, null)) + " " + item.getCoinName());
        } else if (type == 6) {
            helper.setTextColor(R.id.tvType, MyApplication.getAppContext().getResources().getColor(R.color.bg_btn_normal));
            helper.setText(R.id.tvBuyCanUse, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getAmount(), BaseConstant.MONEY_FORMAT, null)) + " " + item.getCoinName());
        } else {
            helper.setTextColor(R.id.tvType, MyApplication.getAppContext().getResources().getColor(R.color.bg_btn_normal));
            helper.setText(R.id.tvBuyCanUse, MathUtils.subZeroAndDot(MathUtils.getRundNumber(item.getAmount(), BaseConstant.MONEY_FORMAT, null)) + " " + item.getCoinName());
        }

        helper.setText(R.id.tvFrozon, DateUtils.getFormatTime("HH:mm:ss MM/dd", item.getCreateTime()));
    }
}
