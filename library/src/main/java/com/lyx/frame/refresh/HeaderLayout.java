package com.lyx.frame.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.lyx.frame.R;

/**
 * HeaderLayout 自定义刷新头，实现Header接口
 * <p>
 * author:  luoyingxing
 * date: 2017/8/28.
 */

public class HeaderLayout extends RelativeLayout implements Header {
    private String mTipInit;
    private String mTipPrepare;
    private String mTipLoading;
    private String mTipFinish;
    private int mArrowsUpId;
    private int mArrowsDownId;

    public HeaderLayout(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public HeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public HeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HeaderLayout, 0, 0);

        mTipInit = array.getString(R.styleable.HeaderLayout_tip_init);
        mTipPrepare = array.getString(R.styleable.HeaderLayout_tip_prepare);
        mTipLoading = array.getString(R.styleable.HeaderLayout_tip_loading);
        mTipFinish = array.getString(R.styleable.HeaderLayout_tip_finish);

        mArrowsUpId = array.getResourceId(R.styleable.HeaderLayout_arrows_up, 0);
        mArrowsDownId = array.getResourceId(R.styleable.HeaderLayout_arrows_down, 0);

        if (TextUtils.isEmpty(mTipInit)) {
            mTipInit = getResources().getString(R.string.refresh_header_tip_init);
        }

        if (TextUtils.isEmpty(mTipPrepare)) {
            mTipPrepare = getResources().getString(R.string.refresh_header_tip_prepare);
        }

        if (TextUtils.isEmpty(mTipLoading)) {
            mTipLoading = getResources().getString(R.string.refresh_header_tip_load);
        }

        if (TextUtils.isEmpty(mTipFinish)) {
            mTipFinish = getResources().getString(R.string.refresh_header_tip_finish);
        }

        array.recycle();
    }

    @Override
    public void onPullingDown(float percent, float pullHeight, int headerHeight, int extendHeight) {
        if (null != mOnPullingListener) {
            mOnPullingListener.onPulling(percent, pullHeight, headerHeight, extendHeight);
        }
    }

    @Override
    public void onInit() {
        if (null == mOnStatusListener) {
            if (null != mPresenter) {
                mPresenter.getHeaderTipTextView().setText(mTipInit);
                mPresenter.getHeaderProgressBar().setVisibility(View.INVISIBLE);
                mPresenter.getHeaderArrows().setVisibility(View.VISIBLE);
                mPresenter.getHeaderArrows().setImageResource(mArrowsDownId);
            }
        } else {
            mOnStatusListener.onInit();
        }
    }

    @Override
    public void onPrepareToRefresh() {
        if (null == mOnStatusListener) {
            if (null != mPresenter) {
                mPresenter.getHeaderTipTextView().setText(mTipPrepare);
                mPresenter.getHeaderArrows().setImageResource(mArrowsUpId);
            }
        } else {
            mOnStatusListener.onPrepareToRefresh();
        }
    }

    @Override
    public void onRefreshing() {
        if (null == mOnStatusListener) {
            if (null != mPresenter) {
                mPresenter.getHeaderTipTextView().setText(mTipLoading);
                mPresenter.getHeaderProgressBar().setVisibility(View.VISIBLE);
                mPresenter.getHeaderArrows().setVisibility(View.INVISIBLE);
            }
        } else {
            mOnStatusListener.onRefreshing();
        }
    }

    @Override
    public void onFinish() {
        if (null == mOnStatusListener) {
            if (null != mPresenter) {
                mPresenter.getHeaderTipTextView().setText(mTipFinish);
                mPresenter.getHeaderProgressBar().setVisibility(View.INVISIBLE);
                mPresenter.getHeaderArrows().setVisibility(View.VISIBLE);
                mPresenter.getHeaderArrows().setImageResource(mArrowsDownId);
            }
        } else {
            mOnStatusListener.onFinish();
        }
    }

    public interface OnPullingListener {
        /**
         * 手指拖动下拉
         *
         * @param percent      下拉的百分比 0.00 - 1.00
         * @param pullHeight   下拉的距离
         * @param headerHeight Header的高度
         * @param extendHeight Header的扩展高度
         */
        void onPulling(float percent, float pullHeight, int headerHeight, int extendHeight);
    }

    /**
     * 使用代理者处理加载事件
     * <p>
     * 如果没有设置 OnStatusListener，则使用 HeaderPresenter
     * 若设置了 OnStatusListener，则优先使用 OnStatusListener
     */
    private HeaderPresenter mPresenter;

    public void setPresenter(HeaderPresenter presenter) {
        mPresenter = presenter;
    }

    private OnPullingListener mOnPullingListener;

    public void setOnPullingListener(OnPullingListener listener) {
        this.mOnPullingListener = listener;
    }

    public interface OnStatusListener {
        void onInit();

        void onPrepareToRefresh();

        void onRefreshing();

        void onFinish();
    }

    private OnStatusListener mOnStatusListener;

    public void setOnStatusListener(OnStatusListener listener) {
        this.mOnStatusListener = listener;
    }
}