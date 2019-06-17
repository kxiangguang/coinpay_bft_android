package com.spark.coinpay.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spark.coinpay.R;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;

/**
 * 付款二维码
 */
public class GetImgDialog extends Dialog {
    private Context context;
    private ImageView ivClose;
    private ImageView ivCode;
    private EditText etUsername;
    private TextView tvLogin;

    public GetImgDialog(@NonNull Context context) {
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
        lp.width = (int) (widthMax * 0.8);
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    /**
     * 初始化view
     */
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_get_img, null);
        setContentView(view);
        ivClose = view.findViewById(R.id.ivClose);
        ivCode = view.findViewById(R.id.ivCode);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        etUsername = view.findViewById(R.id.etUsername);
        tvLogin = view.findViewById(R.id.tvLogin);

    }

    public void setBitmap(Bitmap bitmap) {
        ivCode.setImageBitmap(bitmap);
    }

    public void setClickListener(final ClickLister listener) {

        if (tvLogin != null) {
            tvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        String imgCode = StringUtils.getText(etUsername);
                        if (StringUtils.isEmpty(imgCode)) {
                            ToastUtils.showToast("请输入图片验证码");
                        } else {
                            listener.onConfirm(imgCode);
                        }
                    }
                }
            });
        }
        if (ivCode != null) {
            ivCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.reGetImg();
                    }
                }
            });
        }

    }

    public interface ClickLister {

        void onConfirm(String imgCode);

        void reGetImg();
    }

}
