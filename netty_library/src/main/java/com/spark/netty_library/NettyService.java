package com.spark.netty_library;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.spark.netty_library.listener.SendMsgListener;
import com.spark.netty_library.message.SocketMessage;
import com.spark.netty_library.netty.ConnectListener;
import com.spark.netty_library.netty.NettyClient;

import java.util.HashMap;

/**
 * 接单长连接
 */
public class NettyService extends Service implements ConnectListener {

    /**
     * 是否是测试环境:false线上环境/true测试环境
     */
    private static final boolean isTest = false;
    //币承兑
    private static final String CUR_HOST = "bp.wxmarket.cn";
    //合众承兑
//    private static final String CUR_HOST = "555hub.com";
    //点币承兑
//    private static final String CUR_HOST = "dbipay.com";

    private String ip = isTest ? "192.168.2.89" : "ws." + CUR_HOST;
    private int port = 38901;

    public static final String TAG = NettyService.class.getName();
    private HashMap<Integer, NettyClient> hashMap;
    private NettyClient nettyClient;
    private int channel = 0; // 不同地址的socket
    private SocketBinder sockerBinder = new SocketBinder();

    public NettyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sockerBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        hashMap = new HashMap<>();
        if (hashMap.size() > 0) {
            nettyClient = hashMap.get(channel);
        }
        Log.i(TAG, "ip==" + ip + ",port==" + port + ",size==" + hashMap.size());
        if (nettyClient == null) {
            nettyClient = NettyClient.init();
            nettyClient.setListener(this);
            hashMap.put(channel, nettyClient);
        }
        connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (hashMap == null || hashMap.size() == 0)
            return;
        for (NettyClient nettyClient : hashMap.values()) {
            nettyClient.setReconnectNum(0);
            nettyClient.disconnect();
        }
    }

    /**
     * 创建socket对象
     *
     * @param ip
     * @param port
     */
    private void connect() {
        if (!nettyClient.checkConnectStatus(ip, port)) {
            nettyClient.connect(ip, port); // 连接服务器
        }
    }

    /**
     * 发送数据
     *
     * @param socketMessage
     * @param sendMsgListener
     */
    public void sendData(SocketMessage socketMessage, SendMsgListener sendMsgListener) {
        int code = socketMessage.getChannel();
        if (hashMap != null && hashMap.get(code) != null) {
            NettyClient nettyClient = hashMap.get(code);
            nettyClient.sendMessage(socketMessage, sendMsgListener);
        } else {
            if (sendMsgListener != null) {
                sendMsgListener.error();
            }
        }
    }

    @Override
    public void onServiceStatusConnectChanged(int errorCode) {
        switch (errorCode) {
            case STATUS_CONNECT_SUCCESS:
                Log.e("NettyService", "连接成功");
                break;
            case STATUS_CONNECT_ERROR:
                Log.e("NettyService", "连接失败");
                break;
            case STATUS_CONNECT_CLOSED:
                Log.e("NettyService", "连接关闭");
                break;
        }
    }

    /**
     * 返回NettyService 在需要的地方可以通过ServiceConnection获取到NettyService
     */
    public class SocketBinder extends Binder {
        public NettyService getService() {
            return NettyService.this;
        }
    }

}
