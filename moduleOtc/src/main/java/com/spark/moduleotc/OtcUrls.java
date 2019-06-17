package com.spark.moduleotc;

/**
 * otc模块的url
 */

public class OtcUrls {
    private String host = "";
    private static OtcUrls loginUrls;

    public static OtcUrls getInstance() {
        if (loginUrls == null) {
            loginUrls = new OtcUrls();
        }
        return loginUrls;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    //获取支付方式
    public String queryPayWayListUrl() {
        return host + "/pay/queryList";
    }

    //首页获取认证状态
    public String findAuthenticationStatusUrl() {
        return host + "/navigation/get/info";
    }

    //首页获取在途订单
    public String findOrderInTransitUrl() {
        return host + "/order/intransit/list";
    }

    //我的资产-查询昨日佣金
    public String getTradeDayUsingPost() {
        return host + "/merchant/get/trade/day";
    }

    //我的资产-查询累计佣金和待结算佣金
    public String getTradeUsingGet() {
        return host + "/merchant/find/trade";
    }

    //获取APP版本更新信息
    public String getAppVersionUsingGet() {
        return host + "/app/check-version?platform=1";
    }

    //获取自动放行配置
    public String getAutoRelease() {
        return host + "/setting/get/auto-release";
    }


}
