package com.spark.modulebase.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.modulebase.R;

/**
 * 通用的头部控件
 */

public class CustomHeadView extends LinearLayout {
    private ImageView leftImg;
    private TextView leftTv;
    private TextView titleTv;
    private ImageView rightImg;
    private TextView rightTv;
    private RelativeLayout barRlyt;

    public CustomHeadView(Context context) {
        this(context, null);
    }

    public CustomHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    public void initView(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomHeadView);
        /**-------------获取左边按钮属性------------*/
        boolean isLeftImgVisible = typedArray.getBoolean(R.styleable.CustomHeadView_left_img_visible, false);
        int leftResId = typedArray.getResourceId(R.styleable.CustomHeadView_left_img_src, -1);
        int leftImgBack = typedArray.getResourceId(R.styleable.CustomHeadView_left_img_background, -1);
        /**-------------获取左边文本属性------------*/
        boolean isLeftTvVisible = typedArray.getBoolean(R.styleable.CustomHeadView_left_tv_visible, false);
        String leftTvText = "";
        if (typedArray.hasValue(R.styleable.CustomHeadView_left_tv_text)) {
            leftTvText = typedArray.getString(R.styleable.CustomHeadView_left_tv_text);
        }
        int leftTvColor = typedArray.getResourceId(R.styleable.CustomHeadView_left_tv_text_color, -1);
        int leftTvBack = typedArray.getResourceId(R.styleable.CustomHeadView_left_tv_background, -1);
        /**-------------获取右边按钮属性------------*/
        boolean isRightImgVisible = typedArray.getBoolean(R.styleable.CustomHeadView_right_img_visible, false);
        int rightResId = typedArray.getResourceId(R.styleable.CustomHeadView_right_img_src, -1);
        int rightImgBack = typedArray.getResourceId(R.styleable.CustomHeadView_right_img_background, -1);
        /**-------------获取右边文本属性------------*/
        boolean isRightTvVisible = typedArray.getBoolean(R.styleable.CustomHeadView_right_tv_visible, false);
        String rightTvText = "";
        if (typedArray.hasValue(R.styleable.CustomHeadView_right_tv_text)) {
            rightTvText = typedArray.getString(R.styleable.CustomHeadView_right_tv_text);
        }
        int rightTvColor = typedArray.getResourceId(R.styleable.CustomHeadView_right_tv_text_color, -1);
        int rightTvBack = typedArray.getResourceId(R.styleable.CustomHeadView_right_tv_background, -1);
        /**-------------获取标题属性------------*/
        boolean isTitleVisible = typedArray.getBoolean(R.styleable.CustomHeadView_title_visible, false);
        String titleText = "";
        if (typedArray.hasValue(R.styleable.CustomHeadView_title_text)) {
            titleText = typedArray.getString(R.styleable.CustomHeadView_title_text);
        }
        int titleColor = typedArray.getResourceId(R.styleable.CustomHeadView_title_text_color, -1);
        /**-------------背景颜色------------*/
        int backgroundResId = typedArray.getResourceId(R.styleable.CustomHeadView_barBackground, -1);

        typedArray.recycle();

        /**-------------设置内容------------*/
        View barLayoutView = View.inflate(getContext(), R.layout.view_head_custom, null);
        leftImg = barLayoutView.findViewById(R.id.ivLeft);
        leftTv = barLayoutView.findViewById(R.id.tvLeft);
        titleTv = barLayoutView.findViewById(R.id.tvHeadTitle);
        rightImg = barLayoutView.findViewById(R.id.ivRight);
        rightTv = barLayoutView.findViewById(R.id.tvRight);
        barRlyt = barLayoutView.findViewById(R.id.rlHead);

        setLeftImgVisible(isLeftImgVisible);
        setLeftResId(leftResId);
        setLeftImgBack(leftImgBack);

        setLeftTvVisible(isLeftTvVisible);
        setLeftTvText(leftTvText);
        setLeftTvTextColor(leftTvColor);
        setLeftTvBack(leftTvBack);

        setTitleVisible(isTitleVisible);
        setTitleText(titleText);
        setTitleTextColor(titleColor);

        setBackgroundResId(backgroundResId);

        setRightImgVisible(isRightImgVisible);
        setRightResId(rightResId);
        serRightImgBack(rightImgBack);

        setRightTvVisible(isRightTvVisible);
        setRightTvText(rightTvText);
        setRightTvTextColor(rightTvColor);
        setRightTvTextBack(rightTvBack);

