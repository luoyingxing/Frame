package com.lyx.frame.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.lyx.frame.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TabIndicatorView
 * <p/>
 * Created by luoyingxing on 2019/4/18.
 */
public class TabIndicatorView extends View {
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

    private int visibleCount = 4;

    private int perWidth;

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabIndicatorView, 0, 0);


        array.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        list = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        perWidth = width / visibleCount;
        Log.w(TAG, getScreenWidth() + "   width: " + width + "   perWidth:  " + perWidth);

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

    private List<String> list;

    public void updateList() {
        list.addAll(Arrays.asList("人民1", "新华2", "央视3", "国际4", "在线5", "中国6", "日报7", "最后8"));

        int w = perWidth * list.size();

        setMeasuredDimension(w, getMeasuredHeight());

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float f = mPaint.getTextSize(); //12.0  默认字体大小

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        mPaint.setTextSize(50);
        mPaint.setColor(Color.RED);

        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);

            float w = mPaint.measureText(s);
            Paint.FontMetrics sF = mPaint.getFontMetrics();
            float h = (float) (Math.ceil(sF.descent - sF.top) + 2);

            float startX = perWidth * i + (perWidth - w) / 2;
            float startY = height - (height - h) * 2 / 3;

            canvas.drawText(s, startX, startY, mPaint);


        }
    }

    private int downX, upX;
    private boolean moving;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();

                Log.w(TAG, "downX: " + downX);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();

                Log.d(TAG, "move: " + (downX - moveX));


                scrollTo(downX - moveX, 0);

//                Rect rect = new Rect();
//                getGlobalVisibleRect(rect);
//                Log.v(TAG, rect.left + " , " + rect.top + " , " + rect.right + " , " + rect.bottom);


                moving = true;
                break;
            case MotionEvent.ACTION_UP:
                upX = (int) event.getX();

//                if (moving) {
//                    moving = false;
//
//                    move = upX - downX;
//                    Log.i(TAG, "===== move: " + move);
//                }

                Log.w(TAG, "upX: " + upX);
                break;
        }

        return true;
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (null != wm) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.widthPixels;
    }


}
