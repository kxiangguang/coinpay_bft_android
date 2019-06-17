package com.spark.modulebase.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.spark.modulebase.R;
import com.spark.modulebase.utils.StringUtils;

import java.util.Locale;


/**
 * 加载框
 */
public class LoadDialog extends Dialog {
    private TextView tvMsg;

    public LoadDialog(@NonNull Context context) {
        super(context, R.style.customDialog);
        initView();
        initSettings();
    }

    private void initView() {
        setContentView(R.layout.pop_loading);
        tvMsg = findViewById(R.id.tvMsg);
    }

    private void initSettings() {
        setCanceledOnTouchOutside(false);
    }

}

