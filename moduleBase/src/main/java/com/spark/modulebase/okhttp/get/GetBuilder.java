package com.spark.modulebase.okhttp.get;

import android.net.Uri;


import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.okhttp.OkhttpUtils;
import com.spark.modulebase.okhttp.RequestBuilder;
import com.spark.modulebase.okhttp.RequestCall;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2017/11/13.
 */

public class GetBuilder extends RequestBuilder {
    @Override
    public RequestCall build() {
        if (params != null) {
            url = appendParams(url, params);
        }
        return new GetRequest(url, params, headers).build();
    }

    private String appendParams(String url, Map<String, String> params) {
        //TODO
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }

    @Override
    public GetBuilder url(String url) {
        this.url = url;
        return this;
    }


    @Override
    public GetBuilder addParams(String key, String val) {
        //TODO
        return this;
    }

    @Override
    public GetBuilder addHeader(String key, String value) {
        if (this.headers == null) headers = new HashMap<>();
        headers.put("Accept-Language", SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getAddHeaderLanguage(BaseApplication.getAppContext()));
        headers.put("User-Agent", OkhttpUtils.getUserAgent());
        headers.put(key, value);
        return this;
    }

    @Override
    public RequestBuilder addParams(Map<String, String> map) {
        if (this.params == null) params = new HashMap<>();
        params.putAll(map);
        return this;
    }
}
