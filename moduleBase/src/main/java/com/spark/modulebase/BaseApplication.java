package com.spark.modulebase;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.spark.modulebase.entity.User;
import com.spark.modulebase.okhttp.store.PersistentCookieStore;
import com.spark.modulebase.utils.AppDataSetUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.spark.modulebase.utils.UserUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;

/**
 * 基础Application类
 */

public class BaseApplication extends Application {
    private static BaseApplication mAppContext;
    private User currentUser;
    private PersistentCookieStore persistentCookieStore;
    private CookieManager cookieManager;

    @Override
    public void onCreate() {
        super.onCreate();
        persistentCookieStore = new PersistentCookieStore(this);
        cookieManager = new CookieManager(persistentCookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        mAppContext = this;
        checkPermission();
        closeAndroidPDialog();
    }

    /**
     * 检查是否具有权限
     */
    private void checkPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.d("permission: " + data.get(0));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                currentUser = UserUtils.getCurrentUserFromFile();
                            }
                        }).start();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (AndPermission.hasAlwaysDeniedPermission(mAppContext, data)) {
                            AndPermission.permissionSetting(mAppContext).execute();
                            return;
                        }
                        ToastUtils.showToast(mAppContext, getString(R.string.str_no_permission));
                    }
                }).start();
    }

    /**
     * 获取系统上下文：用于ToastUtil类
     */
    public static BaseApplication getAppContext() {
        return mAppContext;
    }

    /**
     * 删除保存的user
     */
    public void deleteCurrentUser() {
        this.currentUser = null;
        persistentCookieStore.removeAll();
        UserUtils.deleteCurrentUserFile();
    }

    /**
     * 获取当前的user
     *
     * @param user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        UserUtils.saveCurrentUser(currentUser);
    }

    public User getCurrentUser() {
        return currentUser == null ? new User() : currentUser;
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    /**
     * 解决在Android P上的提醒弹窗 'Detected problems with API'
     */
    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
