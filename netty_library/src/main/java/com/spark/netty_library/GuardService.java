package com.spark.netty_library;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import com.spark.netty_library.listener.SendMsgListener;
import com.spark.netty_library.message.SocketMessage;
import com.spark.netty_library.netty.NettyInitDataUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;

import de.greenrobot.event.EventBus;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

/**
 * 接单,创建守护服务
 */

public class GuardService extends Service {
    /**
     * 是否是测试环境:false线上环境/true测试环境
     */
    public static boolean isTest = false;

    private static final String TAG = "GuardService";
    private static WebSocketConnection webSocketConnection; // ws 对应的类
    private static WebSocketOptions options = new WebSocketOptions(); //ws的个选项，声明出来即可使用了
    /**
     * 心跳检测时间
     */
    private static final long HEART_BEAT_RATE = 30 * 1000;//每隔30秒进行一次对长连接的心跳检测
    private SendMsgListener sendMsgListener;
    private PowerManager.WakeLock mWakeLock = null;//唤醒锁
    private int CONNECT_AGAIN = 10;//重新连接间隔时间秒
    private static boolean isNeedReconnect = true;//是否需要重连
    private static boolean isClosed = true;//连接是否被关闭

    //WebSocket地址配置
        private static String WEBSOCKET_HOST_AND_PORT = "ws://ws.bitaccept.1688up.net/ws";
//    private static String WEBSOCKET_HOST_AND_PORT = "";
//
    public static void setHost(String host) {
//        WEBSOCKET_HOST_AND_PORT = isTest ? "ws://" + host + ":38985/ws" :
//                (host.contains("bench") ? "ws://192.168.2.183:38985/ws" : "ws://ws.bitaccept." + host + "/ws");
    }

    public String getHost() {
        return WEBSOCKET_HOST_AND_PORT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new GuardService.MyBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        initWebSocketConnect();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initWebSocketConnect();
        int pol = Settings.System.WIFI_SLEEP_POLICY_NEVER;
        Settings.System.putInt(getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, pol);
        mTimeHandler.sendEmptyMessageDelayed(3, 5 * 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, new Notification());
        //绑定建立链接
        bindService(new Intent(this, WebSocketService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return Service.START_STICKY;
    }

    /**
     * 获取唤醒锁
     */
    private void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager mPM = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            mWakeLock = mPM.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
            if (mWakeLock != null) {
                mWakeLock.acquire();
            }
        }
    }

