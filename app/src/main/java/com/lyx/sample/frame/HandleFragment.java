package com.lyx.sample.frame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.lyx.sample.annotation.IdParser;
import com.lyx.sample.frame.ui.view.BaseToolBar;

/**
 * HandleFragment
 * Hosting Activity must implement HandledInterface
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 * <p>
 * update by luoyingxing on 2017/6/21, add startFragmentForResult and onFragmentResult method
 */
public abstract class HandleFragment extends Fragment {
    protected HandledInterface mHandledInterface;
    /**
     * Standard Fragment result: operation canceled.
     */
    public static final int RESULT_CANCELED = 0;
    /**
     * Standard Fragment result: operation succeeded.
     */
    public static final int RESULT_OK = -1;

    /**
     * 所有继承BackHandledFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
     * FragmentActivity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
     * 如果没有Fragment消息时FragmentActivity自己才会消费该事件
     */
    protected abstract boolean onBackPressed();

    /**
     * Fragment 回退时可调用
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        结果集
     *                    <p>
     *                    Receive the result from a previous call to
     *                    {@link #startForFragmentResult(int requestCode, int resultCode, Intent data)}.
     *                    This follows the related Fragment API as described there in
     *                    {@link HandleFragment#onFragmentResult(int requestCode, int resultCode, Intent data)}.
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    protected abstract void onFragmentResult(int requestCode, int resultCode, Intent data);

    /**
     * Call {@link HandleFragment#startForFragmentResult(int requestCode, int resultCode, Intent data)} from the fragment
     */
    protected abstract void startForFragmentResult(int requestCode, int resultCode, Intent data);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof HandledInterface)) {
            throw new ClassCastException("Hosting Activity must implement HandledInterface");
        } else {
            this.mHandledInterface = (HandledInterface) getActivity();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IdParser.inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mHandledInterface.setSelectedFragment(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onSwitch();
        } else {
            mHandledInterface.dispatchFragmentResult();
            onRestart();
        }
    }

    public void onSwitch() {
    }

    public void onRestart() {
    }

    /**
     * sub class must override this method,
     * and return BaseToolBar,
     * otherwise about BaseToolBar's method is invalid.
     *
     * @return BaseToolBar instance
     */
    public BaseToolBar getToolbar() {
        return null;
    }
}
