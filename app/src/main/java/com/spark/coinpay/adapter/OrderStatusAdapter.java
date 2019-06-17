package com.spark.coinpay.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.R;

import java.util.List;

/**
 * Created by Administrator on 2019/3/2 0002.
 */

public class OrderStatusAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    private List<Object> data;

    public OrderStatusAdapter(int layoutResId, @Nullable List<Object> data) {
        super(layoutResId, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        int position = helper.getPosition();
        if (position == 0) {
            helper.getView(R.id.ivLineLeft).setVisibility(View.INVISIBLE);
        } else if (position == data.size() - 1) {
            helper.getView(R.id.ivLineRight).setVisibility(View.INVISIBLE);
        }

    }
}
