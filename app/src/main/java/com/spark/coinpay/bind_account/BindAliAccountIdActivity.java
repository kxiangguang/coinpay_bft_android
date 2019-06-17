package com.spark.coinpay.bind_account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.BitmapUtils;
import com.spark.modulebase.utils.DateUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 获取支付宝收款id
 */
public class BindAliAccountIdActivity extends BaseActivity {

    @BindView(R.id.ivCode)
    ImageView ivCode;
    @BindView(R.id.tvOpenAliPay)
    TextView tvOpenAliPay;
    private String codeAliUrl = "alipays://platformapi/startapp?appId=20000691&url=https://render.alipay.com/p/f/fd-ixpo7iia/index.html";
    private String codeUrl2 = "https://render.alipay.com/p/f/fd-ixpo7iia/index.html";

    private Bitmap saveBitmap;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_ali_account_id;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_ali_account_id_title));
        String htm = "请打开<font color=#4c7af3>支付宝</font>获取收款id，并复制填入到文本框进行保存";
        tvOpenAliPay.setText(Html.fromHtml(htm));
    }

    @Override
    protected void initData() {
        super.initData();
        ivCode.post(new Runnable() {
            @Override
            public void run() {
                saveBitmap = AppUtils.createQRCode(codeUrl2, Math.min(ivCode.getWidth(), ivCode.getHeight()));
                ivCode.setImageBitmap(saveBitmap);
            }
        });
    }

    @OnClick({R.id.ivCode, R.id.tvOpenAliPay})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivCode://保存二维码
                checkPermission();
                break;
            case R.id.tvOpenAliPay://打开支付宝
                if (AppUtils.isInstalled(activity, "com.eg.android.AlipayGphone")) {
                    skipScheme(activity, codeAliUrl);
                } else {
                    ToastUtils.showToast("未找到支付宝客户端");
                }
                break;
        }
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
                        save();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (AndPermission.hasAlwaysDeniedPermission(activity, data)) {
                            AndPermission.permissionSetting(activity).execute();
                            return;
                        }
                        ToastUtils.showToast(activity, getString(com.spark.modulebase.R.string.str_no_permission));
                    }
                }).start();
    }

    /**
     * 保存到相册
     */
    private void save() {
        String time = DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, new Date());
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName() + "/" + time + ".jpg");
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            if (saveBitmap != null) {
                BitmapUtils.saveBitmapToFile(saveBitmap, file, 100);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (file != null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            ToastUtils.showToast(activity, getString(R.string.str_save_success));
        }
    }

    /**
     * 跳转支付宝
     *
     * @param context
     * @param newurl
     * @return
     */
    public static boolean skipScheme(Context context, String newurl) {
        try {
            // 以下固定写法
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newurl));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            List<ResolveInfo> resolves = context.getPackageManager().queryIntentActivities(intent, 0);
            if (resolves.size() > 0) {
                ((Activity) context).startActivityIfNeeded(intent, -1);
            }
        } catch (Exception e) {
            // 防止没有安装的情况
            ToastUtils.showToast("未找到支付宝客户端");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static class DealedUrl {
        public String url;
        public String params;
    }

    //alipays://platformapi/startapp?appId=20000691&url=https://render.alipay.com/p/f/fd-ixpo7iia/index.html
    public static DealedUrl dealUrl(String url) {
        DealedUrl dealedUrl = new DealedUrl();
        if (!url.contains("?")) {
            dealedUrl.url = url;
            dealedUrl.params = "";
            return dealedUrl;
        }
        String params = url.substring(url.indexOf("?") + 1);
        dealedUrl.url = url.substring(0, url.indexOf("?"));
        String[] results = params.split("&");
        StringBuilder specialParams = new StringBuilder();//该url特有参数
        for (String str : results) {
            if (str.split("=").length != 2) {
                continue;
            }
            String key = str.split("=")[0];
            specialParams.append(str).append("&");
        }
        if (specialParams.length() > 0) {
            specialParams.deleteCharAt(specialParams.length() - 1);
        }
        dealedUrl.params = specialParams.toString();
        return dealedUrl;
    }

    public static TreeMap<String, String> getMapFromString(String data) {
        TreeMap<String, String> reqMap = new TreeMap<>();
        if (TextUtils.isEmpty(data)) {
            return reqMap;
        }
        String[] array = data.split("&");
        for (String entry : array) {
            String[] parts = entry.split("=");
            if (parts.length < 2) {
                continue;
            }
            reqMap.put(parts[0], parts[1]);
        }
        return reqMap;
    }

}
