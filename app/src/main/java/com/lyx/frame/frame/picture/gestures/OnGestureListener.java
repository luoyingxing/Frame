package com.lyx.frame.frame.picture.gestures;

/**
 * OnGestureListener
 * <p>
 * Created by luoyingxing on 2017/5/2.
 */
public interface OnGestureListener {

    public void onDrag(float dx, float dy);

    public void onFling(float startX, float startY, float velocityX, float velocityY);

    public void onScale(float scaleFactor, float focusX, float focusY);

}