package com.lyx.sample.frame.ui.viewpage;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * TabletPageTransformer
 * <p>
 * Created by luoyingxing on 2017/5/18.
 */
public class TabletPageTransformer implements ViewPager.PageTransformer {

    public void transformPage(View view, float position) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        if (position < -1) {
            view.setAlpha(1);
        } else if (position <= 1) {
            float mRot;
            if (position < 0) {
                mRot = 30.0f * Math.abs(position);
                view.setPivotX(width / 2);
                view.setPivotY(height / 2);
                view.setRotationY(mRot);
            } else {
                mRot = -30.0f * Math.abs(position);
                view.setPivotX(width / 2);
                view.setPivotY(height / 2);
                view.setRotationY(mRot);
            }
        } else {
            view.setAlpha(1);
        }
    }
}
