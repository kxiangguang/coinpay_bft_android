package com.spark.coinpay.event;

/**
 * 倒计时完成通知
 */
public class TimeDownFinishEvent {
    public long randomTime;//倒计时为负数时，每隔10秒刷新一次数据

    public TimeDownFinishEvent(long t) {
        randomTime = t;
    }
}
