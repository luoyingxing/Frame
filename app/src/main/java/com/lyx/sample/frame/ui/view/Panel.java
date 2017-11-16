package com.lyx.sample.frame.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.lyx.frame.utils.DpiUtils;

/**
 * Panel
 * <p>
 * Created by luoyingxing on 2017/6/7.
 */

public class Panel extends PopupWindow {
    private Context mContext;
    private View mRootView;

    public int getLayoutResource() {
        return 0;
    }

    public Panel() {
    }

    public Panel(Context context) {
        this(context, null, 0);
    }

    public Panel(Context context, int defStyleAttr) {
        this(context, null, defStyleAttr);
    }

    public Panel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mRootView = LayoutInflater.from(context).inflate(getLayoutResource(), null);
        setContentView(mRootView);

        setWidth(DpiUtils.getWidth(mContext));
        setHeight(DpiUtils.getHeight(mContext));

        setFocusable(true);
        setOutsideTouchable(true);
        update();
        setBackgroundDrawable(new ColorDrawable());
        setAnimationStyle(defStyleAttr);

        init();
    }

    public void init() {
    }

    public View getParent() {
        return mRootView;
    }

    public Context getContext() {
        return mContext;
    }

    public <T extends View> T findViewById(int viewId) {
        return (T) mRootView.findViewById(viewId);
    }

    /**
     * showing below the parent
     *
     * @param parent view
     */
    public void show(View parent) {
        if (!isShowing()) {
            showAsDropDown(parent, 0, 0);
        } else {
            dismiss();
        }
    }
}