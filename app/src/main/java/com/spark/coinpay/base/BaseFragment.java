package com.spark.coinpay.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gyf.barlibrary.ImmersionBar;
import com.spark.coinpay.event.EmptyEvent;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.data.Language;
import com.spark.modulebase.dialog.LoadDialog;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.utils.CheckVolleyErrorUtil;
import com.spark.modulebase.utils.SharedPreferencesUtil;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * 基础Fragment，负责初始化，基础方法
 */

public abstract class BaseFragment extends Fragment implements BaseContract.BaseView {
    protected View rootView;
    protected Activity activity;
    private Unbinder unbinder;
    protected LoadDialog loadDialog;
    private ImmersionBar immersionBar;
    protected ImageView ivBack;
    protected TextView tvTitle;
    protected TextView tvGoto;
    protected LinearLayout llTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getFragmentLayoutId(), null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        initLanguage();
        unbinder = ButterKnife.bind(this, rootView);
        loadDialog = new LoadDialog(activity);
        initView(savedInstanceState);
        initData();
        loadData();
        setListener();
        EventBus.getDefault().register(this);
    }

    /**
     * 界面view
     */
    protected abstract int getFragmentLayoutId();

    /**
     * 初始化所有的控件
     */
    protected void initView(Bundle saveInstance) {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        initRv();
    }

    /**
     * 初始化列表
     */
    protected void initRv() {


    }

    /**
     * 网络数据
     */
    protected void loadData() {
    }

    /**
     * 各控件的点击事件
     *
     * @param v
     */
    protected void setOnClickListener(View v) {

    }


    /**
     * 各控件的点击
     */
    protected void setListener() {

    }

    /**
     * 更新fragemnt数据
     */
    public void update() {

    }

    /**
     * 跳转activity,不关闭当前界面
     *
     * @param cls
     * @param bundle
     */
    protected void showActivity(Class<?> cls, Bundle bundle) {
        showActivity(cls, bundle, -1);
    }

    /**
     * 跳转activity,不关闭当前界面，含跳转回来的的回调
     *
     * @param cls
     * @param bundle
     */
    protected void showActivity(Class<?> cls, Bundle bundle, int requesCode) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        if (requesCode >= 0)
            startActivityForResult(intent, requesCode);
        else
            startActivity(intent);
    }

    /**
     * 初始化语言设置
     */
    private void initLanguage() {
        int code = SharedPreferencesUtil.getInstance(activity).getLanguageCode();
        Language language = Language.values()[code];
        Locale l = new Locale(language.name());
        Resources resources = activity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(l);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(l);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            activity.createConfigurationContext(config);
        }
        Locale.setDefault(l);
        resources.updateConfiguration(config, dm);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    public void showLoading() {
        loadDialog.show();
    }

    @Override
    public void hideLoading() {
        loadDialog.dismiss();
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
    }

    @Override
    public void dealError(VolleyError volleyError) {
        if (volleyError != null) {
            CheckVolleyErrorUtil.checkError(volleyError);
        }
    }

    public abstract String getmTag();

    /**
     * Created by
     * 因为 register event 时必须要在 activity 中声明 onEvent，所以定义了此类
     */
    public void onEvent(EmptyEvent event) {
    }
}
