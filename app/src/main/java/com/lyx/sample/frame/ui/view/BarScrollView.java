package com.lyx.sample.frame.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * BarScrollView
 * <p>
 * Rewrite is to solve the nested ScrollListView clicked forced placed at the top of the problem
 * <p>
 * Created by luoyingxing on 2017/6/18.
 */

public class BarScrollView extends NestedScrollView {
    public BarScrollView(Context context) {
        super(context);
    }

    public BarScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}