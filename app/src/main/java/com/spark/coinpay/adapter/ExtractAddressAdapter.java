package com.spark.coinpay.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.R;
import com.spark.moduleassets.entity.Address;
import com.spark.modulebase.utils.StringUtils;

import java.util.List;

/**
 * 提币地址
 */

public class ExtractAddressAdapter extends BaseQuickAdapter<Address, BaseViewHolder> {

    public ExtractAddressAdapter(@Nullable List<Address> data) {
        super(R.layout.adapter_extract_address, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Address item) {
        helper.setText(R.id.tvCoinUnit, item.getCoinId());
        helper.setText(R.id.tvAddress, item.getAddress());

        LinearLayout llRemark = helper.getView(R.id.llRemark);
        if (StringUtils.isNotEmpty(item.getRemark())) {
            helper.setText(R.id.tvReamrk, "备注： " + item.getRemark());
            llRemark.setVisibility(View.VISIBLE);
        } else {
            helper.setText(R.id.tvReamrk, "备注： ");
            llRemark.setVisibility(View.VISIBLE);
        }
    }


}
