package com.lyx.frame.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.lyx.frame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TabIndicatorView
 * <p/>
 * Created by luoyingxing on 2019/4/18.
 */
public class TabIndicatorView<T> extends View {
    private static final String TAG = "TabIndicatorView";

    public static final int STYLE_LINE = 0;
    public static final int STYLE_TRIANGLE = 1;

    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 文字颜色
     */
    private int mTextColor;
    /**
     * 选中的文字颜色
     */
    private int mTextCheckedColor;
    /**
     * 文字大小
     */
    private float mTextSize;
    /**
     * 指示器的颜色
     */
    private int mTabColor;
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
    /**
     * 是否调试模式
     */
    private boolean DEBUG;
    /**
     * 指示器的样式类型
     */
    private int mTabStyle;

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

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabIndicatorView, 0, 0);

        mTextSize = array.getDimensionPixelSize(R.styleable.TabIndicatorView_textSize, 15);
        mTextCheckedColor = array.getColor(R.styleable.TabIndicatorView_textCheckedColor, 0xFF666666);
        mTextColor = array.getColor(R.styleable.TabIndicatorView_textColor, 0xFF666666);
        mTabColor = array.getColor(R.styleable.TabIndicatorView_tabColor, 0xFFFFFFFF);
        visibleCount = array.getInteger(R.styleable.TabIndicatorView_visibleCount, 4);
        mTabStyle = array.getInt(R.styleable.TabIndicatorView_tabStyle, 0);

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

        if (DEBUG) {
            Log.w(TAG, TAG + "的实际宽度：" + w + "   在父view里面的宽度: " + width + "  每个Tab的宽度: " + perWidth);
        }

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

    public void setChecked(int position) {
        mSelectIndex = position;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (DEBUG) {
            Log.w(TAG, "onDraw()  [" + width + " x " + height + "]");
        }

        int textSize = (int) mTextSize;
        mPaint.setTextSize(textSize);

        for (int i = 0; i < list.size(); i++) {
            if (i == mSelectIndex) {
                mPaint.setColor(mTextCheckedColor);
            } else {
                mPaint.setColor(mTextColor);
            }

            String text = "";
            if (null != mOnItemClickListener) {
                text = mOnItemClickListener.getTabText(list.get(i));
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
            if (mTabStyle == STYLE_LINE) {
                int w = perWidth * 2 / 3;
                int h = 6;

                mPaint.setColor(mTabColor);

                float x1 = perWidth * mSelectIndex + (perWidth - w) / 2;
                float y1 = height - h;
                float x2 = x1 + w;
                float y2 = height - h;

                mPaint.setStrokeWidth(h);
                canvas.drawLine(x1, y1, x2, y2, mPaint);
            } else if (mTabStyle == STYLE_TRIANGLE) {
                int w = perWidth / 4;
                int h = w / 4;

                mPaint.setColor(mTabColor);

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

                if (DEBUG) {
                    Log.d(TAG, "downX: " + downX);
                }

                lastX = downX;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();

                if (DEBUG) {
                    Log.d(TAG, getWidth() + "   " + getMeasuredWidth() + "    " + screenWidth);
                }

                scrollX += lastX - moveX;

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

                if (DEBUG) {
                    Log.d(TAG, "upX: " + upX);
                }

                if (Math.abs(upX - downX) < 5) {
                    //click
                    click(upX);
                } else {
                    //move
                    int move = downX - upX;

                    //make a animation to show scrolling
                    animationScroll(move);
                }

                break;
        }

        return true;
    }

    private void click(int clickX) {
        if (DEBUG) {
            Log.w(TAG, getScrollX() + " 单击了 " + clickX + "   " + perWidth);
        }

        int position = ((getScrollX() + clickX) / perWidth);
        mSelectIndex = position;

        if (DEBUG) {
            Log.i(TAG, "onClick position is " + position);
        }

        //1.纠正布局
        Rect rect = new Rect();
        getLocalVisibleRect(rect);

        //当前选中的标签的起始和终止X坐标值
        int startX = perWidth * position;
        int endX = startX + perWidth;

        if (rect.left > startX) {
            //标签在左边被隐藏了
            int overX = rect.left - startX;
            scrollX -= overX;

            scrollTo(scrollX, 0);
        }

        if (endX > rect.right) {
            //标签在右边被隐藏了
            scrollX += endX - rect.right;

            scrollTo(scrollX, 0);
        }

        invalidate();

        //2.触发点击事件
        if (null != mOnItemClickListener && list.size() > 0) {
            if (position <= list.size() - 1) {
                mOnItemClickListener.onClick(position, list.get(position));
            }
        }
    }

    /**
     * 滑动完成后根据手指移动的距离大小进行后摇滚动
     * move / 2.0f  和 value / 2  均为减小后摇滚动系数到正常水平
     *
     * @param move 手指按下和松开过程中总的距离
     */
    private void animationScroll(int move) {
        ValueAnimator anim = ValueAnimator.ofFloat(move / 2.0f, 0);
        anim.setDuration(260L).start(); //260L为动画持续时间
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                scrollX += value / 2;
                if (scrollX < 0) {
                    scrollX = 0;
                    animation.cancel();
                } else if (scrollX > Math.abs(getMeasuredWidth() - screenWidth)) {
                    scrollX = Math.abs(getMeasuredWidth() - screenWidth);
                    animation.cancel();
                }

                scrollTo(scrollX, 0);
            }
        });
    }

    private OnItemClickListener<T> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onClick(int position, T obj);

        String getTabText(T obj);
    }
}