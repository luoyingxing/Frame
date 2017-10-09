package com.lyx.frame.frame.ui.pullableview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * PullTextView
 * <p>
 * Created by luoyingxing on 2017/5/18.
 */

public class PullTextView extends AppCompatTextView implements Pull {

    public PullTextView(Context context) {
        super(context);
    }

    public PullTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        return true;
    }

    @Override
    public boolean canPullUp() {
        return true;
    }

}
