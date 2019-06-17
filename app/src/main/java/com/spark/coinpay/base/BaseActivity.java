package com.spark.coinpay.base;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gyf.barlibrary.ImmersionBar;
import com.spark.coinpay.R;
import com.spark.coinpay.event.EmptyEvent;
import com.spark.coinpay.main.MainActivity;
import com.spark.coinpay.utils.RestartUtils;
import com.spark.modulebase.base.BaseContract;
import com.spark.modulebase.data.Language;
import com.spark.modulebase.dialog.LoadDialog;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.entity.LoadExceptionEvent;
import com.spark.modulebase.event.CheckLoginEvent;
import com.spark.modulebase.event.LoginoutWithoutApiEvent;
import com.spark.modulebase.utils.ActivityManage;
import com.spark.modulebase.utils.CheckVolleyErrorUtil;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulelogin.CasConstant;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

import static com.spark.coinpay.utils.RestartUtils.START_LAUNCH_ACTION;
import static com.spark.coinpay.utils.RestartUtils.STATUS_FORCE_KILLED;
import static com.spark.coinpay.utils.RestartUtils.STATUS_NORMAL;

/**
 * Activity基类
 * 负责初始化，activity需要执行的方法
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseContract.BaseView {
    private Unbinder unbinder;
    protected Activity activity;
    protected LoadDialog loadDialog;
    private ImmersionBar immersionBar;
    protected ImageView ivBack;
    protected TextView tvTitle;
    protected TextView tvGoto;
    protected LinearLayout llTitle;
    protected ImageView ivMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*switch (RestartUtils.getInstance().getAppStatus()) {
            case STATUS_FORCE_KILLED:
                Log.d("thisttt", "BaseModuledActivity-STATUS_FORCE_KILLED");
                restartApp();
                break;
            case STATUS_NORMAL:
                Log.d("thisttt", "BaseModuledActivity-STATUS_NORMAL");
                break;
            default:
                break;
        }*/
        setContentView(getActivityLayoutId());
        unbinder = ButterKnife.bind(this);
        activity = this;
        initLanguage();
        initBaseView();
        initView(savedInstanceState);
        initData();
        loadData();
        setListener();
        ActivityManage.addActivity(activity);
        EventBus.getDefault().register(this);
    }

    private void restartApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(START_LAUNCH_ACTION, STATUS_FORCE_KILLED);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        ActivityManage.removeActivity(activity);
    }

    /**
     * 设置基础化控件
     */
    private void initBaseView() {
        loadDialog = new LoadDialog(activity);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        if (ivBack != null)
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        tvGoto = findViewById(R.id.tvGoto);
        llTitle = findViewById(R.id.llTitle);
        ivMessage = findViewById(R.id.ivMessage);
        initImmersionBar();
        if (llTitle != null)
            immersionBar.setTitleBar(activity, llTitle);
    }

    /**
     * 设定标题栏的高度
     */
    private void initImmersionBar() {
        if (immersionBar == null) {
            immersionBar = ImmersionBar.with(activity);
            immersionBar.statusBarDarkFont(true);
            immersionBar.keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
        }
    }

    /**
     * 初始化语言设置
     */
    private void initLanguage() {
        int code = SharedPreferencesUtil.getInstance(activity).getLanguageCode();
        Language language = Language.values()[code];
        Locale l = new Locale(language.name());
        Resources resources = getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(l);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(l);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            getApplicationContext().createConfigurationContext(config);
        }
        Locale.setDefault(l);
        resources.updateConfiguration(config, dm);
    }

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
     * 检查输入项
     */
    protected void checkInput() {


    }

    /**
     * 初始化列表
     */
    protected void initRv() {


    }

    /**
     * 界面view
     */
    protected abstract int getActivityLayoutId();

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

    @Override
    public void showLoading() {
        if (!isLiving(activity)) {
            return;
        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (loadDialog != null && !loadDialog.isShowing())
                    loadDialog.show();
            }
        });
    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (loadDialog != null && loadDialog.isShowing())
                    loadDialog.dismiss();
            }
        });
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        if (httpErrorEntity != null) {
            if (httpErrorEntity.getCode() == CasConstant.LOGIN_ERROR) {
                if (StringUtils.isNotEmpty(httpErrorEntity.getUrl())) {
                    LogUtils.e("HttpErrorEntity===" + httpErrorEntity.getCode() + ",httpErrorEntity.getUrl()==" + httpErrorEntity.getUrl());
                    if (httpErrorEntity.getUrl().contains("acceptance")) {
                        EventBus.getDefault().post(new CheckLoginEvent("acceptance"));
                    } else if (httpErrorEntity.getUrl().contains("uc")) {
                        EventBus.getDefault().post(new CheckLoginEvent("uc"));
                    } else if (httpErrorEntity.getUrl().contains("ac")) {
                        EventBus.getDefault().post(new CheckLoginEvent("ac"));
                    } else {
                        LogUtils.e("HttpErrorEntity===" + httpErrorEntity.getCode() + ",new LoadExceptionEvent()==退出登录=======");
                        EventBus.getDefault().post(new LoadExceptionEvent());
                    }
                }
            } else if (StringUtils.isNotEmpty(httpErrorEntity.getMessage())) {
                Message message = new Message();
                message.what = 1;
                message.obj = httpErrorEntity.getMessage();
                mToastHandler.sendMessage(message);
            } else {
                Message message = new Message();
                message.what = 1;
                message.obj = "" + httpErrorEntity.getCode();
                mToastHandler.sendMessage(message);
            }
        } else {
            EventBus.getDefault().post(new LoginoutWithoutApiEvent());
        }
    }

    @Override
    public void dealError(VolleyError volleyError) {
        if (volleyError != null) {
            CheckVolleyErrorUtil.checkError(volleyError);
        }
    }

    /**
     * Created by
     * 因为 register event 时必须要在 activity 中声明 onEvent，所以定义了此类
     */
    public void onEvent(EmptyEvent event) {
    }

    /**
     * 将指定内容粘贴到剪贴板
     *
     * @param content
     */
    public void copyText(String content) {
        if (StringUtils.isNotEmpty(content)) {
            ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newRawUri("copyLable", Uri.parse(content));
            cm.setPrimaryClip(mClipData);
            ToastUtils.showToast(activity, getString(R.string.str_copy_success));
        } else {
            ToastUtils.showToast(activity, getString(R.string.str_copy_fail));
        }

    }

    private static Handler mToastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ToastUtils.showToast(msg.obj.toString());
                    break;
            }
        }
    };

    private static boolean isLiving(Activity activity) {
        if (activity == null) {
            Log.e("wisely", "activity == null");
            return false;
        }

        if (activity.isFinishing()) {
            Log.e("wisely", "activity is finishing");
            return false;
        }
        return true;
    }

}
