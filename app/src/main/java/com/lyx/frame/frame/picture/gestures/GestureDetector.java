package com.lyx.frame.frame.picture.gestures;

import android.view.MotionEvent;

/**
 * GestureDetector
 * <p>
 * Created by luoyingxing on 2017/5/2.
 */
public interface GestureDetector {
    public boolean onTouchEvent(MotionEvent ev);

    public boolean isScaling();

    public void setOnGestureListener(OnGestureListener listener);

}
