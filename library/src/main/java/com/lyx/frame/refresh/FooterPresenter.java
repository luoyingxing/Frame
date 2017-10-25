package com.lyx.frame.refresh;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * FooterPresenter
 * <p>
 * author:  luoyingxing
 * date: 2017/10/25.
 */
public interface FooterPresenter {
    /**
     * 刷新或者加载时显示箭头的ImageView
     *
     * @return ImageView
     */
    ImageView getFooterArrows();

    /**
     * 刷新或者加载时显示进度的ProgressBar
     *
     * @return ProgressBar
     */
    ProgressBar getFooterProgressBar();

    /**
     * 刷新或者加载时显示提示的TextView
     *
     * @return TextView
     */
    TextView getFooterTipTextView();
}