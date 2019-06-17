package com.spark.netty_library.listener;


import com.spark.netty_library.message.SocketResponse;

/**
 * 发送消息的监听
 * Created by daiyy on 20190121.
 */

public interface SendMsgListener {

    void onMessageResponse(SocketResponse socketResponse);

    void error();
}
