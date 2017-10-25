package com.lyx.frame.refresh;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * HeaderPresenter
 * <p>
 * author:  luoyingxing
 * date: 2017/10/25.
 */
public interface HeaderPresenter {
    /**
     * 刷新或者加载时显示箭头的ImageView
     *
     * @return ImageView
     */
    ImageView getHeaderArrows();

    /**
     * 刷新或者加载时显示进度的ProgressBar
     *
     * @return ProgressBar
     */
    ProgressBar getHeaderProgressBar();

    /**
     * 刷新或者加载时显示提示的TextView
     *
     * @return TextView
     */
    TextView getHeaderTipTextView();
}