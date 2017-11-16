package com.lyx.frame.utils;

import android.util.Log;

import com.lyx.frame.BuildConfig;


/**
 * Logger
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 */
public class Logger {
    private static final String mPrefix = "===>>>";
    private String mTag;
    private int mPriority;

    /**
     * 自定义Log类,可单独控制每一个类的debug级别。
     * <p/>
     * 当BuildConfig.java中的DEBUG为false时,Log将不会有任何输出。
     *
     * @param tag      Used to identify the source of a log message
     * @param priority VERBOSE=2, DEBUG=3, INFO=4 , WARN=5, ERROR=6
     * @see Log {@link Log}
     */
    public Logger(String tag, int priority) {
        mTag = mPrefix + tag;
        mPriority = priority;
    }

    public static void v(String tag, Object msg) {
        if (BuildConfig.DEBUG)
            Log.v(tag, "" + msg);
    }

    public static void d(String tag, Object msg) {
        if (BuildConfig.DEBUG)
            Log.d(tag, "" + msg);
    }

    public static void i(String tag, Object msg) {
        if (BuildConfig.DEBUG)
            Log.i(tag, "" + msg);
    }

    public static void w(String tag, Object msg) {
        if (BuildConfig.DEBUG)
            Log.w(tag, "" + msg);
    }

    public static void e(String tag, Object msg) {
        if (BuildConfig.DEBUG)
            Log.e(tag, "" + msg);
    }

    public void v(Object msg) {
        if (mPriority <= Log.VERBOSE && BuildConfig.DEBUG)
            Log.v(mTag, "" + msg);
    }

    public void d(Object msg) {
        if (mPriority <= Log.DEBUG && BuildConfig.DEBUG)
            Log.d(mTag, "" + msg);
    }

    public void i(Object msg) {
        if (mPriority <= Log.INFO && BuildConfig.DEBUG)
            Log.i(mTag, "" + msg);
    }

    public void w(Object msg) {
        if (mPriority <= Log.WARN && BuildConfig.DEBUG)
            Log.w(mTag, "" + msg);
    }

    public void e(Object msg) {
        if (mPriority <= Log.ERROR && BuildConfig.DEBUG)
            Log.e(mTag, "" + msg);
    }
}
