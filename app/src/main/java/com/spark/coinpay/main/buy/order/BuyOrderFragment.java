package com.spark.coinpay.main.buy.order;


import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseFragment;

/**
 * 订单-已完成
 */
public class BuyOrderFragment extends BaseFragment {
    public static BuyOrderEntity.RecordsBean buyOrderEntity;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.activity_common_listview;
    }

    @Override
    public String getmTag() {
        return this.getClass().getSimpleName();
    }
}
