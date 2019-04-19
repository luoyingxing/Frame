package com.lyx.frame.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.lyx.frame.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TabIndicatorView
 * <p/>
 * Created by luoyingxing on 2019/4/18.
 */
public class TabIndicatorView<T> extends View {
    private static final String TAG = "TabIndicatorView";

    public TabIndicatorView(Context context) {
        super(context);
        init(context, null);
    }

    public TabIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TabIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private Paint mPaint;

    private int mTextColor;

    private float mTextSize;

    /**
     * 可见的tab数量
     */
    private int visibleCount;
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * 每个tab的宽度
     */
    private int perWidth;

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabIndicatorView, 0, 0);

        mTextSize = array.getFloat(R.styleable.TabIndicatorView_textSize, 15);
        mTextColor = array.getColor(R.styleable.TabIndicatorView_textColor, 0xFF666666);
        visibleCount = array.getInteger(R.styleable.TabIndicatorView_visibleCount, 4);

        array.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        list = new ArrayList<>();

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (null != manager) {
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            screenWidth = metrics.widthPixels;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        perWidth = width / visibleCount;

        //实际上View的宽度
        int w = perWidth * list.size();

        Log.w(TAG, "w：" + w + "   width: " + width + "  perWidth: " + perWidth);

        setMeasuredDimension(w > width ? w : width, height);
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

    private List<T> list;

    public void updateList(List<T> l) {
        list = l;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        Log.w(TAG, "onDraw() " + width + " x " + height);

        int textSize = (int) (mTextSize * getResources().getDisplayMetrics().density);
        mPaint.setTextSize(textSize);
        mPaint.setColor(mTextColor);

        for (int i = 0; i < list.size(); i++) {
            String text = "";
            if (null != mOnItemClickListener) {
                text = mOnItemClickListener.getText(i);
            }

            float w = mPaint.measureText(text);
            Paint.FontMetrics sF = mPaint.getFontMetrics();
            float h = (float) (Math.ceil(sF.descent - sF.top) + 2);

            float startX = perWidth * i + (perWidth - w) / 2;
            float startY = height - (height - h) * 2 / 3;

            canvas.drawText(text, startX, startY, mPaint);
        }

        //draw tab arrow
        if (list.size() > 0) {
            int w = perWidth / 4;
            int h = w / 4;

            mPaint.setColor(Color.WHITE);

            float x1 = perWidth * mSelectIndex + (perWidth - w) / 2;
            float y1 = height;
            float x2 = x1 + w;
            float y2 = height;
            float x3 = x1 + w / 2;
            float y3 = height - h;

            Path path = new Path();
            path.moveTo(x1, y1);
            path.lineTo(x2, y2);
            path.lineTo(x3, y3);
            path.close();
            canvas.drawPath(path, mPaint);
        }
    }

    /**
     * 记录第一次点击后的X坐标值
     */
    private int downX;
    /**
     * 记录上一次移动后的X坐标值
     */
    private int lastX;
    /**
     * 记录释放手指后的X坐标值
     */
    private int upX;
    /**
     * 记录X轴的总偏移量
     */
    private int scrollX;
    /**
     * 选中的下标
     */
    private int mSelectIndex;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                Log.d(TAG, "downX: " + downX);

                lastX = downX;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();

                scrollX += lastX - moveX;

                Log.d(TAG, getWidth() + "   " + getMeasuredWidth() + "    " + screenWidth);

                if (scrollX < 0) {
                    scrollX = 0;
                } else if (scrollX > Math.abs(getMeasuredWidth() - screenWidth)) {
                    scrollX = Math.abs(getMeasuredWidth() - screenWidth);
                }

                scrollTo(scrollX, 0);

                lastX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                upX = (int) event.getX();
                Log.d(TAG, "upX: " + upX);

                if (Math.abs(upX - downX) < 5) {
                    //click
                    click(upX);
                } else {
                    //move
                }

                break;
        }

        return true;
    }

    private void click(int clickX) {
        Log.w(TAG, getScrollX() + " 单击了 " + clickX + "   " + perWidth);
        int p = ((getScrollX() + clickX) / perWidth);
        mSelectIndex = p;

        Log.i(TAG, "p " + p);
        //1.纠正布局

        invalidate();

        //2.触发点击事件
        if (null != mOnItemClickListener && list.size() > 0) {
            if (p <= list.size() - 1) {
                mOnItemClickListener.onClick(p, list.get(p));
            }
        }
    }

    private OnItemClickListener<T> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onClick(int position, T obj);

        String getText(int position);
    }
}