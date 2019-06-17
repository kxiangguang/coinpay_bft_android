package com.spark.coinpay.my.keep_alive;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.base.WebViewActivity;
import com.spark.coinpay.utils.MobileInfoUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class KeepAliveActivity extends BaseActivity {

    @BindView(R.id.llHelp)
    TextView llHelp;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_keep_alive;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_my_set_premission));
        String htm = "<font color=#313131>不会设置？</font> 点我看教程";
        llHelp.setText(Html.fromHtml(htm));
    }

    @OnClick({R.id.llChinese, R.id.llEnglish, R.id.llHelp})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llChinese://权限隐私
                MobileInfoUtils.jumpStartInterface(activity);
                break;
            case R.id.llEnglish://防睡眠设置/电池
                MobileInfoUtils.jumpToBatteryInfo(activity);
                break;
            case R.id.llHelp://帮助
                getBrand();
                break;
        }
    }

    /**
     * 获取手机品牌
     * http://cas.bitaccept.bench.bitpay.com?id=xiaomi
     */
    private void getBrand() {
        String name = MobileInfoUtils.getMobileType();
        LogUtils.e("获取手机品牌MANUFACTURER --- name : " + name);
        if (StringUtils.isEmpty(name)) {
            name = "xiaomi";
        }
        name = name.toLowerCase();
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.URL, GlobalConstant.KEEP_ALIVE + name);
        bundle.putBoolean(GlobalConstant.NEEDTITLE, true);
        showActivity(WebViewActivity.class, bundle);
    }


}
