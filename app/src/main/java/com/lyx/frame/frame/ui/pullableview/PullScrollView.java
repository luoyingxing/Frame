package com.lyx.frame.frame.ui.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * PullScrollView
 * <p>
 * Created by luoyingxing on 2017/5/18.
 */

public class PullScrollView extends ScrollView implements Pull {

    public PullScrollView(Context context) {
        super(context);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp() {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }

}
