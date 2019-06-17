package com.spark.coinpay.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.R;
import com.spark.coinpay.utils.FormatDataUtils;
import com.spark.moduleuc.entity.CountryEntity;

import java.util.List;

/**
 * 国家
 * Created by Administrator on 2018/3/1.
 */

public class CountryAdapter extends BaseQuickAdapter<CountryEntity, BaseViewHolder> {
    private Context context;

    public CountryAdapter(int layoutResId, @Nullable List<CountryEntity> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CountryEntity item) {
        helper.setText(R.id.tvname, FormatDataUtils.getViewNameByCode(item, context));
    }

}
