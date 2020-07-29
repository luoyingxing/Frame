package com.lyx.frame.widget.picture;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lyx.frame.R;
import com.lyx.frame.annotation.ImageUrl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * PictureDialog
 * <p>
 * Created by luoyingxing on 2017/5/2.
 */
public class PictureDialog<T> extends Dialog implements ViewPager.OnPageChangeListener {
    public static int LOCAL = 0x0001; //1: 本地图片
    public static int REMOTE = 0x0002; //2: 网络图片
    public static int PROCEDURE = 0x0003; //3: 程序内图片
    private Context mContext;
    private DialogViewPage mViewPager;
    private TextView mTextView;
    private int mType;
    private List<T> mUrlList = new ArrayList<>();
    private List<Integer> mIdList = new ArrayList<>();
    private ViewAdapter mAdapter;
    private int mPagerSize;
    private int mCurrentPage = 0;
    private int mPalaceHolderImage;

    public PictureDialog(Context context) {
        this(context, R.style.PictureDialog);
    }

    public PictureDialog(Context context, OnSaveClickListener listener) {
        this(context, R.style.PictureDialog);
        this.mOnSaveClickListener = listener;
    }

    public PictureDialog(Context context, int theme) {
        super(context, R.style.PictureDialog);
        this.mContext = context;
    }

    /**
     * 泛型对象的图片地址必须用{@ImageUrl}标注
     *
     * @param UrlList List
     * @param type    LOCAL/REMOTE/PROCEDURE
     * @return PictureDialog
     */
    public PictureDialog setImageUrl(List<T> UrlList, int type) {
        this.mUrlList = UrlList;
        this.mType = type;
        return this;
    }

    public PictureDialog setImageId(List<Integer> list, int type) {
        this.mIdList = list;
        this.mType = type;
        return this;
    }

    public PictureDialog setImageId(@DrawableRes int id, int type) {
        this.mIdList.clear();
        this.mIdList.add(id);
        this.mType = type;
        return this;
    }

    public PictureDialog setPalaceHolderImage(@DrawableRes int palaceHolderImage) {
        mPalaceHolderImage = palaceHolderImage;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = View.inflate(mContext, R.layout.layout_picture_dialog, null);
        mViewPager = root.findViewById(R.id.pv_picture_contentPager);
        mTextView = root.findViewById(R.id.tv_picture_dialog_tip);
        root.findViewById(R.id.iv_picture_dialog_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSaveClickListener) {
                    mOnSaveClickListener.save(mCurrentPage);
                }
            }
        });
        setContentView(root);

        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        showViewPage();
    }

    private OnSaveClickListener mOnSaveClickListener;

    public interface OnSaveClickListener {
        void save(int position);
    }

    public PictureDialog showPosition(int position) {
        if (mAdapter == null) {
            mCurrentPage = position;
        } else {
            mViewPager.setCurrentItem(position);
        }
        return this;
    }

    private PictureDialog showViewPage() {
        if (!mUrlList.isEmpty()) {
            mPagerSize = mUrlList.size();
        } else if (!mIdList.isEmpty()) {
            mPagerSize = mIdList.size();
        }

        mTextView.setText("1/" + mPagerSize);
        mViewPager.addOnPageChangeListener(this);
        mAdapter = new ViewAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPage);
        return this;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPage = position;
        mTextView.setText((position + 1) + "/" + mPagerSize);
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
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View root = LayoutInflater.from(mContext).inflate(R.layout.layout_picture_dialog_show, mViewPager, false);
            PictureView pictureView = (PictureView) root.findViewById(R.id.pv_picture_dialog);
            if (0 != mPalaceHolderImage) {
                pictureView.setImageDrawable(mContext.getResources().getDrawable(mPalaceHolderImage));
            }
            switch (mType) {
                case 1:
                    pictureView.setImageURI(Uri.parse(getImageUrl(mUrlList.get(position))));
                    break;
                case 2:
                    pictureView.setImageUrl(getImageUrl(mUrlList.get(position)));
                    break;
                case 3:
                    pictureView.setImageResource(mIdList.get(position));
                    break;
            }
            container.addView(root);
            return root;
        }
    }

    private String getImageUrl(T obj) {
        if (obj instanceof String) {
            //如果是String类型，直接返回
            return (String) obj;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ImageUrl.class)) {
                try {
                    field.setAccessible(true);
                    return (String) field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }
}