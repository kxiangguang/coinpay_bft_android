package com.spark.modulebase.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.R;


public class TimeCount extends CountDownTimer {
    private TextView codeView;
    private Context context;

    public TimeCount(long millisInFuture, long countDownInterval, TextView codeView) {
        super(millisInFuture, countDownInterval);
        this.codeView = codeView;
        context = BaseApplication.getAppContext();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        codeView.setEnabled(false);
        codeView.setText(millisUntilFinished / 1000 + context.getResources().getString(R.string.str_resend_code));
        codeView.setTextColor(context.getResources().getColor(R.color.font_grey_a5a5a5));
    }

    @Override
    public void onFinish() {
        codeView.setText(context.getResources().getString(R.string.str_send_code));
        codeView.setEnabled(true);
        codeView.setTextColor(context.getResources().getColor(R.color.base_bg_btn_normal));
    }
}