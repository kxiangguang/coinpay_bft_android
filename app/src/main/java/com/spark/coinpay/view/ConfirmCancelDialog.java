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
import android.widget.EditText;
import android.widget.TextView;

import com.spark.coinpay.R;
import com.spark.modulebase.widget.Keyboard;
import com.spark.modulebase.widget.PayEditText;

/**
 * 确认取消
 */

public class ConfirmCancelDialog extends Dialog {
    private Context context;
    private TextView leftView;
    private TextView rightView;
    private PayEditText payEditText;
    private Keyboard keyboard;

    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "<<", "0", ""
    };

    public PayEditText getPwdEditText() {
        return payEditText;
    }

    public ConfirmCancelDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        setDialogTheme();
        initView();
        setSubView();
        initEvent();
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
        lp.width = (int) (widthMax * 1);//宽度比例0~1
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
    }

    /**
     * 初始化view
     */
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_confirm_cancel, null);
        setContentView(view);
        payEditText = view.findViewById(R.id.etPwd);
        leftView = view.findViewById(R.id.tvLeft);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        rightView = view.findViewById(R.id.tvRight);
        keyboard = view.findViewById(R.id.KeyboardView_pay);
    }

    public void setPositiveOnclickListener(View.OnClickListener onclickListener) {
        rightView.setOnClickListener(onclickListener);
    }

    private void setSubView() {
        //设置键盘
        keyboard.setKeyboardKeys(KEY);
    }

    private void initEvent() {
        keyboard.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
            @Override
            public void onKeyClick(int position, String value) {
                if (position < 11 && position != 9) {
                    payEditText.add(value);
                } else if (position == 9) {
                    payEditText.remove();
                } else if (position == 11) {
                    //当点击完成的时候，也可以通过payEditText.getText()获取密码，此时不应该注册OnInputFinishedListener接口
                    //Toast.makeText(context, "您的密码是：" + payEditText.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 当密码输入完成时的回调
         */
        payEditText.setOnInputFinishedListener(new PayEditText.OnInputFinishedListener() {
            @Override
            public void onInputFinished(String password) {
                //Toast.makeText(context, "您的密码是：" + password, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
