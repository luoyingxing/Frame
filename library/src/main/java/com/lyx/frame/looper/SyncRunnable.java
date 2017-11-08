package com.lyx.frame.looper;

/**
 * SyncRunnable
 * <p>
 * Created by luoyingxing on 2017/5/18.
 */

public final class SyncRunnable {
    private boolean mEnd = false;
    private Runnable mRunnable;

    SyncRunnable(Runnable runnable) {
        this.mRunnable = runnable;
    }

    public void run() {
        if (!mEnd) {
            synchronized (this) {
                if (!mEnd) {
                    mRunnable.run();
                    mEnd = true;
                    try {
                        this.notifyAll();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void waitRun() {
        if (!mEnd) {
            synchronized (this) {
                if (!mEnd) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void waitRun(int time, boolean cancel) {
        if (!mEnd) {
            synchronized (this) {
                if (!mEnd) {
                    try {
                        this.wait(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (!mEnd && cancel)
                            mEnd = true;
                    }
                }
            }
        }
    }
}
