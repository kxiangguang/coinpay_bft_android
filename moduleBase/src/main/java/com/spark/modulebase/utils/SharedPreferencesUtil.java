package com.spark.modulebase.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.spark.modulebase.data.Language;

import java.util.Locale;


/**
 * SharedPreferencesgo
 */

public class SharedPreferencesUtil {
    private static final String FILE_NAME = "setting";
    private static SharedPreferences.Editor editor;
    private static SharedPreferences mPreferences;
    private static SharedPreferencesUtil sharedPreferencesUtil;
    public static final String SP_KEY_LANGUAGE = "SP_KEY_LANGUAGE";
    public static final String SP_KEY_TGT = "SP_KEY_TGT";
    public static final String SP_KEY_OTC_SID = "SP_KEY_OTC_SID";
    public static final String SP_KEY_SHOW = "SP_KEY_SHOW";
    public static final String SP_AHTH = "SP_AHTH";//承兑商是否认证
    public static final String SP_PAY_PASS = "SP_PAY_PASS";//交易密码是否设置
    public static final String SP_KEY_VOICE = "SP_KEY_VOICE";//音效
    public static final String SP_KEY_FIRST_USE = "SP_KEY_FIRST_USE";//首次使用显示引导页
    public static final String SP_KEY_LOGIN_ACCOUNT = "SP_KEY_LOGIN_ACCOUNT";//登录账号：手机号码
    public static final String SP_KEY_AUTO_ACCEPT = "SP_KEY_AUTO_ACCEPT";//自动放行
    public static final String SP_KEY_API_ADDRESS = "SP_KEY_API_ADDRESS";//API地址和备注
    public static final String SP_KEY_APP_NAME = "SP_KEY_APP_NAME";//APP名称
    public static final String SP_KEY_WRITE_API = "SP_KEY_WRITE_API";//是否填写API
    public static final String SP_KEY_WRITE_API_UC = "SP_KEY_WRITE_API_UC";//填写UC
    public static final String SP_KEY_WRITE_API_AC = "SP_KEY_WRITE_API_AC";//填写AC
    public static final String SP_KEY_WRITE_API_ACP = "SP_KEY_WRITE_API_ACP";//填写ACP
    public static final String SP_KEY_clearCode310 = "clearCode310";//版本28前的先清理 重新登录

    private static Context context;

    private SharedPreferencesUtil(Context context) {
        mPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = mPreferences.edit();
    }

    public static SharedPreferencesUtil getInstance(Context context) {
        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = new SharedPreferencesUtil(context);
        }
        return sharedPreferencesUtil;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public void setParam(Context context, String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.commit();
    }

    /**
     * string类型返回值
     *
     * @param context
     * @param key
     * @return
     */
    public String getStringParam(String key) {
        return mPreferences.getString(key, "");
    }

    /**
     * int类型返回值
     *
     * @param context
     * @param key
     * @return
     */
    public int getIntParam(String key) {
        if (StringUtils.isNotEmpty(key) && key.equals(SP_KEY_LANGUAGE))
            return mPreferences.getInt(key, 0);
        return mPreferences.getInt(key, 0);
    }

    /**
     * boolean类型返回值
     *
     * @param context
     * @param key
     * @return
     */
    public boolean getBooleanParam(String key) {
        return mPreferences.getBoolean(key, false);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public void clear() {
        editor.clear().commit();
    }

    /**
     * 清除指定数据
     *
     * @param context
     */
    public void clearAll(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 获取语言
     *
     * @return
     */
    public int getLanguageCode() {
        return getIntParam(SP_KEY_LANGUAGE);
    }

    /**
     * 保存语言
     *
     * @return
     */
    public void setLanguageCode(Context context, int code) {
        setParam(context, SP_KEY_LANGUAGE, code);
    }

    /**
     * 获取添加在头部的header
     *
     * @return
     */
    public String getAddHeaderLanguage(Context context) {
        int code = SharedPreferencesUtil.getInstance(context).getLanguageCode();
        Language language = Language.values()[code];
        Locale locale = new Locale(language.name());
        if (locale.getLanguage().equals(Locale.CHINESE.getLanguage())) {
            return "zh-CN,zh";
        } else if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            return "en-us,en";
        } else if (locale.getLanguage().equals(Locale.JAPAN.getLanguage())) {
            return "ja-JP";
        }
        return "zh-CN,zh";
    }

    /**
     * 保存tgt
     *
     * @return
     */
    public void setTgt(Context context, String tgt) {
        setParam(context, SP_KEY_TGT, tgt);
    }

    /**
     * 获取语言
     *
     * @return
     */
    public String getTgt() {
        return getStringParam(SP_KEY_TGT);
    }

    public void setOtcSid(Context context, String otcSid) {
        setParam(context, SP_KEY_OTC_SID, otcSid);
    }

    public String getSid() {
        return getStringParam(SP_KEY_OTC_SID);
    }


    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        return mPreferences.contains(key);
    }
}
