package com.lyx.frame.frame.ui.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * PullWebView
 * <p>
 * Created by luoyingxing on 2017/5/18.
 */
public class PullWebView extends WebView implements Pull {

    public PullWebView(Context context) {
        super(context);
    }

    public PullWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        return getScrollY() == 0;
    }

    @Override
    public boolean canPullUp() {
        if (getScrollY() >= getContentHeight() * getScale()
                - getMeasuredHeight())
            return true;
        else
            return false;
    }
}
