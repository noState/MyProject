package com.liuwen.myproject.modules.h5.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.liuwen.myproject.views.AdvancedWebView;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-08-01
 * <p>
 * (1) 实现源生与H5界面的交互
 * <p>
 * (2) 一般情况下,绝大部分H5链接在这处理,对于要求性比较高的链接可以在X5WebViewActivity中处理
 * <p>
 * (3) 使用X5内核,注意SDK的混淆,与安全浏览模式的开启
 */

public class WebViewActivity extends BaseActivity implements AdvancedWebView.Listener {

    @BindView(R.id.iv_Back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_Subhead)
    TextView tvSubhead;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    AdvancedWebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private String getIntentUrl = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        // ButterKnife
        ButterKnife.bind(this);
        // 初始化
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        // 初始化WebView
        initWebView();
        // 点击事件
        onClickEvent();
    }

    /**
     * 点击事件
     */
    private void onClickEvent() {
        // 返回键
        RxView.clicks(ivBack)
                .throttleFirst(Constant.DURATION, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> closePage());
    }

    /**
     * 初始化WebView
     */
    private void initWebView() {
        initWebViewSetting();
        initWebViewClient();
        initWebChromeClient();
        getUrl();
        Logger.d("H5链接 >>> " + getIntentUrl);
        webView.loadUrl(getIntentUrl);
//        webView.postUrl(getIntentUrl.toString(), EncodingUtils.getBytes(getIntentUrl.toString(), "UTF-8"));
    }

    /**
     * 获取url
     *
     * @return
     */
    public void getUrl() {
        getIntentUrl = getIntent().getStringExtra(IntentKey.INTENT_TO_WEBVIEWACT_URL);
    }

    /**
     * 初始化 WebChromeClient
     */
    private void initWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient() {

            private Bitmap mDefaultVideoPoster;

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Logger.d(title);
                tvTitle.setText(title);
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

    /**
     * 初始化 WebViewClient
     */
    private void initWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {
            // TODO 该方法可对前端界面url进行拦截,实现源生与前端H5界面的数据互换
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.d("WebViewClient_URL >>> " + url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 错误链接访问错误的全局化链接
                view.loadUrl("file:///android_asset/404.html");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                //handler.cancel(); // Android默认的处理方式
                //handleMessage(Message msg); // 进行其他处理
                handler.proceed();  // 接受所有网站的证书
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (webView != null && webView.getProgress() == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }

        });
    }

    /**
     * 初始化 WebViewSetting
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSetting() {
        webView.setListener(this, this);
        WebSettings mSettings = webView.getSettings();
        // 支持获取手势焦点
        webView.requestFocusFromTouch();
        webView.setHorizontalFadingEdgeEnabled(true);
        webView.setVerticalFadingEdgeEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        // 支持JS
        mSettings.setJavaScriptEnabled(true);
        mSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mSettings.setBuiltInZoomControls(true);
        mSettings.setDisplayZoomControls(true);
        mSettings.setLoadWithOverviewMode(true);
        // 支持插件
        mSettings.setPluginState(WebSettings.PluginState.ON);
        mSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 自适应屏幕
        mSettings.setUseWideViewPort(true);
        mSettings.setLoadWithOverviewMode(true);
        // 支持缩放
        mSettings.setSupportZoom(false);
        // 隐藏原声缩放控件
        mSettings.setDisplayZoomControls(false);
        // 支持内容重新布局
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mSettings.supportMultipleWindows();
        mSettings.setSupportMultipleWindows(true);
        // 设置缓存模式
        mSettings.setDomStorageEnabled(true);
        mSettings.setDatabaseEnabled(true);
        mSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mSettings.setAppCacheEnabled(true);
        mSettings.setAppCachePath(webView.getContext().getCacheDir().getAbsolutePath());
        // 设置可访问文件
        mSettings.setAllowFileAccess(true);
        mSettings.setNeedInitialFocus(true);
        // 支持自定加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            mSettings.setLoadsImagesAutomatically(true);
        } else {
            mSettings.setLoadsImagesAutomatically(false);
        }
        mSettings.setNeedInitialFocus(true);
        // 设定编码格式
        mSettings.setDefaultTextEncodingName("UTF-8");
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        progressBar.setVisibility(View.VISIBLE);
        tvTitle.setText("努力加载中...");
    }

    @Override
    public void onPageFinished(String url) {
        progressBar.setVisibility(View.GONE);
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Logger.d(url);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        webView.loadUrl("file:///android_asset/404.html");
        tvTitle.setText("访问错误！！！");
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webView.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {
        closePage();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        destroyWebView();
        super.onDestroy();

    }

    private void destroyWebView() {
        if (webView != null) {
            webView.stopLoading();
            webView.removeListener();
            webView.onDestroy();
            webView = null;
        }
    }

    /**
     * 销毁H5页面
     */
    private void closePage() {
        MainActivity activity = ActivityManager.getActivity(MainActivity.class);
        if (activity == null) {
            CommonUtils.startActivity(this, MainActivity.class);
        }
        finish();
    }
}
