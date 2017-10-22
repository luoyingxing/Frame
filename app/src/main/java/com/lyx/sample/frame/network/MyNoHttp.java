package com.lyx.sample.frame.network;

import android.app.Application;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

/**
 * MyNoHttp
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 */
public class MyNoHttp {
    private static RequestQueue mQueue = null;
    /**
     * Sets the read timeout time, Unit is a millisecond.
     */
    private static final int TIMEOUT = 10000;

    public static RequestQueue getRequestQueue() {
        return mQueue;
    }

    private MyNoHttp() {
    }

    public static MyNoHttp getInstance() {
        return NoHttpFactory.http;
    }

    private static class NoHttpFactory {
        private static MyNoHttp http = new MyNoHttp();
    }

    public static void initialize(Application application) {
        NoHttp.initialize(application);
        mQueue = NoHttp.newRequestQueue();
    }

    public static <T> void addRequest(Request<T> request) {
        addRequest(0, request);
    }

    public static <T> void addRequest(int what, final Request<T> request) {
        request.setReadTimeout(TIMEOUT);
        getInstance().getRequestQueue().add(what, request, new OnResponseListener<T>() {
            @Override
            public void onStart(int what) {
                if (request instanceof GsonRequest) {
                    ((GsonRequest) request).onStart(what);
                }
            }

            @Override
            public void onSucceed(int what, Response<T> response) {
                if (request instanceof GsonRequest) {
                    ((GsonRequest) request).parseResponse(response);
                    ((GsonRequest) request).onResponse(response.getHeaders(), response.getByteArray());
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                if (request instanceof GsonRequest) {
                    ((GsonRequest) request).onFailed(what, url, tag, exception, responseCode, networkMillis);
                }
            }

            @Override
            public void onFinish(int what) {
                if (request instanceof GsonRequest) {
                    ((GsonRequest) request).onFinish(what);
                }
            }
        });
    }

    public static <T> void addRequest(final FileRequest fileRequest, final Request<T> request) {
        request.setReadTimeout(TIMEOUT);
        getInstance().getRequestQueue().add(0x0008, request, new OnResponseListener<T>() {
            @Override
            public void onStart(int what) {
                fileRequest.onStart(what);
            }

            @Override
            public void onSucceed(int what, Response<T> response) {
                fileRequest.parseResponse(response);
                fileRequest.onResponse(response.getHeaders(), response.getByteArray());
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                fileRequest.onFailed(what, url, tag, exception, responseCode, networkMillis);
            }

            @Override
            public void onFinish(int what) {
                fileRequest.onFinish(what);
            }
        });
    }

    public static <T> void addRequest(int what, Request<T> request, OnResponseListener<T> responseListener) {
        request.setReadTimeout(TIMEOUT);
        getInstance().getRequestQueue().add(what, request, responseListener);
    }

    public static void cancelAll() {
        getInstance().getRequestQueue().cancelAll();
    }

}