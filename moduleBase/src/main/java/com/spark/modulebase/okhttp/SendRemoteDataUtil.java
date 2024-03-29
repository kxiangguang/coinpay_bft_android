package com.spark.modulebase.okhttp;

import com.spark.modulebase.BaseApplication;
import com.spark.modulebase.R;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.okhttp.post.PostFormBuilder;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.NetworkUtil;
import com.spark.modulebase.utils.SharedPreferencesUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 发送数据
 */
public class SendRemoteDataUtil {
    private static SendRemoteDataUtil INSTANCE;

    /**
     * 普通的数据请求回调
     */
    public interface DataCallback {
        void onDataLoaded(Object obj);

        void onDataNotAvailable(HttpErrorEntity httpErrorEntity);
    }

    /**
     * 下载文件回调
     */
    public interface FileDownloadDataCallback extends DataCallback {
        void intProgress(float progress);
    }

    /**
     * 图片验证码
     */
    public interface ImgDataCallback extends DataCallback {
        void onDataLoaded(okhttp3.Response response);
    }

    public static SendRemoteDataUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SendRemoteDataUtil();
        }
        return INSTANCE;
    }

    /**
     * 有参数的post请求
     *
     * @param url
     * @param params
     * @param dataCallback
     */
    public void doStringPost(String url, HashMap<String, String> params, final DataCallback callback) {
        if (NetworkUtil.checkNetwork(BaseApplication.getAppContext())) {
            String tgt = SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getTgt();
            OkhttpUtils.post().url(url)
                    .addHeader("tgt", tgt)
                    .addHeader("tid", AppUtils.getSerialNumber())
                    .addParams(params).build().execute(new StringCallback() {
                @Override
                public void onError(Request request, HttpErrorEntity httpErrorEntity) { // 获得response响应
                    callback.onDataNotAvailable(httpErrorEntity);
                    if (httpErrorEntity != null) {
                        LogUtils.i("onResponse：" + httpErrorEntity.toString());
                        LogUtils.i("request：" + request.toString() + "error:" + httpErrorEntity.getCode());
                    }
                }

                @Override
                public void onResponse(String response) {
                    LogUtils.i("onResponse：" + response.toString());
                    callback.onDataLoaded(response);
                }

            });
            String strParams = url;
            if (params != null) {
                strParams = strParams + "?";
                for (String key : params.keySet()) {
                    strParams = strParams + key + "=" + params.get(key) + "&";
                }
            }
            LogUtils.i("传参==" + strParams);
        } else {
            HttpErrorEntity httpErrorEntity = new HttpErrorEntity(-1, BaseApplication.getAppContext().getResources().getString(R.string.str_please_check_net), url, null);
            callback.onDataNotAvailable(httpErrorEntity);
        }
    }

    /**
     * get请求
     *
     * @param params
     * @param dataCallback
     */
    public void doStringGet(String url, HashMap<String, String> params, final DataCallback callback) {
        if (NetworkUtil.checkNetwork(BaseApplication.getAppContext())) {
            OkhttpUtils.get().url(url)
                    .addHeader("tid", AppUtils.getSerialNumber())
                    .addParams(params).build().execute(new StringCallback() {
                @Override
                public void onError(Request request, HttpErrorEntity httpErrorEntity) {
                    callback.onDataNotAvailable(httpErrorEntity);
                    if (httpErrorEntity != null) {
                        LogUtils.i("onResponse：" + httpErrorEntity.toString());
                        LogUtils.i("request：" + request.toString() + "error:" + httpErrorEntity.getCode());
                    }
                }

                @Override
                public void onResponse(String response) {
                    LogUtils.i("onResponse：" + response.toString());
                    callback.onDataLoaded(response);
                }
            });
            String strParams = url;
            if (params != null) {
                strParams = url + "?";
                for (String key : params.keySet()) {
                    strParams = strParams + key + "=" + params.get(key) + "&";
                }
            }
            LogUtils.i("传参==" + strParams);
        } else {
            HttpErrorEntity httpErrorEntity = new HttpErrorEntity(-1, BaseApplication.getAppContext().getResources().getString(R.string.str_please_check_net), url, null);
            callback.onDataNotAvailable(httpErrorEntity);
        }
    }

    /**
     * get请求
     *
     * @param params
     * @param dataCallback
     */
    public void doStringGetWithHead(String url, HashMap<String, String> params, final DataCallback callback) {
        if (NetworkUtil.checkNetwork(BaseApplication.getAppContext())) {
            String tgt = SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getTgt();
            OkhttpUtils.get().url(url)
                    .addHeader("tgt", tgt)
                    .addHeader("Connection", "close")
                    .addParams(params).build().execute(new StringCallback() {
                @Override
                public void onError(Request request, HttpErrorEntity httpErrorEntity) { // 获得response响应
                    callback.onDataNotAvailable(httpErrorEntity);
                    if (httpErrorEntity != null) {
                        LogUtils.i("onResponse：" + httpErrorEntity.toString());
                        LogUtils.i("request：" + request.toString() + "error:" + httpErrorEntity.getCode());
                    }
                }

                @Override
                public void onResponse(String response) {
                    LogUtils.i("onResponse：" + response.toString());
                    callback.onDataLoaded(response);
                }

            });
            String strParams = url;
            if (params != null) {
                strParams = strParams + "?";
                for (String key : params.keySet()) {
                    strParams = strParams + key + "=" + params.get(key) + "&";
                }
            }
            LogUtils.i("传参==" + strParams);
        } else {
            HttpErrorEntity httpErrorEntity = new HttpErrorEntity(-1, BaseApplication.getAppContext().getResources().getString(R.string.str_please_check_net), url, null);
            callback.onDataNotAvailable(httpErrorEntity);
        }
    }

    /**
     * get请求
     *
     * @param params
     * @param dataCallback
     */

    public void doStringGetWithHeadAndCheck(String url, String code, HashMap<String, String> params, final DataCallback callback) {
        if (NetworkUtil.checkNetwork(BaseApplication.getAppContext())) {
            String tgt = SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getTgt();
            OkhttpUtils.get().url(url)
                    .addHeader("tgt", tgt)
                    .addHeader("check", "phone::" + code).
                    addParams(params).build().execute(new StringCallback() {
                @Override
                public void onError(Request request, HttpErrorEntity httpErrorEntity) { // 获得response响应
                    callback.onDataNotAvailable(httpErrorEntity);
                    if (httpErrorEntity != null) {
                        LogUtils.i("onResponse：" + httpErrorEntity.toString());
                        LogUtils.i("request：" + request.toString() + "error:" + httpErrorEntity.getCode());
                    }
                }

                @Override
                public void onResponse(String response) {
                    LogUtils.i("onResponse：" + response.toString());
                    callback.onDataLoaded(response);
                }

            });
            String strParams = url;
            if (params != null) {
                strParams = strParams + "?";
                for (String key : params.keySet()) {
                    strParams = strParams + key + "=" + params.get(key) + "&";
                }
            }
            LogUtils.i("传参==" + strParams);
        } else {
            HttpErrorEntity httpErrorEntity = new HttpErrorEntity(-1, BaseApplication.getAppContext().getResources().getString(R.string.str_please_check_net), url, null);
            callback.onDataNotAvailable(httpErrorEntity);
        }
    }

    /**
     * delete请求
     */
    public void doStringDelete(String url, HashMap<String, String> params, final DataCallback callback) {
        if (NetworkUtil.checkNetwork(BaseApplication.getAppContext())) {
            String tgt = SharedPreferencesUtil.getInstance(BaseApplication.getAppContext()).getTgt();
            OkhttpUtils.delete().url(url)
                    .addHeader("tgt", tgt)
                    .addParams(params).build().execute(new StringCallback() {
                @Override
                public void onError(Request request, HttpErrorEntity httpErrorEntity) {
                    callback.onDataNotAvailable(httpErrorEntity);
                    if (httpErrorEntity != null) {
                        LogUtils.i("onResponse：" + httpErrorEntity.toString());
                        LogUtils.i("request：" + request.toString() + "error:" + httpErrorEntity.getCode());
                    }
                }

                @Override
                public void onResponse(String response) {
                    LogUtils.i("onResponse：" + response.toString());
                    callback.onDataLoaded(response);
                }
            });
        } else {
            HttpErrorEntity httpErrorEntity = new HttpErrorEntity(-1, BaseApplication.getAppContext().getResources().getString(R.string.str_please_check_net), url, null);
            callback.onDataNotAvailable(httpErrorEntity);
        }
    }

    /**
     * 下载文件
     */

    public void doDownload(String url, String filePath, final FileDownloadDataCallback callback) {
        if (NetworkUtil.checkNetwork(BaseApplication.getAppContext())) {
            String path = filePath;
            OkhttpUtils.get().url(url).build().execute(new FileCallback(path) {
                @Override
                public void inProgress(float progress) {
                    callback.intProgress(progress);
                }

                @Override
                public void onError(Request request, HttpErrorEntity httpErrorEntity) {
                    callback.onDataNotAvailable(httpErrorEntity);
                    if (httpErrorEntity != null) {
                        LogUtils.i("onResponse：" + httpErrorEntity.toString());
                        LogUtils.i("request：" + request.toString() + "error:" + httpErrorEntity.getCode());
                    }
                }

                @Override
                public void onResponse(File response) {
                    callback.onDataLoaded(response);
                }
            });
            LogUtils.i("下载链接==" + url);
        } else {
            HttpErrorEntity httpErrorEntity = new HttpErrorEntity(-1, BaseApplication.getAppContext().getResources().getString(R.string.str_please_check_net), url, null);
            callback.onDataNotAvailable(httpErrorEntity);
        }
    }

    /**
     * 上传文件
     *
     * @param url
     * @param file
     */

    public void doUploadFileMap(String url, HashMap<String, String> params, List<PostFormBuilder.FileInput> fileInputs, final DataCallback callback) {
        if (NetworkUtil.checkNetwork(BaseApplication.getAppContext())) {
            OkhttpUtils.post().url(url).addParams(params).build().execute(new StringCallback() {
                @Override
                public void onError(Request request, HttpErrorEntity httpErrorEntity) { // 获得response响应
                    callback.onDataNotAvailable(httpErrorEntity);
                    if (httpErrorEntity != null) {
                        LogUtils.i("onResponse：" + httpErrorEntity.toString());
                        LogUtils.i("request：" + request.toString() + "error:" + httpErrorEntity.getCode());
                    }
                }

                @Override
                public void onResponse(String response) {
                    LogUtils.i("onResponse：" + response.toString());
                    callback.onDataLoaded(response);
                }

            });
            String strParams = url;
            if (params != null) {
                strParams = strParams + "?";
                for (String key : params.keySet()) {
                    strParams = strParams + key + "=" + params.get(key) + "&";
                }
            }
            LogUtils.i("传参==" + strParams);
        } else {
            HttpErrorEntity httpErrorEntity = new HttpErrorEntity(-1, BaseApplication.getAppContext().getResources().getString(R.string.str_please_check_net), url, null);
            callback.onDataNotAvailable(httpErrorEntity);
        }
    }

    /**
     * get请求图片验证码
     *
     * @param params
     * @param dataCallback
     */
    public void doStringGetForImg(String url, HashMap<String, String> params, final ImgDataCallback callback) {
        if (NetworkUtil.checkNetwork(BaseApplication.getAppContext())) {
            OkhttpUtils.get().url(url)
                    .addHeader("tid", AppUtils.getSerialNumber())
                    .addParams(params).build().execute(new ImgCallback() {
                @Override
                public void onError(Request request, HttpErrorEntity httpErrorEntity) {
                    callback.onDataNotAvailable(httpErrorEntity);
                    if (httpErrorEntity != null) {
                        LogUtils.i("onResponse：" + httpErrorEntity.toString());
                        LogUtils.i("request：" + request.toString() + "error:" + httpErrorEntity.getCode());
                    }
                }

                @Override
                public void onResponse(Response response) {
                    callback.onDataLoaded(response);
                }
            });
            String strParams = url;
            if (params != null) {
                strParams = url + "?";
                for (String key : params.keySet()) {
                    strParams = strParams + key + "=" + params.get(key) + "&";
                }
            }
            LogUtils.i("传参==" + strParams);
        } else {
            HttpErrorEntity httpErrorEntity = new HttpErrorEntity(-1, BaseApplication.getAppContext().getResources().getString(R.string.str_please_check_net), url, null);
            callback.onDataNotAvailable(httpErrorEntity);
        }
    }


}
