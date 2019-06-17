package com.spark.coinpay.my.about;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.coinpay.base.BaseActivity;
import com.spark.coinpay.base.WebViewActivity;
import com.spark.coinpay.entity.VisionEntity;
import com.spark.coinpay.view.AppVersionDialog;
import com.spark.modulebase.entity.HttpErrorEntity;
import com.spark.modulebase.okhttp.FileCallback;
import com.spark.modulebase.okhttp.OkhttpUtils;
import com.spark.modulebase.utils.AppUtils;
import com.spark.modulebase.utils.FileUtils;
import com.spark.modulebase.utils.LogUtils;
import com.spark.modulebase.utils.SharedPreferencesUtil;
import com.spark.modulebase.utils.StringUtils;
import com.spark.modulebase.utils.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;

import static com.spark.modulebase.base.BaseConstant.ERROR_CODE;
import static com.spark.modulebase.base.BaseConstant.FAIL_CODE;
import static com.spark.modulebase.base.BaseConstant.SUCCESS_CODE;
import static com.spark.modulebase.utils.SharedPreferencesUtil.SP_KEY_APP_NAME;

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity implements AboutContract.AboutView {
    @BindView(R.id.tvVersionName)
    TextView tvVersionName;
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    @BindView(R.id.llVersion)
    LinearLayout llVersion;
    @BindView(R.id.llHelp)
    LinearLayout llHelp;

    private AboutPresenterImpl presenter;
    private ProgressDialog progressDialog;
    private AppVersionDialog appVersionDialog;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_about;
    }

    @Override
    protected void initView(Bundle saveInstance) {
        super.initView(saveInstance);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.str_about_us));
        initProgressDialog();
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new AboutPresenterImpl(this);
    }

    @Override
    protected void loadData() {
        super.loadData();
        String versionName = AppUtils.getVersionName(this);
        String appName = SharedPreferencesUtil.getInstance(activity).getStringParam(SP_KEY_APP_NAME);
        tvVersionName.setText(appName + "  " + "V" + versionName);
        tvVersion.setText("V" + versionName);
    }

    @OnClick({R.id.llHelp, R.id.llVersion})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llHelp://帮助中心
                Bundle bundle = new Bundle();
                bundle.putString(GlobalConstant.URL, GlobalConstant.HELP);
                bundle.putBoolean(GlobalConstant.NEEDTITLE, true);
                showActivity(WebViewActivity.class, bundle);
                break;
            case R.id.llVersion://版本更新
                checkPermission();
                break;
        }
    }

    @Override
    public void checkVersionSuccess(String response) {
        if (StringUtils.isNotEmpty(response)) {
            VisionEntity entity = new Gson().fromJson(response, VisionEntity.class);
            if (entity == null) {
                ToastUtils.showToast(activity, getString(R.string.str_version_new));
                return;
            }
            if (entity.getCode() == FAIL_CODE) {
                ToastUtils.showToast(activity, getString(R.string.str_version_new));
                return;
            }
            if (entity.getCode() == ERROR_CODE) {
                ToastUtils.showToast(activity, getString(R.string.str_version_new));
                return;
            }
            if (entity.getData() == null) {
                ToastUtils.showToast(activity, getString(R.string.str_version_new));
                return;
            }
            if (!(AppUtils.compareVersion(entity.getData().getVersion(), AppUtils.getVersionName(activity)) == 1)) {
                ToastUtils.showToast(activity, getString(R.string.str_version_new));
                return;
            }
//            entity = new VisionEntity();
//            VisionEntity.DataBean dataBean = new VisionEntity.DataBean();
//            dataBean.setVersion("1.0.1");
//            dataBean.setDownloadUrl("http://download.ahbc.cn/bcmp_app.apk");
//            dataBean.setRemark("1.优化已知问题\n2.测试部分");
//            entity.setData(dataBean);
            if (StringUtils.isNotEmpty(entity.getData().getUrl())) {
                showReleaseDialog(entity);
            } else {
                ToastUtils.showToast(activity, getString(R.string.update_address_error_tag));
            }

        } else {
            ToastUtils.showToast(activity, getString(R.string.str_version_new));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    /**
     * check权限
     *
     * @param position
     */
    private void checkPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.d("permission: " + data.get(0));
                        presenter.checkVersion();
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
     * 提示框
     */
    private void showReleaseDialog(final VisionEntity visionEntity) {
        appVersionDialog = new AppVersionDialog(activity, visionEntity);
        appVersionDialog.setPositiveOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visionEntity.getData().getUrl() == null || "".equals(visionEntity.getData().getUrl())) {
                    ToastUtils.showToast(activity, getString(R.string.update_address_error_tag));
                } else {
                    download(visionEntity.getData().getUrl());
                }
                appVersionDialog.dismiss();
            }
        });
        appVersionDialog.show();
    }

    private void download(String url) {
        OkhttpUtils.get().url(url).build().execute(new FileCallback(FileUtils.getCacheSaveFile(this, "application.apk").getAbsolutePath()) {
            @Override
            public void inProgress(float progress) {
                progressDialog.show();
                progressDialog.setProgress((int) (progress * 100));
            }

            @Override
            public void onError(Request request, HttpErrorEntity e) {
                progressDialog.dismiss();
                String msg = e.getMessage();
                LogUtils.e("下载download===============================================onError===msg==" + msg);
            }

            @Override
            public void onResponse(File response) {
                progressDialog.dismiss();
                AppUtils.installAPk(response, activity);
            }
        });
    }

    private void initProgressDialog() {
        //创建进度条对话框
        progressDialog = new ProgressDialog(this);
        //设置标题
        progressDialog.setTitle(getString(R.string.downloading_tag));
        //设置信息
        progressDialog.setMessage(getString(R.string.downloading_crazy_tag));
        progressDialog.setProgress(0);
        //设置显示的格式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }
}
