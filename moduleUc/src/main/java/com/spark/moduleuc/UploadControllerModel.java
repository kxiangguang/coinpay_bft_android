package com.spark.moduleuc;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spark.library.uc.api.UploadControllerApi;
import com.spark.library.uc.model.MessageResult;
import com.spark.library.uc.model.OssUploadEntity;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.callback.ResponseCallBack;

import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;

/**
 * 上传-uc
 */

public class UploadControllerModel {
    private UploadControllerApi uploadControllerApi;

    public UploadControllerModel() {
        uploadControllerApi = new UploadControllerApi();
        uploadControllerApi.setBasePath(UcUrls.getInstance().getHost());
    }

    /**
     * 上传base64位
     *
     * @param base64
     */
    public void uploadBase64Pic(final String base64, final ResponseCallBack.SuccessListener<String> successListener, final ResponseCallBack.ErrorListener errorListener) {
        final OssUploadEntity ossUploadEntity = new OssUploadEntity();
        ossUploadEntity.setBase64Data(base64);
        ossUploadEntity.setOss(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadControllerApi.base64UpLoadUsingPOST(ossUploadEntity, new Response.Listener<MessageResult>() {
                    @Override
                    public void onResponse(MessageResult response) {
                        int code = response.getCode();
                        if (code == SUCCESS_CODE) {
                            if (successListener != null)
                                successListener.onResponse((String) response.getData());
                        } else {
                            if (errorListener != null)
                                errorListener.onErrorResponse(new HttpErrorEntity(response.getCode(), response.getMessage(), response.getUrl(), response.getCid()));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (errorListener != null)
                            errorListener.onErrorResponse(error);
                    }
                });
            }
        }).start();
    }

}
