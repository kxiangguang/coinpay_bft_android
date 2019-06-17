package com.spark.coinpay;

import com.spark.coinpay.base.BaseActivity;
import com.spark.moduleassets.AcUrls;
import com.spark.modulebase.BaseApplication;
import com.spark.coinpay.GlobalConstant;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulelogin.LoginUrls;
import com.spark.moduleotc.OtcUrls;
import com.spark.moduleuc.UcUrls;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 应用启动Application
 */
public class MyApplication extends BaseApplication {
    private LoginStatus loginStatus;
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        initBugly();
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    public LoginStatus getLoginStatus() {
        return loginStatus == null ? new LoginStatus() : loginStatus;
    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }

    /**
     * 重新登录
     */
    public void loginAgain(BaseActivity activity) {
        deleteCurrentUser();
    }


    /**
     * 初始化腾讯bug管理平台
     */
    private void initBugly() {
        /* Bugly SDK初始化
        * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        * 注意：如果您之前使用过Bugly SDK，请将以下这句注释掉。
        */
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(AppUtils.getVersionName(this));
        strategy.setAppPackageName(AppUtils.getAppPackageName(this));
        strategy.setAppReportDelay(5000);//Bugly会在启动5s后联网同步数据

        /*  第三个参数为SDK调试模式开关，调试模式的行为特性如下：
            输出详细的Bugly SDK的Log；
            每一条Crash都会被立即上报；
            自定义日志将会在Logcat中输出。
            建议在测试阶段建议设置成true，发布时设置为false。*/

        CrashReport.initCrashReport(getApplicationContext(), "5a3f3706ac", false, strategy);

    }


}
