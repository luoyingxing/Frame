package com.lyx.sample.frame.exception;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;


/**
 * CrashHandler
 * <p>
 * Autor: luoyingxing
 * Time: 2017/11/7 0007
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Context mContext;
    private static CrashHandler mInstance;
    private Thread.UncaughtExceptionHandler mDefaultHandler; // 系统默认的UncaughtException处理类

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (null == mInstance) {
            synchronized (CrashHandler.class) {
                if (mInstance == null) {
                    mInstance = new CrashHandler();
                }
            }
        }
        return mInstance;
    }

    /**
     * 异常处理初始化
     *
     * @param context Context
     */
    public void init(Context context) {
        this.mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        if (!handleException(e) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, e);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param e Throwable
     * @return 如果处理了该异常信息返回 true: 否则返回 false.
     */
    private boolean handleException(Throwable e) {
        if (null == e) {
            return false;
        }

        e.printStackTrace();
        final String message = e.getMessage();

        new Thread() {

            @Override
            public void run() {
                //TODO write exception in file.
                //message
                Looper.prepare();
                Toast.makeText(mContext, "遇见你是我的缘分^_^", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

        }.start();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        return true;
    }
}