    /**
     * 释放锁
     */
    private void releaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //断开链接
            startService(new Intent(GuardService.this, WebSocketService.class));
            //重新绑定
            bindService(new Intent(GuardService.this, WebSocketService.class), mServiceConnection, Context.BIND_IMPORTANT);
            //正在运行的service通知，type：1WebSocketService 2GuardService
            EventBus.getDefault().post(new ServiceTypeEvent(1));
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeWebsocket();
        releaseWakeLock();
    }

    //这里使用binder 主要是为了在activity中进行通讯， 大家也可以使用EventBus进行通讯
    public class MyBinder extends Binder {

        public GuardService getService() {
            return GuardService.this;
        }
    }

    /**
     * 连接ws
     */
    public synchronized GuardService initSocket() {
        webSocketConnection = new WebSocketConnection();
        try {
            webSocketConnection.connect(WEBSOCKET_HOST_AND_PORT, new WebSocketHandler() {

                //websocket启动时候的回调
                @Override
                public void onOpen() {
                    Log.e(TAG, "GuardService- onOpen: 开启成功！！WEBSOCKET_HOST_AND_PORT==" + WEBSOCKET_HOST_AND_PORT);
                    isClosed = false;
                    isNeedReconnect = true;
                    EventBus.getDefault().post(new ServiceOpenSuccessEvent(2));
                }

                //GuardService接收到消息后的回调
                @Override
                public void onTextMessage(String content) {
                }

                @Override
                public void onBinaryMessage(byte[] content) {
                    //这里可以使用EventBus将内容传递到activity
                    InputStream is = new ByteArrayInputStream(content);
                    DataInputStream dis = new DataInputStream(is);
                    NettyInitDataUtils.startRecTask(sendMsgListener, dis);
                }

                @Override
                public void onRawTextMessage(byte[] content) {
                }


                //GuardService关闭时候的回调
                @Override
                public void onClose(int code, String reason) {
                    isClosed = true;
                    Log.e(TAG, "GuardService- 服务器关闭！！" + "isNeedReconnect==" + isNeedReconnect + ",WEBSOCKET_HOST_AND_PORT==" + WEBSOCKET_HOST_AND_PORT);
                    mHandler.removeCallbacks(heartBeatRunnable);
                    if (isNeedReconnect) {
                        EventBus.getDefault().post(new ConnectCloseEvent(2));
                        mTimeHandler.sendEmptyMessageDelayed(1, CONNECT_AGAIN * 1000);
                    } else {
                        mTimeHandler.removeMessages(1);
                    }
                }

            }, options);
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
        } catch (WebSocketException e) {
            e.printStackTrace();
            Log.e(TAG, "GuardService- 打开异常");
            isClosed = true;
            closeWebsocket();
        }
        return this;
    }

    /**
     * 关闭ws
     */
    public void closeWebsocket() {
        isNeedReconnect = false;
        try {
            if (webSocketConnection != null && webSocketConnection.isConnected()) {
                webSocketConnection.disconnect();
                webSocketConnection = null;
            }
        } catch (Exception e) {
        }
    }

    /**
     * 发送数据
     *
     * @param socketMessage
     * @param sendMsgListener
     */
    public void sendData(final SocketMessage socketMessage, final SendMsgListener sendMsgListener) {
        this.sendMsgListener = sendMsgListener;
        if (webSocketConnection != null && !isClosed) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e(TAG, "GuardService- 发送数据指令==" + socketMessage.getCmd() + ",,,,发送数据==" + socketMessage.getBody());
                        //发送消息
                        byte[] requestBytes = NettyInitDataUtils.buildRequest(socketMessage.getCmd(), socketMessage.getBody());
                        if (webSocketConnection != null && requestBytes != null) {
                            webSocketConnection.sendBinaryMessage(requestBytes);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (sendMsgListener != null) {
                            Log.e(TAG, "GuardService- 发送  message  异常" + e.toString());
                            sendMsgListener.error();
                        }
                    }
                }
            }).start();
        } else {
            if (sendMsgListener != null) {
                Log.e(TAG, "GuardService- 发送数据指令error==webSocketConnection==" + webSocketConnection + ",isClosed==" + isClosed);
                sendMsgListener.error();
            }
        }

    }

    private long sendTime = 0L;
    // 发送心跳包
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                try {
                    if (!isClosed && isNeedReconnect) {
                        byte[] requestBytes = NettyInitDataUtils.buildRequest(CMD.ORDER_TICK, null);
                        if (webSocketConnection != null && requestBytes != null) {
                            webSocketConnection.sendBinaryMessage(requestBytes);//发送一个空消息给服务器，通过发送消息的成功失败来判断长连接的连接状态
                            sendTime = System.currentTimeMillis();
                            Log.e(TAG, "GuardService- 发送一次心跳检测");
                        }
                        mHandler.postDelayed(this, HEART_BEAT_RATE);//每隔一定的时间，对长连接进行一次心跳检测
                    }
                } catch (Exception e) {

                }
            }
        }
    };

    Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initWebSocketConnect();
                    break;
                case 3:
                    acquireWakeLock();
                    break;
            }
        }
    };


    /**
     * 重新连接websocket
     */
    public void reconnect() {
        try {
            if (isClosed) {//连接已关闭
                if (webSocketConnection != null && webSocketConnection.isConnected()) {
                    Log.e(TAG, "GuardService- 重新连接websocket11111111111111111111111111111111111");
                    webSocketConnection.disconnect();
                    webSocketConnection = null;
                }
                isNeedReconnect = true;
                initWebSocketConnect();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 初始化WebSocket连接
     */
    private synchronized void initWebSocketConnect() {
        if (isClosed && isNeedReconnect) {
            Log.e(TAG, "WebSocketService- 创建一个新的连接WEBSOCKET_HOST_AND_PORT==" + WEBSOCKET_HOST_AND_PORT);
            new InitSocketThread().start();//创建一个新的连接
        }
    }

    private class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                initSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}