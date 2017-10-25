package com.lyx.frame.refresh;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Presenter mvp模式处理
 * <p>
 * author:  luoyingxing
 * date: 2017/10/25.
 */
public interface Presenter {
    /**
     * 刷新或者加载时显示箭头的ImageView
     *
     * @return ImageView
     */
    ImageView getArrows();

    /**
     * 刷新或者加载时显示进度的ProgressBar
     *
     * @return ProgressBar
     */
    ProgressBar getProgressBar();

    /**
     * 刷新或者加载时显示提示的TextView
     *
     * @return TextView
     */
    TextView getTipTextView();

}