package com.spark.modulebase.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期格式化工具类
 */

public class DateUtils {

    public static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_2 = "m分s秒";
    public static final String DATE_FORMAT_3 = "后订单取消";
    public static final String DATE_FORMAT_4 = "后转超时单";

    /**
     * Date→String
     *
     * @param format
     * @param date
     * @return
     */
    public static String getFormatTime(String format, Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String formatTime = sdf.format(date);
        return formatTime;
    }

    /**
     * String→Long
     *
     * @param format
     * @param strDate
     * @return
     */
    public static long getTimeMillis(String format, String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(strDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Long→Date
     *
     * @param pattern
     * @param longDate
     * @return
     */
    public static Date getDate(String pattern, Long longDate) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String d = format.format(longDate);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * String→Date
     *
     * @param pattern
     * @param strDate
     * @return
     */
    public static Date getDateTransformString(String pattern, String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Long→String
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond(Long millisecond, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 当前时间推迟固定天数
     *
     * @param pattern
     * @param count
     * @return
     */
    public static String getBackDate(String pattern, int count) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, count); // 往上推一天  30推三十天  365推一年
        Date mBefore = calendar.getTime();
        return getFormatTime(pattern, mBefore);
    }


    /**
     * 获取倒计时时间 long→String
     *
     * @param randomTime毫秒数
     * @param pattern格式化
     * @param prefix前缀
     * @param suffix后缀
     * @return
     */
    public static String getTimeDown(long randomTime, String pattern, String prefix, String suffix) {
        if (randomTime < 0) {
            randomTime = 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return prefix + sdf.format(randomTime) + suffix;
    }

    public static void main(String[] art) {
        String ss = getTimeDown(0, DATE_FORMAT_2, "", "");
        LogUtils.e(ss);
    }

}
