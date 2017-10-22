package com.lyx.sample.frame;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyx.sample.App;
import com.lyx.sample.R;
import com.lyx.sample.frame.network.MyNoHttp;
import com.lyx.sample.frame.ui.view.BaseToolBar;
import com.lyx.sample.frame.ui.view.LoadingDialog;
import com.lyx.sample.utils.Logger;
import com.lyx.sample.utils.NetworkUtils;


/**
 * BaseFragment
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 */
public class BaseFragment extends HandleFragment {
    protected String mTag;
    protected Logger mLog;
    private BaseActivity mActivity;
    protected LoadingDialog mDialog;

    private BaseToolBar mToolbar;

    @Override
    public void onAttach(Context context) {
        mTag = getClass().getSimpleName();
        mLog = new Logger(mTag, Log.VERBOSE);
        mLog.v("OnAttach()");
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLog.v("onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLog.v("onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mLog.v("onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
        initToolbar(getToolbar());
    }

    @Override
    public void onStart() {
        mLog.v("onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        mLog.v("onResume()");
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        mLog.v("onHiddenChanged()");
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onRestart() {
        mLog.v("onRestart()");
        super.onRestart();
    }

    @Override
    public void onSwitch() {
        mLog.v("onSwitch()");
        super.onSwitch();
        MyNoHttp.cancelAll();
    }

    @Override
    public void onPause() {
        mLog.v("onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        mLog.v("onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        mLog.v("onDestroyView()");
        super.onDestroyView();
        hideIME();
        hideDialog();
    }

    @Override
    public void onDestroy() {
        mLog.v("onDestroy()");
        super.onDestroy();
        System.gc();
    }

    @Override
    public void onDetach() {
        mLog.v("onDetach()");
        super.onDetach();
    }

    /**
     * 子类可重载该方法,以监听物理返回键.
     *
     * @return 如果希望覆盖默认实现, 则返回true
     */
    public boolean onBackPressed() {
        return false;
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    protected void startForFragmentResult(int requestCode, int resultCode, Intent data) {
        getBase().startForFragmentResult(requestCode, resultCode, data);
    }

    private Toast mToast = null;

    protected void showToast(Object msg) {
        if (mToast != null) {
            mToast.cancel();
            mToast = Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT);
        } else {
            mToast = Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    protected void showLongToast(Object msg) {
        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_LONG).show();
    }

    public void showNetworkError() {
        showToast(getResources().getString(R.string.msg_network_error));
    }

    public void showDialog() {
        showDialog(getResources().getString(R.string.msg_network_loading));
    }

    public void showDialog(String message) {
        hideDialog();
        mDialog = new LoadingDialog(getBase(), message);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    public void hideDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void showIME() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(getActivity().getCurrentFocus(), 0);
        mLog.d("showIME()");
    }

    public void showIMEForce(View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                showIME();
            }
        }, 100);
    }

    public void hideIME() {
        mLog.d("inputMode=" + getActivity().getWindow().getAttributes().softInputMode);
        if (getActivity().getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            return;
        }
        mLog.d("hideIME()");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = getActivity().getCurrentFocus();
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            mLog.d("hideIME() failed");
        }
    }

    public boolean checkNetwork() {
        if (NetworkUtils.isNetConnected()) {
            return true;
        }
        showNetworkError();
        return false;
    }

    public BaseActivity getBase() {
        return mActivity;
    }

    public void goBack() {
        getBase().goBack();
    }

    /**
     * @return 已登录返回true
     */
    public boolean checkLogin() {
        String cookie = App.getApp().getLoginCookie();
        return !TextUtils.isEmpty(cookie);
    }

    /**
     * @return 已登录返回true, 如果未登录，且开启autoJump，将自动跳转至登录界面
     */
    public boolean checkLogin(boolean autoJump) {
        if (!checkLogin()) {
            if (autoJump) {
                //跳转至登录界面
//                Intent loginIntent = new Intent(getActivity(), BaseActivity.class);
//                loginIntent.putExtra(Constant.ARGS_FRAGMENT_NAME, LoginFragment.class.getName());
//                getActivity().startActivity(loginIntent);
            }
            return false;
        }
        return true;
    }

    /**
     * this method recall in onActivityCreated
     *
     * @param toolbar BaseToolBar
     */
    private void initToolbar(BaseToolBar toolbar) {
        if (null != toolbar) {
            mToolbar = toolbar;
            mToolbar.init(this);
        }
    }

    public void setLocationOnClickListener(View.OnClickListener listener) {
        if (null != mToolbar) {
            mToolbar.setLocationOnClickListener(listener);
        }
    }

    public void showBackView(boolean show) {
        if (null != mToolbar) {
            mToolbar.showBackView(show);
        }
    }

    public void setBackDrawable(Drawable drawable) {
        if (null != mToolbar) {
            mToolbar.setBackDrawable(drawable);
        }
    }

    public void setBackResource(int resId) {
        if (null != mToolbar) {
            mToolbar.setBackResource(resId);
        }
    }

    public void setTitle(CharSequence text) {
        if (null != mToolbar) {
            mToolbar.setTitle(text);
        }
    }

    public void setTitleColor(int color) {
        if (null != mToolbar) {
            mToolbar.setTitleColor(color);
        }
    }

    public void setRightText(CharSequence text) {
        if (null != mToolbar) {
            mToolbar.setRightText(text);
        }
    }

    public TextView getRightTextView() {
        return mToolbar.getRightTextView();
    }

    public void setRightImage(Drawable drawable) {
        if (null != mToolbar) {
            mToolbar.setRightImage(drawable);
        }
    }

    public void setRightResource(int resId) {
        if (null != mToolbar) {
            mToolbar.setRightResource(resId);
        }
    }

    public void setOnRightImageClickListener(View.OnClickListener listener) {
        if (null != mToolbar) {
            mToolbar.setOnRightImageListener(listener);
        }
    }

    public void setOnRightTextClickListener(View.OnClickListener listener) {
        if (null != mToolbar) {
            mToolbar.setOnRightTextListener(listener);
        }
    }

    public RelativeLayout getCenterLayout() {
        return mToolbar.getCenterLayout();
    }
}