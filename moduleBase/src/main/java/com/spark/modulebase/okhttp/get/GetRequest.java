package com.spark.modulebase.okhttp.get;


import com.spark.modulebase.okhttp.OkHttpRequest;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * GetRequest
 */

public class GetRequest extends OkHttpRequest {

    protected GetRequest(String url, Map<String, String> params, Map<String, String> headers) {
        super(url, params, headers);
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.get().build();
    }
}
