package com.lyx.frame.slide.animator;

/**
 * FixedSpeedScroller
 * <p>
 * Author:  luoyingxing
 * Date: 2017/10/16.
 */

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller {
    private int mDuration = 500;

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setMDuration(int time) {
        mDuration = time;
    }

    public int getMDuration() {
        return mDuration;
    }

}

