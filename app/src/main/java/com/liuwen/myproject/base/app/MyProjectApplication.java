package com.liuwen.myproject.base.app;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.liuwen.myproject.base.constant.Constant;
import com.liuwen.myproject.modules.MainActivity;
import com.liuwen.myproject.modules.forthpage.activities.EventTestActivity;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.List;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-24
 */

public class MyProjectApplication extends Application {

    public static Context sApplicationContext;

    public static Context getInstance() {
        return sApplicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 预初始化
        prepareInit();
    }

    /**
     * 预初始化
     */
    private void prepareInit() {
        long startTime = System.currentTimeMillis();
        String processName = getMProcessName(this);
        if (!TextUtils.isEmpty(processName)) {
            if (processName.equals("com.liuwen.myproject")) {
                // 初始化日志打印Logger
                initLogger();
                // 初始化上下文
                sApplicationContext = this;
                // LeakCanary内存泄漏检测
                initLeakCanary();
                // 异常处理
                initCrashManager();
                // 初始化BugTags
                initBugtags();
                // 初始化X5内核
                initX5();
            }
            //初始化友盟
            initUmeng();
            long endTime = System.currentTimeMillis();
            Logger.d("Application onCreate Task >>> " + (endTime - startTime) + " milliseconds");
        }
    }

    /**
     * 初始化友盟推送
     */
    private void initUmeng() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(Constant.IS_DEBUG);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Logger.d("UmengPush RegisterSuccess! DeviceToken >>> " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Logger.d("UmengPush RegisterFail! >>> " + s.toString() + " --- " + s1.toString());
            }
        });
        //自定义通知栏打开行为
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                //捕捉自定义跳转的消息
                showUmengCustomMsg(msg);
                switch (msg.custom) {
                    case "goto_EventText":
                        startActivity(new Intent(context, EventTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;
                    default:
                        startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;
                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    /**
     * 初始化腾讯X5内核
     */
    private void initX5() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // 5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核
                Logger.d("X5 Core LoadSucceed >>> " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        // X5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    /**
     * 初始化BugTags
     */
    private void initBugtags() {
//        Bugtags.start("31659bb7592a98f80557330025218c54", this, Constant.IS_DEBUG ? Bugtags.BTGInvocationEventBubble : Bugtags.BTGInvocationEventNone);
    }

    /**
     * 初始化日志打印Logger
     */
    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(0)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("NameOfProject")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return Constant.IS_DEBUG;
            }
        });
    }

    /**
     * 异常处理
     */
    private void initCrashManager() {
        CrashManager.getInstance().init(this);
    }

    /**
     * 内存泄漏检测
     */
    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

    /**
     * 获取当前进程信息
     *
     * @param context
     * @return
     */
    @Nullable
    private String getMProcessName(Context context) {
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (android.app.ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    /**
     * MultiDex方法树处理
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex方法树
        MultiDex.install(this);
    }

    /**
     * Umeng 捕捉自定义跳转的消息
     *
     * @param msg
     */
    private void showUmengCustomMsg(UMessage msg) {
        Logger.d("message_id:" + msg.message_id + "\n"
                + "activity:" + msg.activity + "\n"
                + "after_open:" + msg.after_open + "\n"
                + "alias:" + msg.alias + "\n"
                + "custom:" + msg.custom + "\n"
                + "display_type:" + msg.display_type + "\n"
                + "icon:" + msg.icon + "\n"
                + "img:" + msg.img + "\n"
                + "largeIcon:" + msg.largeIcon + "\n"
                + "msg_id:" + msg.msg_id + "\n"
                + "pulled_package:" + msg.pulled_package + "\n"
                + "pulled_service:" + msg.pulled_service + "\n"
                + "sound:" + msg.sound + "\n"
                + "task_id:" + msg.task_id + "\n"
                + "text:" + msg.text + "\n"
                + "ticker:" + msg.ticker + "\n"
                + "title:" + msg.title + "\n"
                + "url:" + msg.url + "\n"
                + "builder_id" + msg.builder_id + "\n"
                + "clickOrDismiss:" + msg.clickOrDismiss + "\n"
                + "extra:" + msg.extra + "\n"
                + "getRaw:" + msg.getRaw() + "\n"
                + "hasResourceFromInternet:" + msg.hasResourceFromInternet() + "\n"
                + "isLargeIconFromInternet:" + msg.isLargeIconFromInternet() + "\n"
                + "isSoundFromInternet:" + msg.isSoundFromInternet() + "\n"
                + "play_lights:" + msg.play_lights + "\n"
                + "play_vibrate:" + msg.play_vibrate + "\n"
                + "play_sound:" + msg.play_sound + "\n");
    }
}
