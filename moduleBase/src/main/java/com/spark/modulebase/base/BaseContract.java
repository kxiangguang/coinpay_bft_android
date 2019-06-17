package com.spark.modulebase.base;

import android.view.View;

import com.android.volley.VolleyError;
import com.spark.modulebase.entity.HttpErrorEntity;

import okhttp3.Response;

/**
 * Contract的基类
 */

public class BaseContract {
    public interface BaseView {
        void showLoading();

        void hideLoading();

        void dealError(HttpErrorEntity httpErrorEntity);

        void dealError(VolleyError volleyError);

    }

    public interface BasePresenter {
        void showLoading();

        void hideLoading();

        void destory();
    }
}
