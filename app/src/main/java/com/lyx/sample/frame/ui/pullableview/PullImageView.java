package com.lyx.sample.frame.ui.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * PullImageView
 * <p>
 * Created by luoyingxing on 2017/5/18.
 */
public class PullImageView extends ImageView implements Pull {

    public PullImageView(Context context) {
        super(context);
    }

    public PullImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullImageView(Context context, AttributeSet attrs, int defStyle) {
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
