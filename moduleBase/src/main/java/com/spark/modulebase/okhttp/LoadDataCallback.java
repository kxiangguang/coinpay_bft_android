package com.spark.modulebase.okhttp;


public interface LoadDataCallback {
    void onDataLoaded(String response, int requstCode);

    void onDataNotAvailable(Integer code, String toastMessage);

}
