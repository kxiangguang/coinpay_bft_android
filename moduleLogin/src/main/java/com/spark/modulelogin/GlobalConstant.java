package com.spark.coinpay;

/**
 * 配置文件
 */
public class GlobalConstant {

    /**
     * 是否是测试环境:false线上环境/true测试环境
     */
    public static boolean isTest = false;

    public static String CUR_HOST = "1688up.net";

    public static String CUR_HOST_UC = "";
    public static String CUR_HOST_AC = "";
    public static String CUR_HOST_ACP = "";

    public static String getHost() {
        return isTest ? "http://192.168.2.245" : "http://api.bitaccept." + CUR_HOST;
    }

    public static String getHostLogin() {
        return isTest ? "http://192.168.2.245:8446" : "http://cas.bitaccept." + CUR_HOST;
    }

    public static String getHostUC() {
        return isTest ? "http://" + CUR_HOST_UC + ":48888/uc" : "http://api.bitaccept." + CUR_HOST + "/uc";
    }

    public static String getHostAC() {
        return isTest ? "http://" + CUR_HOST_AC + ":48881/ac" : "http://api.bitaccept." + CUR_HOST + "/ac";
    }

    public static String getHostOTC() {
        return isTest ? "http://" + CUR_HOST_ACP + ":38081/acceptance" : "http://api.bitaccept." + CUR_HOST + "/acceptance";
    }

    public static String getHostAgent() {
        return isTest ? "http://192.168.2.183:48891/agentApi" : "http://api.agent.bitaccept." + CUR_HOST + "/agentApi";
    }

    /**
     * AC,UC,OTC模块地址后缀
     */
    public static final String TYPE_AC = "ac";
    public static final String TYPE_UC = "uc";
    public static final String TYPE_OTC = "acceptance";

    /**
     * 上传文件来源
     */
    public static final int TAKE_PHOTO = 0;
    public static final int CHOOSE_ALBUM = 1;

    /**
     * 支付方式类型
     */
    public static final String ALIPAY = "alipay"; // 支付宝
    public static final String WECHAT = "wechat"; // 微信
    public static final String BANK = "card"; // 银行
    public static final String PAYPAL = "PAYPAL"; // PAYPAL

    public static final String CNY = "CNY";

    /**
     * 分页
     */
    public static final int PageSize = 10;

    /**
     * webview参数
     */
    public static final String URL = "url";
    public static final String NEEDTITLE = "needTitle";
    public static final String TITLE = "title";

    public static final String KEEP_ALIVE = "http://manual.bp.wxmarket.cn/#/manual/device?id=";
    public static final String HELP = "http://manual.bp.wxmarket.cn/#/manual/qa";

}
