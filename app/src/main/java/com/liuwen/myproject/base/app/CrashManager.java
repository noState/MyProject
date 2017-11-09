package com.liuwen.myproject.base.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.liuwen.myproject.modules.MainActivity;
import com.liuwen.myproject.utils.CommonUtils;
import com.orhanobut.logger.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Liuwen (WX:LW993209378).
 * <p>
 * <p>
 * <p>
 * on 2017-08-01
 * 系统异常处理(程序上线)
 */

public class CrashManager implements Thread.UncaughtExceptionHandler {

    // 系统默认异常处理器
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;
    private Context mContext;
    private PendingIntent restartIntent;

    private CrashManager() {
    }

    private static CrashManager sMCrashManager = new CrashManager();

    public static CrashManager getInstance() {
        return sMCrashManager;
    }


    public void init(Context context) {
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context;
        // TODO 异常默认启动窗口
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        restartIntent = PendingIntent.getActivity(context.getApplicationContext(), -1, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    /**
     * 当程序出现未知异常调用此方法
     *
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //读取崩溃的stacktrace信息，然后上报到服务器数据便于开发者分析
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        String errorReport = result.toString();
        Logger.e("uncaughtException errorReport=" + errorReport);
        if (!handleException(ex) && mDefaultUncaughtExceptionHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
        } else {
            // 重启应用
//            restartApp();
        }
    }

    /**
     * 提示一下自己的软件问题
     * 收集设备以及异常信息，发送到后端，记录到日志
     *
     * @param ex
     */
    private void handlerException(Throwable ex) {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "抱歉,系统出现异常", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        // 手机设备信息及错误信息
        crashInfo(ex);
        // 退出程序 关闭所有activity
        ActivityManager.removeAllActivity();
        // 关闭软件进程
        android.os.Process.killProcess(android.os.Process.myPid());
        // 关闭虚拟机,释放所有内存
        System.exit(0);
    }

    private void crashInfo(Throwable ex) {
        final String deviceInfo = Build.DEVICE + "......" + Build.BRAND + "......" + Build.MODEL + "......" + Build.VERSION.SDK_INT;
        final String errorInfo = ex.getMessage();
        // 发送到服务器
        new Thread() {
            @Override
            public void run() {
                Logger.d(deviceInfo + "<<<>>>" + errorInfo);
            }
        }.start();
    }


    public void finishProgram() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void restartApp() {
        Logger.d("here restart app");
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
        ActivityManager.removeAllActivity();
        finishProgram();
    }

    private void writeLog(String log, String path) {
        CharSequence timestamp = DateFormat.format("yyyyMMdd_kkmmss", System.currentTimeMillis());
        File f = new File(path, "_" + timestamp + ".log");

        FileOutputStream stream;
        BufferedWriter bw = null;
        OutputStreamWriter output = null;
        try {
            stream = new FileOutputStream(f);
            output = new OutputStreamWriter(stream);
            bw = new BufferedWriter(output);
            bw.write(log);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (bw != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //保存日志文件
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);

        // Inject some info about android version and the device, since google can't provide them in the developer console
        StackTraceElement[] trace = ex.getStackTrace();
        StackTraceElement[] trace2 = new StackTraceElement[trace.length + 3];
        System.arraycopy(trace, 0, trace2, 0, trace.length);
        trace2[trace.length] = new StackTraceElement("Android", "MODEL", Build.MODEL, -1);
        trace2[trace.length + 1] = new StackTraceElement("Android", "VERSION", Build.VERSION.RELEASE, -1);
        trace2[trace.length + 2] = new StackTraceElement("Android", "FINGERPRINT", Build.FINGERPRINT, -1);
        ex.setStackTrace(trace2);

        ex.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            writeLog(stacktrace, CommonUtils.getLogFolder().getAbsolutePath());
        }
        return true;
    }

}
