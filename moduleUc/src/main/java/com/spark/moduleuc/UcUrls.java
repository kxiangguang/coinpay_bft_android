package com.spark.moduleuc;

import com.spark.modulebase.utils.AppDataSetUtils;

/**
 * 账户设置
 */

public class UcUrls {
    private String host = "";
    private static UcUrls ucUrls;

    public static UcUrls getInstance() {
        if (ucUrls == null) {
            ucUrls = new UcUrls();
        }
        return ucUrls;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    /**
     * 获取账户设置参数
     *
     * @return
     */
    public String getSafeSettingUrl() {
        return host + "/ac/trade/security/setting";
    }

    /**
     * 上传图片
     *
     * @return
     */
    public String getUploadPicUrl() {
        return host + "/uc/upload/oss/base64";
    }

    /**
     * 上传头像
     *
     * @return
     */
    public String getAvatarUrl() {
        return host + "/member/update/avatar";
    }

    /**
     * 绑定手机号
     *
     * @return
     */
    public String getBindPhoneUrl() {
        return host + "/uc/bind/phone";
    }

    /**
     * 获取当前绑定手机的验证码
     *
     * @return
     */
    public String getSendCodeAfterLoginUrl() {
        return host + "/uc/mobile/auth/code";
    }

    /**
     * 修改手机号
     *
     * @return
     */
    public String getChangePhoneUrl() {
        return host + "/uc/info/change/phone";
    }

    /**
     * 获取APP版本更新信息
     *
     * @return
     */
    public String getNewVision() {
        return host + "/uc/ancillary/system/app/version/0";
    }

    public String getImg() {
        return host + "/captcha/mm/img";
    }
}
