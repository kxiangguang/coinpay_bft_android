package com.spark.modulebase.utils;


import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.entity.LoadExceptionEvent;
import com.spark.modulebase.event.CheckLoginEvent;
import com.spark.modulebase.R;
import com.spark.modulebase.event.LoginoutWithoutApiEvent;

import de.greenrobot.event.EventBus;


/**
 * Created by wonderful on 2017/5/23.
 * 网络请求错误
 */

public class CheckVolleyErrorUtil {

    /**
     * VolleyError
     *
     * @param error
     */
    public static void checkError(VolleyError error) {
        if (error != null) {
            Context context = BaseApplication.getAppContext();
            if (error instanceof TimeoutError) {
                Toast.makeText(context, context.getString(R.string.str_connection_out), Toast.LENGTH_SHORT).show();
                return;
            }
            if (error instanceof ServerError) {
                Toast.makeText(context, context.getString(R.string.str_server_error), Toast.LENGTH_SHORT).show();
                return;
            }
            if (error instanceof NetworkError) {
                Toast.makeText(context, context.getString(R.string.str_please_check_net), Toast.LENGTH_SHORT).show();
                return;
            }
            if (error instanceof ParseError) {
                Toast.makeText(context, context.getString(R.string.str_json_error), Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtils.isNotEmpty(error.getMessage()) && error.getMessage().contains("BEGIN_")) {
                if (error.getMessage().contains("acceptance")) {
                    EventBus.getDefault().post(new CheckLoginEvent("acceptance"));
                } else if (error.getMessage().contains("uc")) {
                    EventBus.getDefault().post(new CheckLoginEvent("uc"));
                } else if (error.getMessage().contains("ac")) {
                    EventBus.getDefault().post(new CheckLoginEvent("ac"));
                } else {
                    LogUtils.e("CheckVolleyErrorUtil.checkError(VolleyError error)===error.getMessage()==" + error.getMessage() + ",new LoadExceptionEvent()==退出登录=======");
                    EventBus.getDefault().post(new LoadExceptionEvent());
                }
                return;
            }
            if (StringUtils.isNotEmpty(error.getMessage()) && error.getMessage().contains("Bad URL")) {
                LogUtils.e("CheckVolleyErrorUtil.checkError(VolleyError error)===error.getMessage()==" + error.getMessage() + ",new LoadExceptionEvent()==退出登录=======");
                EventBus.getDefault().post(new LoginoutWithoutApiEvent());
                return;
            }
            Toast.makeText(BaseApplication.getAppContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

}
