package com.lyx.sample.frame.picture.scroller;

import android.annotation.TargetApi;
import android.content.Context;

/**
 * IcsScroller
 * <p>
 * Created by luoyingxing on 2017/5/2.
 */
@TargetApi(14)
public class IcsScroller extends GingerScroller {

    public IcsScroller(Context context) {
        super(context);
    }

    @Override
    public boolean computeScrollOffset() {
        return mScroller.computeScrollOffset();
    }

}
