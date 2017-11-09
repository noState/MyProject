package com.liuwen.myproject.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;

import com.liuwen.myproject.base.app.ActivityManager;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-25
 * <p>
 * <p>
 * <p>
 * 设定时间内双击 程序退出工具
 */

public class DoubleClickExitAppUtil {

    private final Activity mActivity;
    private boolean isOnKeyBacking;
    private Handler mHandler;
    private View mView;
    private Snackbar mSnackbar;

    public DoubleClickExitAppUtil(Activity activity, View view) {
        mActivity = activity;
        mView = view;
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Activity onKeyDown事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false;
        }
        if (isOnKeyBacking) {
            mHandler.removeCallbacks(onBackTimeRunnable);
            AppExit(mActivity);
            return true;
        } else {
            isOnKeyBacking = true;
            if (mSnackbar == null) {
                mSnackbar = Snackbar.make(mView, "再单击一次返回 退出程序", Snackbar.LENGTH_SHORT);
            }
            mSnackbar.show();
            //延迟两秒的时间
            mHandler.postDelayed(onBackTimeRunnable, 2000);
            return true;
        }
    }

    private Runnable onBackTimeRunnable = new Runnable() {
        @Override
        public void run() {
            isOnKeyBacking = false;
        }
    };

    /**
     * App退出操作
     *
     * @param context
     */
    public void AppExit(Context context) {
        try {
            ActivityManager.removeAllActivity();
            android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            //结束整个程序 所有组件
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {

        }
    }
}
