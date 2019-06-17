package com.spark.modulebase.utils;

import android.content.Context;

import java.util.Locale;

/**
 * 全局共用的数据
 */

public class AppDataSetUtils {
    private boolean isDebug = true; // 是否打印log日志
    private String host = "";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 是否打印log
     *
     * @return
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * 设置是否打印log
     *
     * @param debug
     */
    public void setDebug(boolean debug) {
        isDebug = debug;
    }

}
