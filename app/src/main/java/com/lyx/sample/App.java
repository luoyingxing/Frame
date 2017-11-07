package com.lyx.sample;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.lyx.sample.frame.exception.CrashHandler;
import com.lyx.sample.entity.UserInfo;
import com.lyx.sample.frame.network.MyNoHttp;
import com.lyx.sample.utils.FileUtils;

/**
 * App
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 */
public class App extends Application {
    private static App mApp;
    private String mLoginCookie;
    private UserInfo mUserInfo;

    public static Context getAppContext() {
        return mApp;
    }

    public static App getApp() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        MyNoHttp.initialize(this);
        Fresco.initialize(mApp);
        CrashHandler.getInstance().init(mApp);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public String getLoginCookie() {
        if (mLoginCookie == null) {
            mLoginCookie = FileUtils.getPref(Constant.PREFS_LOGIN_COOKIE);
        }
        return mLoginCookie;
    }

    public void setLoginCookie(String loginCookie) {
        this.mLoginCookie = loginCookie;
        if (loginCookie == null) {
            FileUtils.removePref(Constant.PREFS_LOGIN_COOKIE);
        } else {
            FileUtils.savePref(Constant.PREFS_LOGIN_COOKIE, loginCookie);
        }
    }

    public UserInfo getUserInfo() {
        if (mUserInfo == null) {
            String jsonStr = FileUtils.getPref(Constant.PREFS_USER_INFO);
            if (!jsonStr.isEmpty()) {
                mUserInfo = new Gson().fromJson(jsonStr, UserInfo.class);
            }
        }
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        if (userInfo == null) {
            FileUtils.removePref(Constant.PREFS_USER_INFO);
        } else {
            FileUtils.savePref(Constant.PREFS_USER_INFO, new Gson().toJson(userInfo));
        }

    }
}