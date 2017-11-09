package com.liuwen.myproject.base.app;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MotionEvent;
import android.view.View;

import com.liuwen.myproject.utils.ToastUtil;
import com.liuwen.myproject.utils.httpUtil.ApiException;
import com.liuwen.myproject.utils.httpUtil.RxBus;
import com.liuwen.myproject.utils.httpUtil.SucceedSubscribe;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-24
 * <p>
 * Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    private CompositeSubscription compositeSubscription;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //预初始化
        prepareInit();
    }

    /**
     * 预初始化
     */
    private void prepareInit() {
        //初始化友盟
        initUmeng();
        //横竖屏设定
        orientation();
        //Activity管理
        activityManager();
        //异常处理
        dealWithError();
    }

    /**
     * 初始化友盟
     */
    private void initUmeng() {
        PushAgent.getInstance(this).onAppStart();
    }

    /**
     * 沉浸式状态栏
     */
    protected void immersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

    /**
     * 添加当前Activity子类到ActivityManager
     */
    private void activityManager() {
        ActivityManager.addActivity(this, getClass());
    }

    /**
     * 手机屏幕设置为竖屏
     */
    private void orientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 异常处理
     */
    private void dealWithError() {
        Subscription subscribe = RxBus.getDefault().toObservable(Throwable.class).subscribe(new SucceedSubscribe<Throwable>() {
            @Override
            public void onNext(Throwable e) {
                super.onNext(e);
                if (e instanceof ApiException) {
                    //服务器内部错误
                    ApiException apiException = (ApiException) e;
                    ToastUtil.errorTastyToast(apiException.ResponseMessageError);
                } else {
                    //其他异常
                    String localizedMessage = e.getLocalizedMessage();
                    switch (localizedMessage) {
                    }
                }
            }

        });
        addSubscription(subscribe);
    }

    /**
     * 进程异常
     */
    public void forceExceptionOp() {

    }

    public void addSubscription(Subscription subscription) {
        if (subscription == null) {
            return;
        }
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
//        Bugtags.onResume(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
//        Bugtags.onPause(this);
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
//        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription.clear();
        }
        ActivityManager.removeActivity(this);
        super.onDestroy();
    }
}
