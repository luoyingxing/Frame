package com.lyx.frame.frame.network;

import com.lyx.frame.App;
import com.yolanda.nohttp.Headers;

import java.util.Map;

/**
 * ApiRequest
 * <p/>
 * Created by luoyingxing on 2017/5/23.
 */
public class ApiRequest<T> extends GsonRequest<T> {
    /**
     * CONTENT_TYPE
     */
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";

    public ApiRequest(String url) {
        super(url);
    }

    public ApiRequest(String url, Map<String, Object> params) {
        super(url, params);
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

    public String getContentType() {
        return CONTENT_TYPE;
    }
}