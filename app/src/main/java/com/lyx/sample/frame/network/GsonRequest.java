package com.lyx.sample.frame.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lyx.sample.App;
import com.lyx.sample.Constant;
import com.lyx.frame.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.RestRequest;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * GsonRequest
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 */
public class GsonRequest<T> extends RestRequest<T> {
    /**
     * TAG
     */
    private static final String TAG = "GsonRequest";
    /**
     * ACCEPT
     */
    protected static final String ACCEPT = "application/json";
    /**
     * Request method.
     */
    private RequestMethod mRequestMethod;
    /**
     * URL of this request.
     */
    private String mUrl;
    /**
     * Param collection.
     */
    private Map<String, Object> mParamKeyValues;

    private Context mContext;

    public GsonRequest(String url) {
        super(url);
        mUrl = url;
        mParamKeyValues = new HashMap<>();
    }

    public GsonRequest(String url, Map<String, Object> params) {
        super(url, RequestMethod.GET);
        mUrl = url;
        mParamKeyValues = new HashMap<>();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                mParamKeyValues.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public String getAccept() {
        return ACCEPT;
    }

    @Override
    public String url() {
        return mUrl;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return mRequestMethod;
    }

    private String paramsToString(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        StringBuilder encodedParams = new StringBuilder();

        try {
            for (String key : map.keySet()) {
                encodedParams.append(URLEncoder.encode(key, getParamsEncoding()));
                encodedParams.append('=');
                Object value = map.get(key);
                if (null != value) {
                    encodedParams.append(URLEncoder.encode(value.toString(), getParamsEncoding()));
                }
                encodedParams.append('&');
            }

            if (0 < encodedParams.length()) {
                encodedParams = encodedParams.deleteCharAt(encodedParams.length() - 1);
            }

            return encodedParams.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GsonRequest<T> addHeaders(String key, String value) {
        addHeader(key, value);
        return this;
    }

    public GsonRequest<T> addParam(String key, Object value) {
        if (mParamKeyValues == null) {
            mParamKeyValues = new HashMap<>();
        }
        mParamKeyValues.put(key, value);
        return this;
    }

    public GsonRequest<T> get() {
        mRequestMethod = RequestMethod.GET;

        if (mParamKeyValues != null) {
            mUrl = mParamKeyValues.keySet().size() == 0 ? mUrl : mUrl + (mUrl.contains("?") ? "&" : "?") + paramsToString(mParamKeyValues);
        }

        Log.i(TAG, "Get: " + mUrl);
        return send();
    }

    public GsonRequest<T> post() {
        mRequestMethod = RequestMethod.POST;
        Log.i(TAG, "Post: " + mUrl);
        Log.i(TAG, "Body: " + paramsToString(mParamKeyValues));

        setRequestBody(paramsToString(mParamKeyValues));
        return send();
    }

    private GsonRequest<T> send() {
        MyNoHttp.addRequest(this);
        return this;
    }

    @Override
    public T parseResponse(String url, Headers responseHeaders, byte[] responseBody) {
        if (responseBody == null || responseBody.length == 0) {
            return null;
        }

        String json = null;

        try {
            json = new String(responseBody, getParamsEncoding());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (json == null) {
            return null;
        }

        try {
            return new Gson().fromJson(json, getType());
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * 结果回调方法，运行在主线程的方法，可以直接更新UI
     *
     * @param response 服务器返回的结果
     * @return 实体类型
     */
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
                        if (ApiMsg.LOGOUT == apiMsg.getCode()) {
                            FileUtils.removePref(Constant.PREFS_LOGIN_COOKIE);
                            FileUtils.removePref(Constant.PREFS_LOGIN_ACCOUNT);
                            FileUtils.removePref(Constant.PREFS_LOGIN_PASSWORD);
                            App.getApp().setLoginCookie(null);
                            App.getApp().setUserInfo(null);
                        }
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
            Toast.makeText(mContext, apiMsg.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void onFinish(int what) {
    }

    protected void onResponse(Headers headers, byte[] responseBody) {
    }

    protected void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
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

    public GsonRequest<T> showErrorToast(Context context) {
        mContext = context;
        return this;
    }

    protected Context getContext() {
        return mContext == null ? App.getAppContext() : mContext;
    }
}