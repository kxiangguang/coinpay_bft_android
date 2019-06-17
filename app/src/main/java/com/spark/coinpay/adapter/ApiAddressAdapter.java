package com.spark.coinpay.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.MyApplication;
import com.spark.coinpay.R;
import com.spark.coinpay.entity.ApiAddressEntity;
import com.spark.coinpay.entity.PayWaySetting;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.MathUtils;
import com.spark.modulebase.utils.StringUtils;

import java.util.List;

/**
 * Api地址
 */
public class ApiAddressAdapter extends BaseQuickAdapter<ApiAddressEntity, BaseViewHolder> {
    private List<ApiAddressEntity> payWaySettings;
    private String apiAddress;

    public ApiAddressAdapter(int layoutResId, @Nullable List<ApiAddressEntity> data, String apiAddress) {
        super(layoutResId, data);
        this.payWaySettings = data;
        this.apiAddress = apiAddress;
    }

    @Override
    protected void convert(BaseViewHolder helper, ApiAddressEntity item) {
        if (apiAddress.equals(item.getApiAddress())) {
            helper.setImageResource(R.id.ivType, R.mipmap.icon_selected);
            helper.setVisible(R.id.ivType, true);
        } else {
            helper.setImageResource(R.id.ivType, R.mipmap.icon_selected);
            helper.setVisible(R.id.ivType, false);
        }
        helper.setText(R.id.tvAppName, item.getAppName()).setText(R.id.tvApiAddress, item.getApiAddress());
    }
}