        addView(barLayoutView, 0);
    }


    /**
     * 设置左边图片背景
     *
     * @param leftImgBack
     */
    public void setLeftImgBack(int leftImgBack) {
        if (leftImgBack != -1) {
            leftImg.setBackgroundResource(leftImgBack);
        }
    }

    /**
     * 设置左边图片是否可见
     *
     * @param leftImgVisible
     */
    public void setLeftImgVisible(Boolean leftImgVisible) {
        if (leftImgVisible) {
            leftImg.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置左边资源图片
     *
     * @param leftResId
     */
    public void setLeftResId(int leftResId) {
        if (leftResId != -1) {
            leftImg.setImageResource(leftResId);
        }
    }

    /**
     * 左边按钮监听
     *
     * @param clickListener
     */
    public void setLeftImgListener(OnClickListener clickListener) {
        leftImg.setOnClickListener(clickListener);
    }

    /**
     * 设置左边文字是否可见
     *
     * @param leftTvVisible
     */
    public void setLeftTvVisible(Boolean leftTvVisible) {
        if (leftTvVisible) {
            leftTv.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置左边文字内容
     *
     * @param leftTvText
     */
    public void setLeftTvText(String leftTvText) {
        leftTv.setText(leftTvText);
    }

    /**
     * 设置左边文字颜色
     *
     * @param leftTvColor
     */
    public void setLeftTvTextColor(int leftTvColor) {
        if (leftTvColor != -1) {
            leftTv.setTextColor(leftTvColor);
        }
    }

    /**
     * 设置左边文字背景
     *
     * @param leftTvBack
     */
    public void setLeftTvBack(int leftTvBack) {
        if (leftTvBack != -1) {
            leftTv.setBackgroundResource(leftTvBack);
        }
    }

    /**
     * 左边文字监听
     *
     * @param clickListener
     */
    public void setLeftTvListener(OnClickListener clickListener) {
        leftTv.setOnClickListener(clickListener);
    }

    /**
     * 设置右边图片是否可见
     *
     * @param rightImgVisible
     */
    public void setRightImgVisible(Boolean rightImgVisible) {
        if (rightImgVisible) {
            rightImg.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置右边图片资源
     *
     * @param rightResId
     */
    public void setRightResId(int rightResId) {
        if (rightResId != -1) {
            rightImg.setImageResource(rightResId);
        }
    }

    /**
     * 右边图片背景
     *
     * @param rightImgBack
     */
    public void serRightImgBack(int rightImgBack) {
        if (rightImgBack != -1) {
            rightImg.setBackgroundResource(rightImgBack);
        }
    }

    /**
     * 右边按钮监听
     *
     * @param clickListener
     */
    public void setRightImgListener(OnClickListener clickListener) {
        rightImg.setOnClickListener(clickListener);
    }

    /**
     * 设置右边文字是否可见
     *
     * @param rightTvVisible
     */
    public void setRightTvVisible(Boolean rightTvVisible) {
        if (rightTvVisible) {
            rightTv.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置右边文字内容
     *
     * @param rightTvText
     */
    public void setRightTvText(String rightTvText) {
        rightTv.setText(rightTvText);
    }

    /**
     * 设置右边文字背景
     *
     * @param rightTvBack
     */
    public void setRightTvTextBack(int rightTvBack) {
        if (rightTvBack != -1) {
            rightTv.setBackgroundResource(rightTvBack);
        }
    }

    /**
     * 设置右边文字颜色
     *
     * @param rightTvColor
     */
    public void setRightTvTextColor(int rightTvColor) {
        if (rightTvColor != -1) {
            rightTv.setTextColor(rightTvColor);
        }
    }

    /**
     * 右边文字
     *
     * @param clickListener
     */
    public void setRightTvListener(OnClickListener clickListener) {
        rightTv.setOnClickListener(clickListener);
    }

    /**
     * 设置标题是否可见
     *
     * @param titleVisible
     */
    public void setTitleVisible(Boolean titleVisible) {
        if (titleVisible) {
            titleTv.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置标题文字
     *
     * @param titleText
     */
    public void setTitleText(String titleText) {
        titleTv.setVisibility(VISIBLE);
        titleTv.setText(titleText);
    }

    /**
     * 设置标题颜色
     *
     * @param titleColor
     */
    public void setTitleTextColor(int titleColor) {
        if (titleColor != -1) {
            titleTv.setTextColor(titleColor);
        }
    }

    /**
     * 设置图片背景资源
     *
     * @param backgroundResId
     */
    public void setBackgroundResId(int backgroundResId) {
        if (backgroundResId != -1) {
            barRlyt.setBackgroundResource(backgroundResId);
        }
    }


}
