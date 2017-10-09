package com.lyx.frame.frame.picture;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * DialogViewPage
 * <p>
 * Created by luoyin=gxing on 2017/5/2.
 */
public class DialogViewPage extends ViewPager {
    public DialogViewPage(Context context) {
        super(context);
    }

    public DialogViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean mIsDisallowIntercept = false;

    /**
     * 解决多点触控时可能发生的错误（父控件与子控件的触摸事件，如scrollview,listview,viewpage等等）
     * 为父控件添加以下两段代码。
     */
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (ev.getPointerCount() > 1 && mIsDisallowIntercept) {
            requestDisallowInterceptTouchEvent(false);
            boolean handled = super.dispatchTouchEvent(ev);
            requestDisallowInterceptTouchEvent(true);
            return handled;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }
}
