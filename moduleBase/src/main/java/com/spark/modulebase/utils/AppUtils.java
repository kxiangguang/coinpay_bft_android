package com.spark.modulebase.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * 通用工具类
 */

public class AppUtils {
    /**
     * 将指定内容粘贴到剪贴板
     *
     * @param content 剪切内容
     */
    public static void copyText(Context context, String content) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newRawUri("copyLable", Uri.parse(content));
        cm.setPrimaryClip(mClipData);
    }

    /**
     * 获取版本名称
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            //第二个参数代表额外的信息，例如获取当前应用中的所有的Activity
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号大小
     */
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            //第二个参数代表额外的信息，例如获取当前应用中的所有的Activity
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取手机序列号
     *
     * @return
     */
    public static String getSerialNumber() {
        String serialnum = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (Exception ignored) {
        }
        LogUtils.d("获取手机序列号serialnum==" + serialnum);
        return serialnum;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 程序是否在前台运行
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 安装
     *
     * @param apkPath
     */
    public static void installAPk(File apkPath, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri apkUri = UriUtils.getUriForFile(context, apkPath);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取包名
     *
     * @return
     */
    public static String getAppPackageName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getPackageName();
    }

    /**
     * 比较版本大小
     * <p>
     * 说明：支n位基础版本号+1位子版本号
     * 示例：1.0.2>1.0.1 , 1.0.1.1>1.0.1
     *
     * @param version1 版本1
     * @param version2 版本2
     * @return 0:相同 1:version1大于version2 -1:version1小于version2
     */
    public static int compareVersion(String version1, String version2) {
        if (StringUtils.isEmpty(version1, version2)) {
            return 0; //版本相同
        }
        if (version1.equals(version2)) {
            return 0; //版本相同
        }
        String[] v1Array = version1.split("\\.");
        String[] v2Array = version2.split("\\.");
        int v1Len = v1Array.length;
        int v2Len = v2Array.length;
        int baseLen = 0; //基础版本号位数（取长度小的）
        if (v1Len > v2Len) {
            baseLen = v2Len;
        } else {
            baseLen = v1Len;
        }

        for (int i = 0; i < baseLen; i++) { //基础版本号比较
            if (v1Array[i].equals(v2Array[i])) { //同位版本号相同
                continue; //比较下一位
            } else {
                return Integer.parseInt(v1Array[i]) > Integer.parseInt(v2Array[i]) ? 1 : -1;
            }
        }
        //基础版本相同，再比较子版本号
        if (v1Len != v2Len) {
            return v1Len > v2Len ? 1 : -1;
        } else {
            //基础版本相同，无子版本号
            return 0;
        }
    }


    /**
     * 检查是否有网络
     */
    public static boolean checkNetConnect(Context context) {
        boolean isConnect = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        NetworkInfo.State mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if ((wifiState != null && wifiState == NetworkInfo.State.CONNECTED) || (mobileState != null && mobileState == NetworkInfo.State.CONNECTED)) {
            isConnect = true;
        }
        return isConnect;
    }

    /**
     * 获取颜色值
     *
     * @param context
     * @param resId
     * @return
     */
    public static int getColor(Context context, @ColorRes int resId) {
        return ContextCompat.getColor(context, resId);
    }

    /**
     * 获取尺寸
     *
     * @param context
     * @param resId
     * @return
     */
    public static float getDimension(Context context, @DimenRes int resId) {
        return context.getResources().getDimension(resId);
    }

    /**
     * 判断应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static Boolean isInstalled(Context context, String packageName) {
        boolean bInstalled = false;
        if (packageName == null) return false;
        PackageInfo packageInfo = null;

        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }

        if (packageInfo == null) {
            bInstalled = false;
        } else {
            bInstalled = true;
        }
        return bInstalled;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 生成二维码
     *
     * @param text
     * @param size
     * @return
     */
    public static Bitmap createQRCode(String text, int size) {
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 2);   //设置白边大小 取值为 0- 4 越大白边越大
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * size + x] = 0xff000000;
                    } else {
                        pixels[y * size + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 启动页变形优化
     *
     * @param activity
     * @param view
     * @param drawableResId
     */
    public static void scaleImage(final Activity activity, final View view, int drawableResId) {
        // 获取屏幕的高宽
        Point outSize = new Point();//720*1184
        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);
        // 解析将要被处理的图片
        Bitmap resourceBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId);
        if (resourceBitmap == null) {
            return;
        }
        // 开始对图片进行拉伸或者缩放
        // 使用图片的缩放比例计算将要放大的图片的高度
        int bitmapScaledWitch = Math.round(resourceBitmap.getWidth() * outSize.y * 1.0f / resourceBitmap.getHeight());
        // 以屏幕的宽度为基准，如果图片的宽度比屏幕宽，则等比缩小，如果窄，则放大
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(resourceBitmap, bitmapScaledWitch, outSize.y, false);
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //这里防止图像的重复创建，避免申请不必要的内存空间
                if (scaledBitmap.isRecycled()) {
                    return true;//必须返回true
                }
                // 当UI绘制完毕，我们对图片进行处理
                int viewHeight = view.getMeasuredHeight();
                // 计算将要裁剪的图片的顶部以及底部的偏移量
                int offset = (scaledBitmap.getHeight() - viewHeight) / 2;
                //优化小米系列手机在缩放时offset小于0的问题
                if (offset < 0) {
                    offset = 0;
                }
                // 对图片以中心进行裁剪，裁剪出的图片就是非常适合做引导页的图片了
                Bitmap finallyBitmap = null;
                int width = scaledBitmap.getWidth();
                int hight = scaledBitmap.getHeight() - offset * 2;
                if (width <= 0) {
                    width = 1080;
                }
                if (hight <= 0) {
                    hight = 1920;
                }
                finallyBitmap = Bitmap.createBitmap(scaledBitmap, 0, offset, width, hight);
                if (!finallyBitmap.equals(scaledBitmap)) {//如果返回的不是原图，则对原图进行回收
                    scaledBitmap.recycle();
                    System.gc();
                }
                // 设置图片显示
                view.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), finallyBitmap));
                return true;
            }
        });
    }

    /**
     * 完成率
     *
     * @param num
     * @param total
     * @return
     */
    public static String getRate(int num, int total) {
        if (total == 0) {
            return "100%";
        }
        //0表示的是小数点  之前没有这样配置有问题例如  num=1 and total=1000  结果是.1  很郁闷
        DecimalFormat df = new DecimalFormat("0%");
        //可以设置精确几位小数
        df.setMaximumFractionDigits(0);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num * 1.0 / total * 1.0;
        return df.format(accuracy_num);
    }

    /**
     * 平均放行速度
     *
     * @param num
     * @param total
     * @return
     */
    public static String getSpeed(int num, int total) {
        if (total == 0) {
            return "0分";
        }
        //0表示的是小数点  之前没有这样配置有问题例如  num=1 and total=1000  结果是.1  很郁闷
        DecimalFormat df = new DecimalFormat("0.00分");
        //可以设置精确几位小数
        df.setMaximumFractionDigits(2);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num * 1.0 / total * 1.0 / 60;
        return df.format(accuracy_num);
    }

    /**
     * 手机号码加*号
     *
     * @param mobile
     * @return
     */
    public static String addStar(String mobile) {
        if (mobile == null || "".equals(mobile)) {
            mobile = "";
        } else {
            String front = mobile.substring(0, 3);
            String last = mobile.substring(7);
            mobile = front + "****" + last;
        }
        return mobile;
    }

    /**
     * @param account收款账户
     * @param type1支付宝2微信3银行卡4PayPal
     * @return
     */
    public static String getPayAccountStr(String account, int type, String bankName) {
        if (StringUtils.isEmpty(account)) {
            return "";
        } else {
            String result = account;
            switch (type) {
                case 1:
                    if (account.length() > 4) {
                        String front = account.substring(0, 2);
                        String last = account.substring(account.length() - 2);
                        result = front + "**" + last;
                    }
                    break;
                case 2:
                    if (account.length() > 4) {
                        String front = account.substring(0, 2);
                        String last = account.substring(account.length() - 2);
                        result = front + "**" + last;
                    }
                    break;
                case 3:
                    if (account.length() > 4) {
                        String last = account.substring(account.length() - 4);
                        result = last;
                    }
                    break;
                case 4:
                    if (account.length() > 4) {
                        String front = account.substring(0, 2);
                        String last = account.substring(account.length() - 2);
                        result = front + "**" + last;
                    }
                    break;
                default:
                    return "";
            }
            return bankName + "(" + result + ")";
        }
    }

    /**
     * 获取通知中金额
     *
     * @param content
     * @param pkgName
     * @return
     */
    public static String getNoticeMoney(String content, String pkgName) {
        String money = "";
        //收银台支付宝付款：小新通过扫码向你付款1.00元
        //支付宝扫码：成功收款0.01元。享免费提现等更多专属服务，点击查看
        //微信扫码：微信支付收款1.00元
        Pattern pJxYmf_Nofity;
        Pattern pAlipay;
        Pattern pAlipay2;
        Pattern pAlipayDianyuan;
        Pattern pWeixin;
        //支付宝
        pAlipay = Pattern.compile("(\\S*)通过扫码向你付款([\\d\\.]+)元");
        pAlipay2 = Pattern.compile("成功收款([\\d\\.]+)元。享免费提现等更多专属服务，点击查看");
        pAlipayDianyuan = Pattern.compile("支付宝成功收款([\\d\\.]+)元。收钱码收钱提现免费，赶紧推荐顾客使用");
        //微信
        pWeixin = Pattern.compile("微信支付收款([\\d\\.]+)元");
        //金信一码付
        pJxYmf_Nofity = Pattern.compile("一笔收款交易已完成，金额([\\d\\.]+)元");
        //支付宝com.eg.android.AlipayGphone
        //com.eg.android.AlipayGphone]:支付宝通知 & 新哥通过扫码向你付款0.01元
        if (pkgName.equals("com.eg.android.AlipayGphone")) {
            // 现在创建 matcher 对象
            Matcher m = pAlipay.matcher(content);
            if (m.find()) {
                //String uname = m.group(1);
                money = m.group(2);
            }
            m = pAlipay2.matcher(content);
            if (m.find()) {
                //支付宝用户
                money = m.group(1);
            }
            m = pAlipayDianyuan.matcher(content);
            if (m.find()) {
                //支付宝-店员
                money = m.group(1);
            }
        }
        //微信
        //com.tencent.mm]:微信支付 & 微信支付收款0.01元
        else if (pkgName.equals("com.tencent.mm")) {
            // 现在创建 matcher 对象
            Matcher m = pWeixin.matcher(content);
            if (m.find()) {
                //微信用户
                money = m.group(1);
            }
        } else if (pkgName.equals("com.buybal.buybalpay.nxy.jxymf")) {
            Matcher m = pJxYmf_Nofity.matcher(content);
            if (m.find()) {
                //一码付
                money = m.group(1);
            }
        }
        //QQ
        else if (pkgName.equals("com.tencent.mobileqq222")) {
            Matcher m = pAlipay.matcher(content);
            if (m.find()) {
                //String uname = m.group(1);
                money = m.group(2);
            }
        }
        //小米服务框架
        else if (pkgName.equals("com.xiaomi.xmsf")) {
            // 现在创建 matcher 对象
            Matcher m = pAlipay.matcher(content);
            if (m.find()) {
                //String uname = m.group(1);
                money = m.group(2);
            }
            m = pAlipay2.matcher(content);
            if (m.find()) {
                //支付宝用户
                money = m.group(1);
            }
            m = pAlipayDianyuan.matcher(content);
            if (m.find()) {
                //支付宝-店员
                money = m.group(1);
            }
        }
        return money;
    }

    /**
     * 判断是否为合法IP * @return the ip
     */
    public static boolean isIp(String... strings) {
        if (StringUtils.isEmpty(strings)) {
            return false;
        }
        for (String ipAddress : strings) {
            String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
            Pattern pattern = Pattern.compile(ip);
            Matcher matcher = pattern.matcher(ipAddress);
            return matcher.matches();
        }
        return false;
    }
}
