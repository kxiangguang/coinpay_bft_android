package com.spark.modulebase.okhttp;


import com.spark.modulebase.utils.LogUtils;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/9/29.
 */

public class RequestCall {
    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;

    public RequestCall(OkHttpRequest okHttpRequest) {
        this.okHttpRequest = okHttpRequest;
    }

    public void execute(Callback callback) {
        generateCall(callback);
        if (callback != null) {
            callback.onBefore(request);
        }
        OkhttpUtils.getInstance().execute(this, callback);
    }

    private Call generateCall(Callback callback) {
        request = generateRequest(callback);
        call = OkhttpUtils.getInstance().getOkHttpClient().newCall(request);
        return call;
    }


    private Request generateRequest(Callback callback) {
        return okHttpRequest.generateRequest(callback);
    }

    public Call getCall() {
        return call;
    }
}


