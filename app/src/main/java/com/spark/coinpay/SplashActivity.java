package com.spark.coinpay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.base.GuideActivity;
import com.spark.coinpay.login.LoginActivity;
import com.spark.coinpay.main.MainActivity;
import com.spark.coinpay.utils.RestartUtils;
import com.spark.moduleassets.AcUrls;
import com.spark.modulebase.entity.User;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulelogin.LoginUrls;
import com.spark.moduleotc.OtcUrls;
import com.spark.moduleuc.AgentUrls;
import com.spark.moduleuc.UcUrls;
import com.spark.netty_library.GuardService;
import com.spark.netty_library.WebSocketService;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

import static com.spark.coinpay.utils.RestartUtils.STATUS_NORMAL;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_API_ADDRESS;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_APP_NAME;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_FIRST_USE;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API_AC;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API_ACP;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_WRITE_API_UC;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_clearCode310;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.iv_pic)
    ImageView ivPic;

    private Timer timer;
    int n = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //RestartUtils.getInstance().setAppStatus(STATUS_NORMAL);
        super.onCreate(savedInstanceState);
        isRoot();
        ImmersionBar.with(this).fullScreen(true).init();
        timerStart();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        //启动页变形优化
        AppUtils.scaleImage(this, ivPic, R.mipmap.splash);
    }

    private void timerStart() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (n == 0) {
                    timer.cancel();
                    timer = null;
//                    boolean isFirst = !SharedPreferencesUtil.getInstance(activity).contains(SP_KEY_FIRST_USE);
//                    if (isFirst) {
//                        showActivity(GuideActivity.class, null);
//                    } else {
                    User user = MyApplication.getAppContext().getCurrentUser();
                    if (user.isLogin()) {

                        String clearCode310 = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_clearCode310);

                        if (StringUtils.isEmpty(clearCode310)) {
                            MyApplication.getAppContext().deleteCurrentUser();
                            MyApplication.getAppContext().getCookieManager().getCookieStore().removeAll();
                            showActivity(LoginActivity.class, null);
                        } else {
                            boolean isTest = SharedPreferencesUtil.getInstance(activity).getBooleanParam(SP_KEY_WRITE_API);
                            if (!isTest) {
                                String apiAddress = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_API_ADDRESS);
                                if (StringUtils.isEmpty(apiAddress)) {
                                    //ToastUtils.showToast("请先设置API地址");
                                    return;
                                }

                                com.spark.coinpay.GlobalConstant.isTest = false;
                                GuardService.isTest = false;
                                WebSocketService.isTest = false;

                                com.spark.coinpay.GlobalConstant.CUR_HOST = apiAddress;
                                WebSocketService.setHost(apiAddress);
                                GuardService.setHost(apiAddress);

                                LoginUrls.getInstance().setHost(com.spark.coinpay.GlobalConstant.getHost() + "/" + com.spark.coinpay.GlobalConstant.TYPE_UC);
                                LoginUrls.getInstance().setHostBusiness(com.spark.coinpay.GlobalConstant.getHost());
                                LoginUrls.getInstance().setHostLogin(com.spark.coinpay.GlobalConstant.getHostLogin());
                                AcUrls.getInstance().setHost(com.spark.coinpay.GlobalConstant.getHostAC());
                                UcUrls.getInstance().setHost(com.spark.coinpay.GlobalConstant.getHostUC());
                                OtcUrls.getInstance().setHost(com.spark.coinpay.GlobalConstant.getHostOTC());
                                AgentUrls.getInstance().setHost(com.spark.coinpay.GlobalConstant.getHostAgent());
                            } else {
                                String uc = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_WRITE_API_UC);
                                String ac = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_WRITE_API_AC);
                                String acp = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_WRITE_API_ACP);

                                com.spark.coinpay.GlobalConstant.isTest = true;
                                GuardService.isTest = true;
                                WebSocketService.isTest = true;

                                com.spark.coinpay.GlobalConstant.CUR_HOST_UC = uc;
                                com.spark.coinpay.GlobalConstant.CUR_HOST_AC = ac;
                                com.spark.coinpay.GlobalConstant.CUR_HOST_ACP = acp;
                                WebSocketService.setHost(acp);
                                GuardService.setHost(acp);

                                LoginUrls.getInstance().setHost(com.spark.coinpay.GlobalConstant.getHost() + "/" + com.spark.coinpay.GlobalConstant.TYPE_UC);
                                LoginUrls.getInstance().setHostBusiness(com.spark.coinpay.GlobalConstant.getHost());
                                LoginUrls.getInstance().setHostLogin(com.spark.coinpay.GlobalConstant.getHostLogin());
                                AcUrls.getInstance().setHost(com.spark.coinpay.GlobalConstant.getHostAC());
                                UcUrls.getInstance().setHost(com.spark.coinpay.GlobalConstant.getHostUC());
                                OtcUrls.getInstance().setHost(com.spark.coinpay.GlobalConstant.getHostOTC());
                                AgentUrls.getInstance().setHost(com.spark.coinpay.GlobalConstant.getHostAgent());
                            }
                            showActivity(MainActivity.class, null);
                        }
                    } else {
                        showActivity(LoginActivity.class, null);
                    }
//                    }
                    finish();
                }
                n--;
            }
        }, 100, 800);
    }


    @Override
    public void onBackPressed() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onBackPressed();
    }

    /**
     * 修改每次点击桌面图标都会重新启动应用的Bug
     */
    private void isRoot() {
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                }
            }
        }
    }
}
