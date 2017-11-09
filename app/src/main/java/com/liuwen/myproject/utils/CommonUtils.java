package com.liuwen.myproject.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liuwen.myproject.base.constant.Constant;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-24
 */

public class CommonUtils {

    //按钮重复点击有效时长
    private static int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;
    private static int lastViewId;
    private static int mStatusHeight = -1;

    /**
     * 手机号码,中间4位星号替换
     *
     * @param phone 手机号
     * @return
     */
    public static String phoneToHide(String phone) {
        // 括号表示组，被替换的部分$n表示第n组的内容
        // 正则表达式中，替换字符串，括号的意思是分组，在replace()方法中，
        // 参数二中可以使用$n(n为数字)来依次引用模式串中用括号定义的字串。
        // "(\d{3})\d{4}(\d{4})", "$1****$2"的这个意思就是用括号，
        // 分为(前3个数字)中间4个数字(最后4个数字)替换为(第一组数值，保持不变$1)(中间为*)(第二组数值，保持不变$2)
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 银行卡号,保留最后4位,其他星号替换
     *
     * @param cardId 卡号
     * @return
     */
    public static String cardIdToHide(String cardId) {
        return cardId.replaceAll("\\d{15}(\\d{3})", "**** **** **** **** $1");
    }

    /**
     * 获取当前屏幕截图,不包含状态栏
     *
     * @param activity
     * @return bp
     */
    public static Bitmap getSnapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        if (bmp == null) {
            return null;
        }
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Bitmap bitmap = Bitmap.createBitmap(bmp, 0, statusBarHeight, bmp.getWidth(), bmp.getHeight() - statusBarHeight);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return mStatusHeight
     */
    public static int getStatusHeight(Context context) {
        if (mStatusHeight != -1) {
            return mStatusHeight;
        }
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                mStatusHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStatusHeight;
    }

    /**
     * 获取当前应用程序的VersionCode
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 100;
    }

    /**
     * 获取当前应用程序VersionName
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName.split("-")[0];
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    /**
     * Activity跳转
     *
     * @param context
     * @param clazz
     */
    public static void startActivity(Context context, Class<? extends Activity> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    /**
     * 重复/快速点击事件的判断
     *
     * @param viewId
     * @return
     */
    public static boolean isFastDoubleClick(int viewId) {
        boolean flag;
        long currentTime = System.currentTimeMillis();
        if (viewId == lastViewId) {
            if ((currentTime - lastClickTime) > MIN_CLICK_DELAY_TIME) {
                flag = false;
            } else {
                flag = true;
            }
            lastViewId = viewId;
            lastClickTime = currentTime;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * Log文件
     *
     * @return
     */
    public static File getLogFolder() {
        File file = new File(Constant.LOG_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 程序运行环境？真机:模拟器
     */
    private static boolean isRunOnVirtual() {
        if (Build.MODEL.contains("sdk") || Build.MODEL.contains("SDK")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 收集设备信息
     *
     * @return
     */
    public static String collectDeviceInfo() {
        StringBuilder deviceInfo = new StringBuilder("Product: " + Build.PRODUCT);
        deviceInfo.append(", DEVICE: " + Build.DEVICE);
        deviceInfo.append(", MODEL: " + Build.MODEL);
        deviceInfo.append(", CPU_ABI: " + Build.CPU_ABI);
        deviceInfo.append(", VERSION.RELEASE: " + Build.VERSION.RELEASE);
        deviceInfo.append(", SDK: " + Build.VERSION.SDK_INT);
        deviceInfo.append(", DISPLAY: " + Build.DISPLAY);
        deviceInfo.append(", VERSION_CODES.BASE: " + Build.VERSION_CODES.BASE);
        deviceInfo.append(", ID: " + Build.ID);
        deviceInfo.append(", TAGS: " + Build.TAGS);

        Logger.d("Product: " + Build.PRODUCT + "\n"
                + "DEVICE: " + Build.DEVICE + "\n"
                + "MODEL: " + Build.MODEL + "\n"
                + "CPU_ABI: " + Build.CPU_ABI + "\n"
                + "VERSION.RELEASE: " + Build.VERSION.RELEASE + "\n"
                + "SDK: " + Build.VERSION.SDK_INT + "\n"
                + "DISPLAY: " + Build.DISPLAY + "\n"
                + "VERSION_CODES.BASE: " + Build.VERSION_CODES.BASE + "\n"
                + "ID: " + Build.ID + "\n"
                + "TAGS: " + Build.TAGS);
        return deviceInfo.toString();

    }

    /**
     * 获取当前设备机型
     *
     * @return
     */
    public static String getDeviceName() {
        return Build.MODEL;
    }


    /**
     * 获取当前设备系统SDK版本
     *
     * @return
     */
    public static int getDeviceVersionSDK() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取当前设备的系统版本
     *
     * @return
     */
    public static String getDeviceVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 从资源图片中获取Bitmap
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap getBitmapFromResource(Context context, int resId) {
        Resources resources = context.getResources();
        return BitmapFactory.decodeResource(resources, resId);
    }

    /**
     * 验证手机号码格式是否正确
     *
     * @param mobiles
     * @return
     */
    public static boolean isPhoneNumber(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            mobiles = mobiles.replaceAll("\\s*", "");
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     * 移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     * 联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）
     * 电信的号段：133、153、180（未启用）、189
     *
     * @param mobile 移动、联通、电信运营商的号码段
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobileNum(String mobile) {
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证6位数字手机验证码格式是否正确
     *
     * @param code
     * @return
     */
    private static boolean isCode(String code) {
        String codeRegex = "\\d{6}";
        if (TextUtils.isEmpty(code))
            return false;
        else
            return code.matches(codeRegex);
    }

    /**
     * 判断字符串中是否有字母
     *
     * @param cardNum
     * @return
     */
    public static boolean hasLetter(String cardNum) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }

    /**
     * 判断字符串中是否有数字
     *
     * @param content
     * @return
     */
    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 6~16位数字与字母组合
     *
     * @param pwd
     * @return
     */
    public static boolean isPassword(String pwd) {
        String check = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        return !TextUtils.isEmpty(pwd) && pwd.matches(check);
    }

    /**
     * 结果显示 1,896.98
     *
     * @param number
     * @return
     */
    public static String numberFormat(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        double d = Double.valueOf(number);
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(d);
    }

    /**
     * 获取name对应的AndroidManifest中的meta-data值
     * 比如:获取当成程序的渠道名
     *
     * @param context
     * @param name
     * @return
     */
    public static String getMetaData(Context context, String name) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo == null) {
                return "";
            } else {
                Bundle bundle = applicationInfo.metaData;
                if (bundle == null) {
                    return "";
                }
                String result = bundle.getString(name);
                if (result == null) {
                    return "";
                } else {
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * (1)判断QQ是否可用
     * (2)QQ客服
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
