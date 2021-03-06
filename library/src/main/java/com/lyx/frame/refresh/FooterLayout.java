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
 * FooterLayout 自定义加载，实现Footer接口
 * <p>
 * author:  luoyingxing
 * date: 2017/8/28.
 */

public class FooterLayout extends RelativeLayout implements Footer {
    private String mTipInit;
    private String mTipPrepare;
    private String mTipLoading;
    private String mTipFinish;
    private int mArrowsUpId;
    private int mArrowsDownId;

    public FooterLayout(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public FooterLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public FooterLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @Override
    public void onPullingUp(float percent, float pullHeight, int footerHeight, int extendHeight) {
        if (null != mOnPullingListener) {
            mOnPullingListener.onPullingUp(percent, pullHeight, footerHeight, extendHeight);
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FooterLayout, 0, 0);

        mTipInit = array.getString(R.styleable.FooterLayout_tip_init);
        mTipPrepare = array.getString(R.styleable.FooterLayout_tip_prepare);
        mTipLoading = array.getString(R.styleable.FooterLayout_tip_loading);
        mTipFinish = array.getString(R.styleable.FooterLayout_tip_finish);

        mArrowsUpId = array.getResourceId(R.styleable.FooterLayout_arrows_up, 0);
        mArrowsDownId = array.getResourceId(R.styleable.FooterLayout_arrows_down, 0);

        if (TextUtils.isEmpty(mTipInit)) {
            mTipInit = getResources().getString(R.string.refresh_footer_tip_init);
        }

        if (TextUtils.isEmpty(mTipPrepare)) {
            mTipPrepare = getResources().getString(R.string.refresh_footer_tip_prepare);
        }

        if (TextUtils.isEmpty(mTipLoading)) {
            mTipLoading = getResources().getString(R.string.refresh_footer_tip_load);
        }

        if (TextUtils.isEmpty(mTipFinish)) {
            mTipFinish = getResources().getString(R.string.refresh_footer_tip_finish);
        }

        array.recycle();
    }

    @Override
    public void onInit() {
        if (null == mOnStatusListener) {
            if (null != mPresenter) {
                mPresenter.getFooterTipTextView().setText(mTipInit);
                mPresenter.getFooterProgressBar().setVisibility(View.INVISIBLE);
                mPresenter.getFooterArrows().setVisibility(View.VISIBLE);
                mPresenter.getFooterArrows().setImageResource(mArrowsUpId);
            }
        } else {
            mOnStatusListener.onInit();
        }
    }

    @Override
    public void onPrepareToLoadMore() {
        if (null == mOnStatusListener) {
            if (null != mPresenter) {
                mPresenter.getFooterTipTextView().setText(mTipPrepare);
                mPresenter.getFooterArrows().setImageResource(mArrowsDownId);
            }
        } else {
            mOnStatusListener.onPrepareToLoadMore();
        }
    }

    @Override
    public void onLoading() {
        if (null == mOnStatusListener) {
            if (null != mPresenter) {
                mPresenter.getFooterTipTextView().setText(mTipLoading);
                mPresenter.getFooterProgressBar().setVisibility(View.VISIBLE);
                mPresenter.getFooterArrows().setVisibility(View.INVISIBLE);
            }
        } else {
            mOnStatusListener.onLoading();
        }
    }

    @Override
    public void onFinish() {
        if (null == mOnStatusListener) {
            if (null != mPresenter) {
                mPresenter.getFooterTipTextView().setText(mTipFinish);
                mPresenter.getFooterProgressBar().setVisibility(View.INVISIBLE);
                mPresenter.getFooterArrows().setVisibility(View.VISIBLE);
                mPresenter.getFooterArrows().setImageResource(mArrowsUpId);
            }
        } else {
            mOnStatusListener.onFinish();
        }
    }

    public interface OnPullingListener {
        /**
         * 手指拖动上拉
         *
         * @param percent      上拉的百分比 0.00 - 1.00
         * @param pullHeight   上拉的距离
         * @param footerHeight footer的高度
         * @param extendHeight footer的扩展高度
         */
        void onPullingUp(float percent, float pullHeight, int footerHeight, int extendHeight);
    }

    /**
     * 使用代理者处理加载事件
     * <p>
     * 如果没有设置 OnStatusListener，则使用 FooterPresenter
     * 若设置了 OnStatusListener，则优先使用 OnStatusListener
     */
    private FooterPresenter mPresenter;

    public void setPresenter(FooterPresenter presenter) {
        mPresenter = presenter;
    }

    private OnPullingListener mOnPullingListener;

    public void setOnPullingListener(OnPullingListener listener) {
        this.mOnPullingListener = listener;
    }

    public interface OnStatusListener {
        void onInit();

        void onPrepareToLoadMore();

        void onLoading();

        void onFinish();
    }

    private OnStatusListener mOnStatusListener;

    public void setOnStatusListener(OnStatusListener listener) {
        this.mOnStatusListener = listener;
    }
}