package com.lyx.sample.frame.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.lyx.sample.App;
import com.lyx.sample.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yolanda.nohttp.BitmapBinary;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * FileRequest
 * <p>
 * Created by luoyingxing on 2017/7/26.
 */

public class FileRequest<T> extends StringRequest {
    private static final String TAG = "FileRequest";
    private Context mContext;

    public FileRequest(String url) {
        this(url, RequestMethod.POST);
    }

    public FileRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    @Override
    public Headers headers() {
        Headers headers = super.headers();
        String cookie = App.getApp().getLoginCookie();
        if (cookie != null) {
            headers.set(Headers.HEAD_KEY_COOKIE, cookie);
            headers.set(Headers.HEAD_KEY_CONTENT_TYPE, getContentType());
        }
        return headers;
    }

    public void uploadFile(String key, String filePath) {
        Request<T> request = (Request<T>) this;
        request.add(key, new FileBinary(new File(filePath)));
        MyNoHttp.addRequest(this, request);
        Log.e(TAG, "Post：" + url());
    }

    public void uploadBitmap(String key, Bitmap bitmap) {
        Request<T> request = (Request<T>) this;
        request.add(key, new BitmapBinary(bitmap, System.currentTimeMillis() + ".png"));
        MyNoHttp.addRequest(this, request);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        Log.e(TAG, "Post：" + url());
    }

    public void parseResponse(Response<T> response) {
        Headers responseHeaders = response.getHeaders();
        byte[] responseBody = response.getByteArray();

        if (responseBody != null && responseBody.length != 0) {
            String json = null;

            try {
                json = new String(responseBody, getParamsEncoding());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (json != null) {
                onResponse(responseHeaders, responseBody);
                Log.e(TAG, "parseResponse= " + json);

                if (ApiMsg.isApiMsg(json)) {
                    ApiMsg apiMsg = new Gson().fromJson(json, ApiMsg.class);
                    if (ApiMsg.OK == apiMsg.getCode()) {
                        onSuccess((T) apiMsg);
                    } else {
                        onError(apiMsg);
                    }
                } else {
                    try {
                        T obj = new Gson().fromJson(json, getType());
                        onSuccess(obj);
                    } catch (JsonSyntaxException e) {
                        onError(ErrMsg.parseError());
                    }
                }
            }
        }
    }

    protected void onStart(int what) {
    }

    protected void onSuccess(T result) {
    }

    protected void onError(ApiMsg apiMsg) {
        if (mContext != null) {
            Logger.showToast(mContext, apiMsg.getMsg());
        }
    }

    protected void onFinish(int what) {
    }

    protected void onResponse(Headers headers, byte[] responseBody) {
    }

    protected void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
    }

    public FileRequest<T> showErrorToast(Context context) {
        mContext = context;
        return this;
    }

    @SuppressWarnings("unchecked")
    protected Type getType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType genericSuperclassType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = genericSuperclassType.getActualTypeArguments();
        return actualTypeArguments[0];
    }
}
