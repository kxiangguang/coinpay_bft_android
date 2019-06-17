package com.spark.modulebase.utils;

import android.util.Log;

import com.spark.modulebase.BaseApplication;

/**
 * 打印工具类
 */
public class LogUtils {
    static String className;//类名
    static String methodName;//方法名
    static int lineNumber;//行数
    private static AppDataSetUtils appDataSetUtils;

    /**
     * 是否打印
     *
     * @return
     */
    public static boolean isDebuggable() {
        if (appDataSetUtils == null) {
            appDataSetUtils = new AppDataSetUtils();
        }
        return appDataSetUtils.isDebug();
    }

    private static String createLog(String log) {
        getMethodNames(new Throwable().getStackTrace());
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName(); // 方法名
        lineNumber = sElements[1].getLineNumber();// 行数及对应的类名
    }


    public static void e(String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }


    public static void i(String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message) {
        if (!isDebuggable())
            return;
        Log.wtf(className, createLog(message));
    }

}
