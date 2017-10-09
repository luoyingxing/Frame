package com.lyx.frame.frame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.lyx.frame.Constant;
import com.lyx.frame.R;
import com.lyx.frame.utils.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * BaseActivity
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 * <p>
 * Major update, change ActionBar to Toolbar. on 2017/6/15.
 */
public class BaseActivity extends AppCompatActivity implements HandledInterface {
    private HandleFragment mBackHandedFragment;
    private String mTag;
    protected Logger mLog;
    private List<HandleFragment> mFragmentList;

    protected int getLayout() {
        return R.layout.activity_base;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTag = getClass().getSimpleName();
        mLog = new Logger(mTag, Log.VERBOSE);
        mLog.d("onCreate");

        mFragmentList = new ArrayList<>();

        setContentView(getLayout());
        String fragmentName = getIntent().getStringExtra(Constant.ARGS_FRAGMENT_NAME);
        switchFragment(fragmentName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLog.d("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mLog.d("onRestart()");
        //TODO 当跳转去分享等打开第三方应用程序，会调用onSaveInstanceState保存fragment状态，结果就造成重复创建，但是缓存的List也要清空，才能解决问题
        if (null != mFragmentList) {
            mFragmentList.clear();
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mLog.d("onRestoreInstanceState()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLog.d("onResume()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLog.d("onSaveInstanceState()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLog.d("onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLog.d("onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLog.d("onDestroy()");
    }

    @Override
    public void setSelectedFragment(HandleFragment selectedFragment) {
        mLog.d("setSelectedFragment()");
        mBackHandedFragment = selectedFragment;
        mFragmentList.add(mBackHandedFragment);
    }

    private int mRequestCode;
    private int mResultCode;
    private Intent mData;

    @Override
    public void dispatchFragmentResult() {
        if (mRequestCode != 0) {
            mBackHandedFragment.onFragmentResult(mRequestCode, mResultCode, mData);
            mRequestCode = 0;
            mResultCode = 0;
            mData = null;
        }
    }

    public void startForFragmentResult(int requestCode, int resultCode, Intent data) {
        this.mRequestCode = requestCode;
        this.mResultCode = resultCode;
        this.mData = data;
    }

    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            goBack();
        }
    }

    public void switchFragment(String fragmentName) {
        if (fragmentName != null) {
            Fragment fragment = Fragment.instantiate(this, fragmentName, getIntent().getExtras());
            switchFragment(fragment);
        }
    }

    public void switchFragment(Fragment fragment) {
        switchFragment(fragment, false);
    }

    public void switchFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_right_in,
                R.anim.fragment_left_out,
                R.anim.fragment_left_in,
                R.anim.fragment_right_out
        );

        if (null != mFragmentList && 0 != mFragmentList.size()) {
            if (addToBackStack) {
                transaction.hide(mFragmentList.get(mFragmentList.size() - 1));
            } else {
                transaction.remove(mFragmentList.get(mFragmentList.size() - 1));
                mFragmentList.remove(mFragmentList.size() - 1);
            }
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(getContainer(), fragment);
        }
        transaction.commit();
    }

    public void goBack() {
        int count = mFragmentList.size();
        if (count < 2) {
            mFragmentList.clear();
            finish();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.fragment_left_in,
                    R.anim.fragment_right_out,
                    R.anim.fragment_right_in,
                    R.anim.fragment_left_out
            );

            transaction.remove(mBackHandedFragment).show(mFragmentList.get(count - 2));
            mFragmentList.remove(count - 1);
            mBackHandedFragment = mFragmentList.get(mFragmentList.size() - 1);
            transaction.commit();
        }
    }

    public int getContainer() {
        return R.id.base_container;
    }

    protected void showToast(Object msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(Object msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_LONG).show();
    }
}