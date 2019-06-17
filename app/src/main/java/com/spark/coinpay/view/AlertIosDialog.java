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
import android.widget.ImageView;
import android.widget.TextView;

import com.spark.coinpay.R;

/**
 * 仿ios警示框
 */

public class AlertIosDialog extends Dialog {
    private Context context;
    private ImageView imageView;
    private TextView contentView;
    private TextView tagView;
    private TextView sureView;
    private float widthScale;
    private float heightScale;

    public AlertIosDialog(@NonNull Context context) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_alert, null);
        setContentView(view);
        imageView = view.findViewById(R.id.ivAlert);
        tagView = view.findViewById(R.id.tvTag);
        sureView = view.findViewById(R.id.tvSure);
        contentView = view.findViewById(R.id.tvContent);
    }

    /**
     * 设置宽度
     *
     * @param widthScale
     * @return
     */
    public AlertIosDialog withWidthScale(float widthScale) {
        this.widthScale = widthScale;
        return this;
    }

    /**
     * 设置高度
     *
     * @param heightScale
     * @return
     */
    public AlertIosDialog heightScale(float heightScale) {
        this.heightScale = heightScale;
        return this;
    }


    /**
     * 设置图标
     *
     * @param resId
     */
    public AlertIosDialog setImg(int resId) {
        if (imageView != null && resId != -1) {
            if (resId != 0) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(resId);
            }
        }
        return this;
    }

    /**
     * 设置标签
     *
     * @param title
     */
    public AlertIosDialog setTag(String tag) {
        if (tagView != null) {
            tagView.setVisibility(View.VISIBLE);
            tagView.setText(tag);
        }
        return this;
    }

    /**
     * 设置标签颜色
     *
     * @param titleColor
     */
    public AlertIosDialog setTagColor(int tagColor) {
        if (tagView != null && tagColor != -1)
            tagView.setTextColor(tagColor);
        return this;
    }

    /**
     * 设置内容
     *
     * @param title
     */
    public AlertIosDialog setContent(String content) {
        if (contentView != null) {
            contentView.setVisibility(View.VISIBLE);
            contentView.setText(content);
        }
        return this;
    }

    /**
     * 设置内容颜色
     *
     * @param titleColor
     */
    public AlertIosDialog setContentColor(int contentColor) {
        if (contentView != null && contentColor != -1)
            contentView.setTextColor(contentColor);
        return this;
    }


    /**
     * 左侧按钮点击
     *
     * @param onClickListener
     */
    public AlertIosDialog setPositiveClickLister(View.OnClickListener onClickListener) {
        if (sureView != null)
            sureView.setOnClickListener(onClickListener);
        return this;
    }

}
