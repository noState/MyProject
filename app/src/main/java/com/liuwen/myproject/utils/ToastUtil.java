package com.liuwen.myproject.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.liuwen.myproject.base.app.MyProjectApplication;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-08-09
 * <p>
 * <p>
 * <p>
 * 单例Toast
 * 上下文参数Context建议传递ApplicationContext(context.getApplication()或者全局Context,否则造成内存泄漏
 * <p>
 * TastyToast萌萌的Toast(有意思!)依赖于三方库 com.sdsmdg.tastytoast:tastytoast:0.1.1
 */

public class ToastUtil {

    private static Toast toast = null;

    /**
     * 顶部Toast样式
     *
     * @param applicationContext
     * @param string
     */
    public static void showTopToast(Context applicationContext, String string) {
        if (toast == null) {
            toast = Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, toast.getXOffset() / 2, toast.getYOffset() / 2);
            toast.show();
        } else {
            toast.setText(string);
            toast.show();
        }
    }

    /**
     * Toast   标准Toast样式
     *
     * @param applicationContext
     * @param content
     */
    public static void showNomalToast(Context applicationContext, String content) {
        if (toast == null) {
            toast = Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT);
            toast.show();
        }
        toast.setText(content);
        toast.show();
    }

    /**
     * 成功反馈TastyToast
     *
     * @param successMessage
     */
    public static void successTastyToast(String successMessage) {
        TastyToast.makeText(MyProjectApplication.getInstance(), successMessage, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
    }

    /**
     * 警告反馈TastyToast
     *
     * @param warningMessage
     */
    public static void warningTastyToast(String warningMessage) {
        TastyToast.makeText(MyProjectApplication.getInstance(), warningMessage, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
    }

    /**
     * 错误反馈astyToast
     *
     * @param errorMessage
     */
    public static void errorTastyToast(String errorMessage) {
        TastyToast.makeText(MyProjectApplication.getInstance(), errorMessage, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }

}
