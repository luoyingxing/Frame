package com.lyx.frame.frame.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lyx.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TabLayout
 * <p>
 * Created by luoyingxing on 2017/5/22.
 */

public class TabLayout extends RelativeLayout {
    private static final String TAG = "TabLayout";

    private float mHorizontalSpace;

    private float mVerticalSpace;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabLayout, 0, 0);
        mHorizontalSpace = array.getDimension(R.styleable.TabLayout_horizontal_space, 14f);
        mVerticalSpace = array.getDimension(R.styleable.TabLayout_vertical_space, 10f);
        array.recycle();
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return super.generateLayoutParams(lp);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return super.generateLayoutParams(attrs);
    }

//    public static class LayoutParams extends MarginLayoutParams {
//        public int gravity = -1;
//
//        public LayoutParams(Context c, AttributeSet attrs) {
//            super(c, attrs);
//            TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.TabLayout);
//            gravity = ta.getInt(R.styleable.TabLayout_layout_gravity, -1);
//            ta.recycle();
//        }
//
//        public LayoutParams(int width, int height) {
//            this(width, height, -1);
//        }
//
//        public LayoutParams(int width, int height, int gravity) {
//            super(width, height);
//            this.gravity = gravity;
//        }
//
//        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
//            super(source);
//        }
//
//        public LayoutParams(MarginLayoutParams source) {
//            super(source);
//        }
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int lineWidth = 0;

        int lineHeight = 0;

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(lineWidth, childWidth);
                lineWidth = childWidth;
                height += lineHeight + mVerticalSpace;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                if (lineWidth + mHorizontalSpace <= sizeWidth) {
                    lineWidth += mHorizontalSpace;
                }
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight + mVerticalSpace;
            }

        }
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width,
                (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);

    }

    private List<List<View>> mAllViews = new ArrayList<>();
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList<>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width - getPaddingLeft() - getPaddingRight()) {
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                lineWidth = 0;
                lineViews = new ArrayList<>();
            }

            if (lineWidth + childWidth + lp.leftMargin + lp.rightMargin + mHorizontalSpace > width - getPaddingLeft() - getPaddingRight()) {
                lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            } else {
                child.setTag("HasSpace");
                lineWidth += childWidth + lp.leftMargin + lp.rightMargin + mHorizontalSpace;
            }

            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        int left = getPaddingLeft();
        int top = getPaddingTop();
        int lineNumber = mAllViews.size();
        for (int i = 0; i < lineNumber; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);

                boolean hasSpace = child.getTag() != null;
                int space = hasSpace ? (int) mHorizontalSpace : 0;
                left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin + space;

            }
            left = getPaddingLeft();
            top += lineHeight + mVerticalSpace;
        }
    }
}