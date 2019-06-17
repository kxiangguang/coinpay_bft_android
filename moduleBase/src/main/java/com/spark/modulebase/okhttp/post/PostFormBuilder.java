package com.spark.modulebase.okhttp.post;


import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.okhttp.OkhttpUtils;
import com.spark.modulebase.okhttp.RequestBuilder;
import com.spark.modulebase.okhttp.RequestCall;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * post表单
 */

public class PostFormBuilder extends RequestBuilder {
    private List<FileInput> files = new ArrayList<>();

    @Override
    public PostFormBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostFormRequest(url, params, headers, files).build();
    }

    @Override
    public PostFormBuilder addParams(String key, String value) {
        if (this.params == null) params = new HashMap<>();
        params.put(key, value);
        return this;
    }

    public PostFormBuilder addFile(FileInput fileInput) {
        if (fileInput == null) {
            files.add(fileInput);
        }
        return this;
    }

    public PostFormBuilder addFileList(List<FileInput> fileInputs) {
        if (fileInputs != null) {
            files.addAll(fileInputs);
        }
        return this;
    }

    @Override
    public RequestBuilder addParams(Map<String, String> map) {
        if (this.params == null) params = new HashMap<>();
        params.putAll(map);
        return this;
    }

    @Override
    public PostFormBuilder addHeader(String key, String value) {
        if (this.headers == null) headers = new HashMap<>();
        headers.put("Accept-Language", SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getAddHeaderLanguage(BaseApplication.getAppContext()));
        headers.put("User-Agent", OkhttpUtils.getUserAgent());
        headers.put(key, value);
        return this;
    }

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }
    }
}
