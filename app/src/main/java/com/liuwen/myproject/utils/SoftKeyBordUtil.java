package com.liuwen.myproject.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-08-10
 * <p>
 * <p>
 * <p>
 * 软件盘状态
 */

public class SoftKeyBordUtil {

    /**
     * 隐藏系统软键盘
     *
     * @param context
     */
    public static void closeSoftKeyBord(Activity context) {
        boolean softKeyBordHide = isFoftKeyBordHide(context);
        if (softKeyBordHide) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            View currentFocus = context.getCurrentFocus();
            if (currentFocus != null) {
                imm.hideSoftInputFromWindow(currentFocus.getApplicationWindowToken(), 0);
            }
        }
    }

    /**
     * 打开软键盘
     *
     * @param editText
     */
    public static void openSoftKeyBord(EditText editText) {
        if (editText == null) {
            return;
        }
        //设置可获得焦点
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        //请求获得焦点
        editText.requestFocus();
        //调用系统输入法
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    /**
     * 显示软键盘
     *
     * @param activity
     */
    public static void showSoftBord(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 判断软键盘是否打开
     *
     * @param context
     * @return true 打开
     */
    public static boolean isFoftKeyBordHide(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

}
