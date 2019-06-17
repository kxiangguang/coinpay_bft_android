package com.spark.netty_library;

/**
 * 服务器断开连接通知
 */
public class ConnectCloseEvent {
    /**
     * type：1WebSocketService 2GuardService
     **/
    public int type;

    public ConnectCloseEvent(int t) {
        type = t;
    }

}
