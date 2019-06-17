package com.spark.netty_library;

/**
 * 正在运行的service通知
 */
public class ServiceTypeEvent {
    /**type：1WebSocketService 2GuardService**/
    public int type;

    public ServiceTypeEvent(int t) {
        type = t;
    }

}
