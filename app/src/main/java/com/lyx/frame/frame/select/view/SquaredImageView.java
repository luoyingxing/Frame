package com.lyx.frame.frame.select.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * SquaredImageView
 * An image view which always remains square with respect to its width.
 * <p>
 * Created by luoyingxing on 2016/6/20.
 */
public class SquaredImageView extends android.support.v7.widget.AppCompatImageView {
    public SquaredImageView(Context context) {
        super(context);
    }

    public SquaredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}