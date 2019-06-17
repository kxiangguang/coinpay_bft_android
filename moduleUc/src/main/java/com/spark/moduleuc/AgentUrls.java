package com.spark.moduleuc;

/**
 * 账户设置
 */

public class AgentUrls {
    private String host = "";
    private static AgentUrls agentUrls;

    public static AgentUrls getInstance() {
        if (agentUrls == null) {
            agentUrls = new AgentUrls();
        }
        return agentUrls;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }


    /**
     * 校验推荐码
     *
     * @return
     */
    public String checkInviteCode() {
        return host + "/member/check/promotionCode?promotionCode=";
    }

}
