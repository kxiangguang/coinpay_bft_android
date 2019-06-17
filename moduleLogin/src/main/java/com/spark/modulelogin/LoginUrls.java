package com.spark.modulelogin;

import com.spark.coinpay.GlobalConstant;

/**
 * 登录模块的url
 */

public class LoginUrls {
    private String host = "";
    private String hostBusiness = "";
    private String hostLogin = "";
    private static LoginUrls loginUrls;

    public static LoginUrls getInstance() {
        if (loginUrls == null) {
            loginUrls = new LoginUrls();
        }
        return loginUrls;
    }

    /**
     * cas登录
     *
     * @param hostLogin
     */
    public void setHostLogin(String hostLogin) {
        this.hostLogin = hostLogin;
    }

    /**
     * 业务模块
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 业务系统
     *
     * @param hostBusiness
     */
    public void setHostBusiness(String hostBusiness) {
        this.hostBusiness = hostBusiness;
    }

    public String getHost() {
        return host;
    }

    /**
     * 登录cas
     *
     * @return
     */
    public String getCasLogin() {
        return hostLogin + "/cas/v1/tickets";
    }

    /**
     * 登录业务系统
     */
    public String doBussionLogin(String bussineType) {
        return getBusinessUrl(bussineType) + "/cas";
    }

    /**
     * 获取service
     */
    public String getService(String bussineType) {
        return getBusinessUrl(bussineType) + "/cas?client_name=CasClient";
    }

    /**
     * check是否登录
     */
    public String doCheck(String bussineType) {
        return getBusinessUrl(bussineType) + "/check";
    }

    private String getBusinessUrl(String bussineType) {
        String url = "";
        switch (bussineType) {
            case GlobalConstant.TYPE_AC:
                url = GlobalConstant.getHostAC();
                break;
            case GlobalConstant.TYPE_UC:
                url = GlobalConstant.getHostUC();
                break;
            case GlobalConstant.TYPE_OTC:
                url = GlobalConstant.getHostOTC();
                break;
        }
        return url;
    }

    /**
     * 获取验证码
     */
    public String getCodeUrl() {
        return host + "/uc/mobile/code";
    }


    /**
     * 获取手机验证
     */
    public String getSendVertifyCodeUrl() {
        return hostLogin + "/cas/captcha/permission";
    }

    /**
     * 验证短信验证
     */
    public String getVertifyCodeUrl() {
        return hostLogin + "/cas/captcha/check";
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public String getUserInfo() {
        return host + "/uc/sign/getUserInfo";
    }

    /**
     * 获取邮箱验证码
     *
     * @return
     */
    public String getEmailCodeUrl() {
        return host + "/uc/register/email/code";
    }

    /**
     * 极验验证
     *
     * @return
     */
    public String getCaptchaUrl() {
        return host + "/uc/gee/captcha";
    }

    /**
     * 获取国家列表
     *
     * @return
     */
    public String getCountryUrl() {
        return host + "/uc/register/support/getCountry";
    }

    /**
     * 邮箱注册
     *
     * @return
     */
    public String getSignUpByEmail() {
        return host + "/uc/register/email";
    }

    /**
     * 手机号注册
     *
     * @return
     */
    public String getSignUpByPhone() {
        return host + "/uc/register/phone";
    }

    /**
     * 忘记密码
     *
     * @return
     */
    public String getForgotPwdUrl() {
        return host + "/uc/info/reset/login/password";
    }

}
