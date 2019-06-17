package com.spark.coinpay.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.R;
import com.spark.library.acp.model.Dict;

import java.util.List;

/**
 * 承兑商等级
 */

public class LevelAdapter extends BaseQuickAdapter<Dict, BaseViewHolder> {
    private List<Dict> data;

    public LevelAdapter(int layoutResId, @Nullable List<Dict> data) {
        super(layoutResId, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, Dict item) {
        helper.setText(R.id.tvLevel, item.getValue());
        helper.setText(R.id.tvLevelInfo, item.getDescript());
        int position = helper.getAdapterPosition();
        if (position == data.size() - 1) {
            helper.getView(R.id.ivLine).setVisibility(View.GONE);
        }
        if (item.getStatus() != null && item.getStatus() == 99) {
            helper.setGone(R.id.tvAdd, false);
            helper.setGone(R.id.ivMore, false);
        } else {
            helper.setVisible(R.id.tvAdd, true);
            helper.setVisible(R.id.ivMore, true);
        }
    }
}
