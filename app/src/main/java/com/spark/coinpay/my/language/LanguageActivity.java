package com.spark.coinpay.my.language;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.modulebase.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class LanguageActivity extends BaseActivity {

    @BindView(R.id.ivChinese)
    ImageView ivChinese;
    @BindView(R.id.llChinese)
    LinearLayout llChinese;
    @BindView(R.id.ivEnglish)
    ImageView ivEnglish;
    @BindView(R.id.llEnglish)
    LinearLayout llEnglish;

    private int languageCode = 1;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_my_set_language));
    }


    @OnClick({R.id.llChinese, R.id.llEnglish})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llChinese:
                if (languageCode != 0) language(0);
                break;
            case R.id.llEnglish:
                if (languageCode != 1) language(1);
                break;

        }

    }

    private void language(int languageCode) {
        SharedPreferencesUtil.getInstance(activity).setLanguageCode(activity, languageCode);
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }


    @Override
    protected void initData() {
        super.initData();
        languageCode = SharedPreferencesUtil.getInstance(activity).getLanguageCode();
        if (languageCode == 0) ivChinese.setVisibility(View.VISIBLE);
        else if (languageCode == 1) ivEnglish.setVisibility(View.VISIBLE);
    }

}
