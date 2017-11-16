package com.lyx.frame.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * WindowUtils
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 */
public class WindowUtils {

    public static void statusBar(AppCompatActivity activity, int color) {
        // only for sdk >= 19
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getSupportActionBar().setElevation(0);

            ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            int statusBarHeight = getStatusBarHeight(activity.getApplicationContext());

            View mTopView = mContentView != null ? mContentView.getChildAt(0) : null;
            if (mTopView != null && mTopView.getLayoutParams() != null && mTopView.getLayoutParams().height == statusBarHeight) {
                mTopView.setBackgroundColor(activity.getApplicationContext().getResources().getColor(color));
                return;
            }
            if (mTopView != null) {
                ViewCompat.setFitsSystemWindows(mTopView, true);
            }

            mTopView = new View(activity.getApplicationContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            mTopView.setBackgroundColor(activity.getApplicationContext().getResources().getColor(color));
            if (mContentView != null) {
                mContentView.addView(mTopView, 0, lp);
            }
        }

    }

    public static int getStatusBarHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    //返回值就是状态栏的高度,得到的值是像素
    public static float getStatusBarHeight2(Context context) {
        float result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimension(resourceId);
        }
        return result;
    }

    //返回值就是导航栏的高度,得到的值是像素
    public static float getNavigationBarHeight(Context context) {
        float result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimension(resourceId);
        }
        return result;
    }
}