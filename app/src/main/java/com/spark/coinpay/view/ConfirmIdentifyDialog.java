package com.spark.coinpay.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.spark.coinpay.R;

/**
 * 承兑商认证
 */
public class ConfirmIdentifyDialog extends Dialog {
    private Context context;
    private TextView titleView;
    private TextView contentSelfView;
    private TextView contentAssetsView;
    private TextView contentFrozenView;
    private TextView leftView;
    private TextView rightView;

    public ConfirmIdentifyDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        setDialogTheme();
        initView();
    }

    public void setContent(String content) {
        if (contentFrozenView != null) {
            contentFrozenView.setText(content + context.getString(R.string.str_frozen));
        }
    }

    /**
     * set dialog theme(设置对话框主题)
     */
    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        int heightMax = context.getResources().getDisplayMetrics().heightPixels;
        int widthMax = context.getResources().getDisplayMetrics().widthPixels;
        lp.width = (int) (widthMax * 0.9);
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    /**
     * 初始化view
     */
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_confirm_identify, null);
        setContentView(view);
        titleView = view.findViewById(R.id.tvDialogTitle);
        contentSelfView = view.findViewById(R.id.tvContentSelf);
        contentAssetsView = view.findViewById(R.id.tvContentAssets);
        contentFrozenView = view.findViewById(R.id.tvContentFrozen);
        leftView = view.findViewById(R.id.tvLeft);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        rightView = view.findViewById(R.id.tvRight);
    }

    /**
     * 右侧按钮点击
     *
     * @param onClickListener
     */
    public void setOnPositiveClickLister(View.OnClickListener onClickListener) {
        if (rightView != null)
            rightView.setOnClickListener(onClickListener);
    }

}
