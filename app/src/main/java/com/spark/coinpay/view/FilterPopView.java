package com.spark.coinpay.view;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.coinpay.R;
import com.spark.modulebase.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;


public class FilterPopView extends PopupWindow implements View.OnClickListener {

    private RadioGroup rgGroup;
    private RadioButton rbAll;
    private RadioButton rbRu;
    private RadioButton rbChu;

    private TextView tvReset;
    private TextView tvFinish;
    private View view_blank;

    private Context context;
    private FilterCallBack filterCallBack;

    private int intType = 0;

    public FilterPopView(Context context, FilterCallBack filterCallBack, int width, int height) {
        super(context);
        this.context = context;
        this.filterCallBack = filterCallBack;
        setFocusable(true);

        View contentView = LayoutInflater.from(context).inflate(R.layout.view_popwindow_my_order, null);
        setContentView(contentView);
        initView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setTouchable(true);
        setAnimationStyle(R.style.AnimTools);

        setListener();
    }

    private void initView(View mainView) {
        rgGroup = mainView.findViewById(R.id.rgGroup);
        rbAll = mainView.findViewById(R.id.rbAll);
        rbRu = mainView.findViewById(R.id.rbRu);
        rbChu = mainView.findViewById(R.id.rbChu);

        tvReset = mainView.findViewById(R.id.tvReset);
        tvFinish = mainView.findViewById(R.id.tvFinish);
        view_blank = mainView.findViewById(R.id.view_blank);
    }

    private void setListener() {
        tvReset.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        view_blank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                intType = group.indexOfChild(group.findViewById(checkedId));
            }

        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReset:
                intType = 0;
                rbAll.setChecked(true);
                break;
            case R.id.tvFinish:
                filterCallBack.callBack(intType);
                dismiss();
                break;
        }

    }

    public interface FilterCallBack {
        void callBack(int type);
    }

}
