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
 * 仿ios提示框
 */

public class ConfirmIosDialog extends Dialog {
    private Context context;
    private TextView titleView;
    private TextView contentView;
    private TextView leftView;
    private TextView rightView;
    private float widthScale;
    private float heightScale;

    public ConfirmIosDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        setDialogTheme();
        initView();
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
        if (widthScale > 0)
            lp.width = (int) (widthMax * widthScale);
        if (heightScale > 0)
            lp.height = (int) (heightMax * heightScale);
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    /**
     * 初始化view
     */
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_confirm, null);
        setContentView(view);
        titleView = view.findViewById(R.id.tvDialogTitle);
        contentView = view.findViewById(R.id.tvContent);
        leftView = view.findViewById(R.id.tvLeft);
        rightView = view.findViewById(R.id.tvRight);
    }

    /**
     * 设置宽度
     *
     * @param widthScale
     * @return
     */
    public ConfirmIosDialog withWidthScale(float widthScale) {
        this.widthScale = widthScale;
        return this;
    }

    /**
     * 设置高度
     *
     * @param heightScale
     * @return
     */
    public ConfirmIosDialog heightScale(float heightScale) {
        this.heightScale = heightScale;
        return this;
    }


    /**
     * 设置标题
     *
     * @param title
     */
    public ConfirmIosDialog setTitle(String title) {
        if (titleView != null)
            titleView.setText(title);
        return this;
    }

    /**
     * 设置标题颜色
     *
     * @param titleColor
     */
    public ConfirmIosDialog setTitleColor(int titleColor) {
        if (titleView != null && titleColor != -1)
            titleView.setTextColor(titleColor);
        return this;
    }

    /**
     * 设置内容
     *
     * @param title
     */
    public ConfirmIosDialog setContent(String content) {
        if (contentView != null)
            contentView.setText(content);
        return this;
    }

    /**
     * 设置内容颜色
     *
     * @param titleColor
     */
    public ConfirmIosDialog setContentColor(int contentColor) {
        if (contentView != null && contentColor != -1)
            contentView.setTextColor(contentColor);
        return this;
    }

    /**
     * 设置左侧按钮内容
     *
     * @param title
     */
    public ConfirmIosDialog setLeftContent(String leftTxt) {
        if (leftView != null)
            leftView.setText(leftTxt);
        return this;
    }

    /**
     * 设置左侧按钮内容颜色
     *
     * @param titleColor
     */
    public ConfirmIosDialog setLeftContentColor(int leftContentColor) {
        if (leftView != null && leftContentColor != -1)
            leftView.setTextColor(leftContentColor);
        return this;
    }

    /**
     * 设置右侧按钮内容
     *
     * @param title
     */
    public ConfirmIosDialog setRightContent(String rightTxt) {
        if (rightView != null)
            rightView.setText(rightTxt);
        return this;
    }

    /**
     * 设置右侧按钮内容颜色
     *
     * @param titleColor
     */
    public ConfirmIosDialog setRightContentColor(int rightContentColor) {
        if (rightView != null && rightContentColor != -1)
            rightView.setTextColor(rightContentColor);
        return this;
    }


    /**
     * 左侧按钮点击
     *
     * @param onClickListener
     */
    public ConfirmIosDialog setLeftClickLister(View.OnClickListener onClickListener) {
        if (leftView != null)
            leftView.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 右侧按钮点击
     *
     * @param onClickListener
     */
    public ConfirmIosDialog setRightClickLister(View.OnClickListener onClickListener) {
        if (rightView != null)
            rightView.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 获取内容
     */
    public String getContent() {
        if (contentView != null) {
            return contentView.getText().toString().trim();
        }
        return "";
    }

}
