package com.lyx.frame.looper;

import android.os.Looper;

/**
 * LooperKit
 * <p>
 * Created by luoyingxing on 2017/5/18.
 */

public class LooperKit {
    private static HandlerKit mHandlerKit = null;
    private static final int MILLISECOND = 20;

    private static HandlerKit getHandlerKit() {
        if (mHandlerKit == null) {
            synchronized (LooperKit.class) {
                if (mHandlerKit == null) {
                    mHandlerKit = new HandlerKit(Looper.getMainLooper(), MILLISECOND);
                }
            }
        }
        return mHandlerKit;
    }

    /**
     * Asynchronously
     * The child thread asynchronous run relative to the main thread,
     * not blocking the child thread
     *
     * @param runnable Runnable Interface
     */
    public static void runOnMainThreadAsync(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        getHandlerKit().async(runnable);
    }

    /**
     * Synchronously
     * The child thread relative thread synchronization operation,
     * blocking the child thread,
     * thread for the main thread to complete
     *
     * @param runnable Runnable Interface
     */
    public static void runOnMainThreadSync(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        SyncRunnable sync = new SyncRunnable(runnable);
        getHandlerKit().sync(sync);
        sync.waitRun();
    }

    /**
     * Synchronously
     * The child thread relative thread synchronization operation,
     * blocking the child thread,
     * thread for the main thread to complete
     *
     * @param runnable Runnable Interface
     * @param waitTime waitTime
     * @param cancel   cancel
     */
    public static void runOnMainThreadSync(Runnable runnable, int waitTime, boolean cancel) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        SyncRunnable sync = new SyncRunnable(runnable);
        getHandlerKit().sync(sync);
        sync.waitRun(waitTime, cancel);
    }

    public static void dispose() {
        if (mHandlerKit != null) {
            mHandlerKit.dispose();
            mHandlerKit = null;
        }
    }
}