package com.lyx.sample.frame.ui.view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lyx.sample.R;
import com.lyx.sample.frame.BaseFragment;

import java.lang.ref.SoftReference;

/**
 * TitleToolBar
 * <p>
 * Created by luoyingxing on 2017/6/28.
 */

public class BaseToolBar extends Toolbar implements View.OnClickListener, View.OnTouchListener {
    private View mRootView;
    private SoftReference<BaseFragment> mFragment;
    private ImageView mBackIV;
    private TextView mTitleTV;
    private ImageView mRightIV;
    private TextView mRightTV;
    private RelativeLayout mCenterLayout;

    public void init(BaseFragment fragment) {
        mFragment = new SoftReference<>(fragment);
    }

    public BaseToolBar(Context context) {
        super(context);
        initView(context);
    }

    public BaseToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        if (mRootView == null) {
            mRootView = LayoutInflater.from(context).inflate(R.layout.toolbar_custom_view, null);
        }

        mBackIV = (ImageView) mRootView.findViewById(R.id.iv_base_toolbar_back);
        mTitleTV = (TextView) mRootView.findViewById(R.id.tv_base_toolbar_title);
        mRightIV = (ImageView) mRootView.findViewById(R.id.iv_base_toolbar_right_image);
        mRightTV = (TextView) mRootView.findViewById(R.id.tv_base_toolbar_right_text);
        mCenterLayout = (RelativeLayout) mRootView.findViewById(R.id.rl_base_toolbar_layout);

        mBackIV.setOnTouchListener(this);
        mRightIV.setOnTouchListener(this);
        mRightTV.setOnTouchListener(this);
        mBackIV.setOnClickListener(this);
        mRightTV.setOnClickListener(this);
        mRightIV.setOnClickListener(this);

        Toolbar.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.FILL);
        addView(mRootView, params);
        setContentInsetsRelative(0, 0);
    }

    public void setLocationOnClickListener(OnClickListener listener) {
        mLocationListener = listener;
    }

    public void showBackView(boolean show) {
        mBackIV.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public void setBackDrawable(Drawable drawable) {
        mBackIV.setImageDrawable(drawable);
    }

    public void setBackResource(int resId) {
        mBackIV.setImageResource(resId);
    }

    public void setTitle(CharSequence text) {
        mTitleTV.setText(text);
    }

    public void setTitleColor(int color) {
        mTitleTV.setTextColor(color);
    }

    public void setRightText(CharSequence text) {
        mRightTV.setText(text);
    }

    public TextView getRightTextView() {
        return mRightTV;
    }

    public void setRightImage(Drawable drawable) {
        mRightIV.setImageDrawable(drawable);
    }

    public void setRightResource(int resId) {
        mRightIV.setImageResource(resId);
    }

    public void setOnRightImageListener(OnClickListener listener) {
        mRightImageListener = listener;
    }

    public void setOnRightTextListener(OnClickListener listener) {
        mRightTextListener = listener;
    }

    public RelativeLayout getCenterLayout() {
        return mCenterLayout;
    }

    private OnClickListener mLocationListener;
    private OnClickListener mRightImageListener;
    private OnClickListener mRightTextListener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_base_toolbar_back:
                if (null != mFragment) {
                    mFragment.get().goBack();
                }
                break;
            case R.id.iv_base_toolbar_right_image:
                if (mRightImageListener != null) {
                    mRightImageListener.onClick(v);
                }
                break;
            case R.id.tv_base_toolbar_right_text:
                if (mRightTextListener != null) {
                    mRightTextListener.onClick(v);
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.94f, 1.0f);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.94f, 1.0f);
                ObjectAnimator.ofPropertyValuesHolder(v, pvhX, pvhY).setDuration(100).start();
                break;
        }
        return false;
    }
}