package com.spark.coinpay.base;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.spark.coinpay.GlobalConstant;
import com.spark.coinpay.R;
import com.spark.modulebase.utils.StringUtils;

import butterknife.BindView;

/**
 * H5界面
 */
public class WebViewActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
    //H5地址
    private String CONSTANT_URL = "";
    //是否显示头部
    private boolean isNeedTitle = false;

    protected int getActivityLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ivBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView(Bundle saveInstance) {
        initWebview();
    }

    @Override
    protected void loadData() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(GlobalConstant.URL)) {
                CONSTANT_URL = bundle.getString(GlobalConstant.URL);
                if (StringUtils.isNotEmpty(CONSTANT_URL)) {
                    webView.loadUrl(CONSTANT_URL);
                }
            }
            if (bundle.containsKey(GlobalConstant.NEEDTITLE)) {
                isNeedTitle = bundle.getBoolean(GlobalConstant.NEEDTITLE, false);
            }
        }
        if (!isNeedTitle) {
            llTitle.setVisibility(View.GONE);
        } else {
            llTitle.setVisibility(View.VISIBLE);
        }
    }

    private void initWebview() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        /**
         *  Webview在安卓5.0之前默认允许其加载混合网络协议内容
         *  在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
         *   解决图片不显示问题
         */
        webSettings.setBlockNetworkImage(false); // 解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.LOAD_NORMAL);
        }

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

        //与H5交互
        webView.addJavascriptInterface(this, "android");
        webView.setBackgroundColor(getResources().getColor(R.color.white));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //showLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!StringUtils.isEmpty(view.getTitle())) {
                    if (view.getTitle().length() > 10) {
                        tvTitle.setText(view.getTitle().substring(0, 10) + "...");
                    } else {
                        tvTitle.setText(view.getTitle());
                    }
                }
                //hideLoading();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (webView != null)
            webView.destroy();
        super.onDestroy();
    }

}
