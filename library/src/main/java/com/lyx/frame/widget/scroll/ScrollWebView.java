package com.lyx.frame.widget.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * ScrollWebView
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 */
public class ScrollWebView extends WebView {

    public ScrollWebView(Context context) {
        super(context);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
