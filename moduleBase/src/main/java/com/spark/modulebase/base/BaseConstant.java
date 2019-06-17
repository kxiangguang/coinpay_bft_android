package com.spark.modulebase.base;

/**
 * 常量
 */

public class BaseConstant {
    public static final int SUCCESS_CODE = 200; // 成功指令
    public static final int JSON_ERROR = -9999; // 解析出错
    public static final int CAPTCH = 411; // 全局拦截，显示极验
    public static final int CAPTCH2 = 412; //解决验证码失效问题
    public static final String UserSaveFileName = "USER.INFO"; // 保存的文件file
    public static final String UserSaveFileDirName = "USER"; // 保存文件file的目录
    public static final int MONEY_FORMAT = 4; // app金额格式化位数
    public static final int MONEY_FORMAT_ASSET = 2; // app金额格式化位数
    public static final int ERROR_CODE = 500; // 异常
    public static final int FAIL_CODE = 404; // 版本更新无新版本code
}
