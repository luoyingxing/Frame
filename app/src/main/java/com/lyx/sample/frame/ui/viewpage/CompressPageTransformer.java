package com.lyx.sample.frame.ui.viewpage;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * CompressPageTransformer
 * <p>
 * Created by luoyingxing on 2017/5/18.
 */
public class CompressPageTransformer implements ViewPager.PageTransformer {

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) {
            view.setScaleX(0);
        } else if (position <= 1) {
            if (position < 0) {
                view.setScaleX(1 - Math.abs(position));
                view.setTranslationX(pageWidth * -position * 0.5f);
            } else {
                view.setScaleX(1 - Math.abs(position));
                view.setTranslationX(pageWidth * -position * 0.5f);
            }
        } else {
            view.setScaleX(0);
        }
    }
}
