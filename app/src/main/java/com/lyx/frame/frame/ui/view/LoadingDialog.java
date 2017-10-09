package com.lyx.frame.frame.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyx.frame.R;


/**
 * LoadingDialog
 * <p/>
 * Created by Administrator on 2017/5/18.
 */
public class LoadingDialog extends Dialog {
    private ImageView mImageView;
    private Animation mAnimation;
    private TextView mMessageTV;
    private String mMessage;

    public LoadingDialog(Context context, String message) {
        super(context, R.style.loading_dialog);
        this.mMessage = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_loading_dialog);
        mImageView = (ImageView) findViewById(R.id.iv_view_loading_circle);
        mMessageTV = (TextView) findViewById(R.id.tv_view_loading);

        mAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setDuration(2000);

        LinearInterpolator lin = new LinearInterpolator();
        mAnimation.setInterpolator(lin);
        mImageView.startAnimation(mAnimation);

        if (!TextUtils.isEmpty(mMessage)) {
            mMessageTV.setText(mMessage);
        }
    }

    @Override
    public void show() {
        if (mImageView != null) {
            mImageView.startAnimation(mAnimation);
        }
        super.show();
    }

    @Override
    public void dismiss() {
        if (mImageView != null) {
            mImageView.clearAnimation();
        }
        super.dismiss();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            dismiss();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}