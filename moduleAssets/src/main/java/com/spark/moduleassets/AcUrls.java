package com.spark.moduleassets;

/**
 * 资产
 */

public class AcUrls {
    private String host = "";
    private static AcUrls acUrls;

    public static AcUrls getInstance() {
        if (acUrls == null) {
            acUrls = new AcUrls();
        }
        return acUrls;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

}
