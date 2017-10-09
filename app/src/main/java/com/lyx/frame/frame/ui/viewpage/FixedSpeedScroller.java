package com.lyx.frame.frame.ui.viewpage;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;


/**
 * FixedSpeedScroller
 * <p>
 * Created by luoyingxing on 2017/5/18.
 */

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

