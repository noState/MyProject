package com.liuwen.myproject.modules.h5.X5;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.liuwen.myproject.R;
import com.liuwen.myproject.base.app.ActivityManager;
import com.liuwen.myproject.base.app.BaseActivity;
import com.liuwen.myproject.base.constant.Constant;
import com.liuwen.myproject.base.constant.IntentKey;
import com.liuwen.myproject.modules.MainActivity;
import com.liuwen.myproject.utils.CommonUtils;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-09-06
 */

public class X5WebViewActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subhead)
    TextView tvSubhead;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private String getIntent_Url = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_x5webview);
        ButterKnife.bind(this);
        // 初始化X5WebView
        initWebView();
        // 点击事件
        onClick();
    }

    /**
     * 点击事件
     */
    private void onClick() {
        //返回
        RxView.clicks(ivBack)
                .throttleFirst(Constant.DURATION, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    closePage();
                });

        RxView.clicks(tvReturn)
                .throttleFirst(Constant.DURATION, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                });
    }

    private void initWebView() {
        initWebSettings();
        initWebViewClient();
        initWebChromeClient();
        getUrl();
        webView.loadUrl(getIntent_Url);
        Logger.d("WebView Core >>> " + webView.getX5WebViewExtension());
    }

    private void initWebSettings() {
        WebSettings mSettings = webView.getSettings();
        webView.requestFocusFromTouch();
        mSettings.setJavaScriptEnabled(true);
        mSettings.setPluginState(WebSettings.PluginState.ON);
        // mSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mSettings.setUseWideViewPort(true);
        mSettings.setLoadWithOverviewMode(true);
        mSettings.setSupportZoom(false);
        mSettings.setDisplayZoomControls(false);
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mSettings.supportMultipleWindows();
        mSettings.setSupportMultipleWindows(true);
        mSettings.setDomStorageEnabled(true);
        mSettings.setDatabaseEnabled(true);
        mSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mSettings.setAppCacheEnabled(true);
        mSettings.setAppCachePath(webView.getContext().getCacheDir().getAbsolutePath());
        mSettings.setAllowFileAccess(true);
        mSettings.setNeedInitialFocus(true);
        if (Build.VERSION.SDK_INT >= 19) {
            mSettings.setLoadsImagesAutomatically(true);
        } else {
            mSettings.setLoadsImagesAutomatically(false);
        }
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
        mSettings.setNeedInitialFocus(true);
        mSettings.setDefaultTextEncodingName("UTF-8");
        webView.addJavascriptInterface(new InJavaScript(), "android");
    }

    private void initWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView webView, String s) {
                super.onLoadResource(webView, s);
                if (webView != null && webView.getProgress() == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                progressBar.setVisibility(View.VISIBLE);
                tvTitle.setText("努力加载中...");
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                progressBar.setVisibility(View.GONE);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return super.shouldOverrideUrlLoading(webView, s);
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                webView.loadUrl("file:///android_asset/404.html");
                tvTitle.setText(s);
            }

            @Override
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                //handler.cancel(); // Android默认的处理方式
                //handleMessage(Message msg); // 进行其他处理
                sslErrorHandler.proceed();  // 接受所有网站的证书
            }
        });
    }

    /**
     * 获取url
     */
    public void getUrl() {
        getIntent_Url = getIntent().getStringExtra(IntentKey.INTENT_TO_X5WEBVIEWACT_URL);
    }

    /**
     * 初始化 WebChromeClient
     */
    private void initWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient() {
            private Bitmap mDefaultVideoPoster;

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                Logger.d(s);
                tvTitle.setText(s);
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                if (mDefaultVideoPoster == null) {
                    mDefaultVideoPoster = BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo);
                    return mDefaultVideoPoster;
                }
                return super.getDefaultVideoPoster();
            }
        });
    }

    @SuppressLint("JavascriptInterface")
    private class InJavaScript {
    }

    @Override
    protected void onDestroy() {
        destroyWebView();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        closePage();
        super.onBackPressed();
    }

    private void closePage() {
        MainActivity activity = ActivityManager.getActivity(MainActivity.class);
        if (activity == null) {
            CommonUtils.startActivity(this, MainActivity.class);
        }
        finish();
    }

    private void destroyWebView() {
        if (webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
            webView.pauseTimers();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }
}
