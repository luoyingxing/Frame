package com.lyx.sample.frame.picture;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lyx.sample.R;

import java.util.ArrayList;
import java.util.List;


/**
 * PictureDialog
 * <p>
 * Created by luoyingxing on 2017/5/2.
 */
@SuppressWarnings("unused")
public class PictureDialog extends Dialog implements ViewPager.OnPageChangeListener {
    public static int LOCAL = 0x0001; //1: 本地图片
    public static int REMOTE = 0x0002; //2: 网络图片
    public static int PROCEDURE = 0x0003; //3: 程序内图片
    private Context mContext;
    private DialogViewPage mViewPager;
    private TextView mTextView;

    private int mType;
    private List<String> mUrlList = new ArrayList<>();
    private List<Integer> mIdList = new ArrayList<>();
    private ViewAdapter mAdapter;
    private int mPagerSize;
    private int mCurrentPage = 0;

    public PictureDialog(Context context) {
        this(context, R.style.PictureDialog);
    }

    public PictureDialog(Context context, int theme) {
        super(context, R.style.PictureDialog);
        this.mContext = context;
    }

    public void setUri(List<String> list, int type) {
        this.mUrlList = list;
        this.mType = type;
    }

    public void setId(List<Integer> list, int type) {
        this.mIdList = list;
        this.mType = type;
    }

    public void setId(int id, int type) {
        this.mIdList.clear();
        this.mIdList.add(id);
        this.mType = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = View.inflate(mContext, R.layout.layout_picture_dialog, null);
        mViewPager = (DialogViewPage) root.findViewById(R.id.pv_picture_contentPager);
        mTextView = (TextView) root.findViewById(R.id.tv_picture_dialog_tip);
        setContentView(root);

        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        showViewPage();
    }

    public void showPager(int position) {
        if (null == mAdapter) {
            mCurrentPage = position;
        } else {
            mViewPager.setCurrentItem(position);
        }
    }

    private void showViewPage() {
        if (!mUrlList.isEmpty()) {
            mPagerSize = mUrlList.size();
        } else if (!mIdList.isEmpty()) {
            mPagerSize = mIdList.size();
        }

        mTextView.setText("第1/" + mPagerSize + "张");
        mViewPager.addOnPageChangeListener(this);
        mAdapter = new ViewAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPage);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPage = position;
        mTextView.setText("第" + (position + 1) + "/" + mPagerSize + "张");
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class ViewAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPagerSize;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View root = LayoutInflater.from(mContext).inflate(R.layout.layout_picture_dialog_show, mViewPager, false);
            PictureView pictureView = (PictureView) root.findViewById(R.id.pv_picture_dialog);
            pictureView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.image_empty_fresco));
            switch (mType) {
                case 1:
                    pictureView.setImageURI(Uri.parse(mUrlList.get(position)));
                    break;
                case 2:
                    pictureView.setImageUri(mUrlList.get(position));
                    break;
                case 3:
                    pictureView.setImageResource(mIdList.get(position));
                    break;
            }
            container.addView(root);
            return root;
        }
    }

}