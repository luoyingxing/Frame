package com.lyx.sample.frame.picture.scroller;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

/**
 * ScrollerProxy
 * <p>
 * Created by luoyingxing on 2017/5/2.
 */
public abstract class ScrollerProxy {
    public static ScrollerProxy getScroller(Context context) {
        if (VERSION.SDK_INT < VERSION_CODES.GINGERBREAD) {
            return new PreGingerScroller(context);
        } else if (VERSION.SDK_INT < VERSION_CODES.ICE_CREAM_SANDWICH) {
            return new GingerScroller(context);
        } else {
            return new IcsScroller(context);
        }
    }

    public abstract boolean computeScrollOffset();

    public abstract void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY);

    public abstract void forceFinished(boolean finished);

    public abstract boolean isFinished();

    public abstract int getCurrX();

    public abstract int getCurrY();


}
