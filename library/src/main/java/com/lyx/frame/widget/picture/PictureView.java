package com.lyx.frame.widget.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * PictureView
 * <p>
 * Created by luoyingxing on 2017/5/2.
 */
public class PictureView extends IPictureView {
    private static final int TIME = 10000;

    public PictureView(Context context) {
        this(context, null);
    }

    public PictureView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public PictureView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected boolean verifyDrawable(Drawable dr) {
        super.verifyDrawable(dr);
        return false;
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    /**
     * 可加载网络图片
     *
     * @param url String
     */
    public void setImageUrl(final String url) {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    URL urlRequest = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) urlRequest.openConnection();
                    conn.setDoInput(true);
                    conn.setConnectTimeout(TIME);
                    conn.setReadTimeout(TIME);
                    conn.setReadTimeout(TIME);

                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                    subscriber.onNext(bitmap);
                    is.close();
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable("加载失败"));
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        setImageBitmap(bitmap);
                    }
                });
    }
}