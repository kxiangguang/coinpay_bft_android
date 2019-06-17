package com.spark.netty_library.netty;


/**
 * Created by daiyy on 20190121.
 */

public interface ConnectListener {
    int STATUS_CONNECT_ERROR = 0;
    int STATUS_CONNECT_SUCCESS = 1;
    int STATUS_CONNECT_CLOSED = 2;

    /**
     * 服务器状态发生变化
     */
    void onServiceStatusConnectChanged(int errorCode);
}
