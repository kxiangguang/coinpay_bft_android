package com.spark.netty_library;

/**
 * 接单相关指令
 */

public interface CMD {
    short LOGIN = 11002;//登录指令
    short CONNECT = 11003;//连接指令
    short START_ORDER = 20001;//开始接单指令
    short STOP_ORDER = 20002;//停止接单指令
    short ACCEPT_ORDER = 20003; //订单承接通知,收到该指令，说明承兑商有订单匹配，此时需要查询在途订单
    short ORDER_STATUS = 20004;//订单状态变更
    short ORDER_TICK = 11004;//心跳指令，对长连接进行心跳检测
}
