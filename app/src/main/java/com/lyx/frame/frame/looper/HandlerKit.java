package com.lyx.frame.frame.looper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import java.util.LinkedList;
import java.util.Queue;

/**
 * HandlerKit
 * <p>
 * Created by luoyingxing on 2017/5/18.
 */

public class HandlerKit extends Handler {
    private final int ASYNC = 0x1;
    private final int SYNC = 0x2;
    private final Queue<Runnable> mAsyncPool;
    private final Queue<SyncRunnable> mSyncPool;
    private final int mMaxMillisInsideHandleMessage;
    private boolean mAsyncActive;
    private boolean mSyncActive;

    HandlerKit(Looper looper, int maxMillisInsideHandleMessage) {
        super(looper);
        this.mMaxMillisInsideHandleMessage = maxMillisInsideHandleMessage;
        mAsyncPool = new LinkedList<>();
        mSyncPool = new LinkedList<>();
    }

    public void dispose() {
        this.removeCallbacksAndMessages(null);
        this.mAsyncPool.clear();
        this.mSyncPool.clear();
    }

    public void async(Runnable runnable) {
        synchronized (mAsyncPool) {
            mAsyncPool.offer(runnable);
            if (!mAsyncActive) {
                mAsyncActive = true;
                if (!sendMessage(obtainMessage(ASYNC))) {
                    try {
                        throw new LooperKitException("Could not send handler message");
                    } catch (LooperKitException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void sync(SyncRunnable runnable) {
        synchronized (mSyncPool) {
            mSyncPool.offer(runnable);
            if (!mSyncActive) {
                mSyncActive = true;
                if (!sendMessage(obtainMessage(SYNC))) {
                    try {
                        throw new LooperKitException("Could not send handler message");
                    } catch (LooperKitException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == ASYNC) {
            boolean rescheduled = false;
            try {
                long started = SystemClock.uptimeMillis();
                while (true) {
                    //get runnable from queue
                    Runnable runnable = mAsyncPool.poll();
                    if (runnable == null) {
                        synchronized (mAsyncPool) {
                            // Check again, this time in synchronized
                            runnable = mAsyncPool.poll();
                            if (runnable == null) {
                                mAsyncActive = false;
                                return;
                            }
                        }
                    }
                    runnable.run();
                    long timeInMethod = SystemClock.uptimeMillis() - started;
                    if (timeInMethod >= mMaxMillisInsideHandleMessage) {
                        if (!sendMessage(obtainMessage(ASYNC))) {
                            try {
                                throw new LooperKitException("Could not send handler message");
                            } catch (LooperKitException e) {
                                e.printStackTrace();
                            }
                        }
                        rescheduled = true;
                        return;
                    }
                }
            } finally {
                mAsyncActive = rescheduled;
            }
        } else if (msg.what == SYNC) {
            boolean rescheduled = false;
            try {
                long started = SystemClock.uptimeMillis();
                while (true) {
                    SyncRunnable post = mSyncPool.poll();
                    if (post == null) {
                        synchronized (mSyncPool) {
                            // Check again, this time in synchronized
                            post = mSyncPool.poll();
                            if (post == null) {
                                mSyncActive = false;
                                return;
                            }
                        }
                    }
                    post.run();
                    long timeInMethod = SystemClock.uptimeMillis() - started;
                    if (timeInMethod >= mMaxMillisInsideHandleMessage) {
                        if (!sendMessage(obtainMessage(SYNC))) {
                            try {
                                throw new LooperKitException("Could not send handler message");
                            } catch (LooperKitException e) {
                                e.printStackTrace();
                            }
                        }
                        rescheduled = true;
                        return;
                    }
                }
            } finally {
                mSyncActive = rescheduled;
            }
        } else super.handleMessage(msg);
    }
}
