package com.lyx.frame.widget.picture.gestures;

/**
 * OnGestureListener
 * <p>
 * Created by luoyingxing on 2017/5/2.
 */
public interface OnGestureListener {

    void onDrag(float dx, float dy);

    void onFling(float startX, float startY, float velocityX, float velocityY);

    void onScale(float scaleFactor, float focusX, float focusY);

}