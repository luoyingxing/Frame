package com.lyx.frame.widget.picture.gestures;

import android.view.MotionEvent;

/**
 * GestureDetector
 * <p>
 * Created by luoyingxing on 2017/5/2.
 */
public interface GestureDetector {
    boolean onTouchEvent(MotionEvent ev);

    boolean isScaling();

    void setOnGestureListener(OnGestureListener listener);

}