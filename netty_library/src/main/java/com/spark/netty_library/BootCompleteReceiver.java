package com.spark.netty_library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2019/4/28 0028.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    /**
     * 开机完成广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BootCompleteReceiver", "开机完成广播==================");
        Intent mIntent = new Intent(context, WebSocketService.class);
//        context.startService(mIntent);
    }
}

