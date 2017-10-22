package com.lyx.sample.frame.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lyx.sample.R;

import java.util.List;

/**
 * TimeAxisView
 * <p>
 * Created by luoyingxing on 2017/6/23.
 */

public class TimeAxisView extends View {
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;
    /**
     * drawing of the Paint
     */
    private Paint mPaint;
    /**
     * TimeAxis Orientation
     */
    private int mOrientation;
    /**
     * TimeAxis text
     */
    private List<String> mTextList;
    /**
     * font size
     */
    private float mTextSize = 16f;
    /**
     * the number of Circle
     */
    private int mCircleCount;
    /**
     * The radius of the circle
     */
    private int mCircleRadius;
    /**
     * The interval of words and circle
     */
    private int mCirclePadding;
    /**
     * The color of the circle
     */
    private int mCircleColor;
    /**
     * The color of the text
     */
    private int mTextColor;
    /**
     * The circle of stroke width
     */
    private int mCircleStrokeWidth;
    /**
     * the line of stroke width
     */
    private int mLineStrokeWidth;
    /**
     * The line of line to circle
     */
    private int mInterval;

    public TimeAxisView(Context context) {
        this(context, null);
    }

    public TimeAxisView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeAxisView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeAxisView, 0, 0);

        mTextSize = array.getFloat(R.styleable.TimeAxisView_textSize, 14);

        mCircleCount = array.getInt(R.styleable.TimeAxisView_count, 3);
        mCircleRadius = array.getInt(R.styleable.TimeAxisView_radius, 20);
        mCircleStrokeWidth = array.getInt(R.styleable.TimeAxisView_strokeWidth, 1);
        mCirclePadding = array.getInt(R.styleable.TimeAxisView_circlePadding, 10);
        mInterval = array.getInt(R.styleable.TimeAxisView_interval, 6);
        mLineStrokeWidth = array.getInt(R.styleable.TimeAxisView_lineStrokeWidth, 2);
        mOrientation = array.getInt(R.styleable.TimeAxisView_orientation, 1);

        mCircleColor = array.getColor(R.styleable.TimeAxisView_circleColor, 0x333333);
        mTextColor = array.getColor(R.styleable.TimeAxisView_textColor, 0x666666);

        array.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    /**
     * Set the display text
     *
     * @param list List<String>
     */
    public void setTextList(List<String> list) {
        mTextList = list;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public static int getDefaultSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED: //match_parent
                result = size;
                break;
            case MeasureSpec.AT_MOST: //wrap_content
            case MeasureSpec.EXACTLY: //xx dp
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTextList.size() != mCircleCount) {
            throw new IllegalStateException("the text length is not equal to circle count ! ");
        }

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mCircleStrokeWidth);
        mPaint.setTextAlign(Paint.Align.CENTER);
        int testSize = (int) (mTextSize * getResources().getDisplayMetrics().density);
        mPaint.setTextSize(testSize);

        int perWidth = width / mCircleCount;

        float circleX = perWidth / 2;
        float cx;

        for (int i = 0; i < mCircleCount; i++) {
            //circle
            mPaint.setColor(mCircleColor);
            if (i == 0 || i == mCircleCount - 1) {
                mPaint.setStyle(Paint.Style.FILL);
            } else {
                mPaint.setStyle(Paint.Style.STROKE);
            }
            cx = circleX + i * perWidth;
            canvas.drawCircle(cx, paddingTop + mCircleRadius, mCircleRadius, mPaint);

            //text
            mPaint.setColor(mTextColor);
            mPaint.setStyle(Paint.Style.FILL);
            String text = mTextList.get(i);
            Rect bounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, cx, paddingTop + mCircleRadius * 2 + mCirclePadding + bounds.height() / 2, mPaint);

            //horizontal line
            if (i != 0) {
                mPaint.setColor(mCircleColor);
                mPaint.setStrokeWidth(mLineStrokeWidth);
                canvas.drawLine(cx - perWidth + mCircleRadius + mInterval, paddingTop + mCircleRadius,
                        cx - mCircleRadius - mInterval, paddingTop + mCircleRadius, mPaint);
            }

        }
    }
}