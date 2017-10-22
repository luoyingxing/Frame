package com.lyx.sample.frame.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lyx.sample.R;
import com.lyx.sample.frame.ui.viewpage.TransFormFactory;
import com.lyx.sample.utils.DpiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * SlideView
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 */
public class SlideView<T> extends FrameLayout {
    /**
     * image size
     */
    private final static int IMAGE_COUNT = 5;
    /**
     * switching time / second
     */
    private final static int TIME_INTERVAL = 3;
    /**
     * image List
     */
    private List<String> mImageList;
    /**
     * ImageView List
     */
    private List<SimpleDraweeView> mImageViewList;
    /**
     * dot list
     */
    private List<View> mDotViewList;
    /**
     * viewPage
     */
    private ViewPager mViewPager;
    /**
     * Current page
     */
    private int mCurrentItem = 0;
    /**
     * timed task
     */
    private ScheduledExecutorService mScheduled;
    /**
     * Context
     */
    private Context mContext;
    /**
     * handler
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mViewPager.setCurrentItem(mCurrentItem);
        }
    };

    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }


    private List<T> mSource;

    /**
     * call this method,introduction data
     * onItemClick() will recall onItemClick(T result,int position);
     *
     * @param dataSource dataSource
     */
    public void setDataSource(List<T> dataSource) {
        mSource = new ArrayList<>();
        mSource.addAll(dataSource);
    }

    /**
     * image's url
     *
     * @param image the url for image
     */
    public void init(List<String> image) {
        mImageList = new ArrayList<>();
        mImageViewList = new ArrayList<>();
        mDotViewList = new ArrayList<>();
        mImageList.addAll(image);

        if (mImageList.size() == 0) {
            return;
        }

        initImage();
    }

    /**
     * start slide
     */
    private void startPlay() {
        mScheduled = Executors.newSingleThreadScheduledExecutor();
        mScheduled.scheduleAtFixedRate(new SlideShowTask(), 1, TIME_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * stop slide
     */
    private void stopPlay() {
        mScheduled.shutdown();
    }

    /**
     * init slide image
     */
    private void initImage() {
        initUI();
    }

    /**
     * init ui
     */
    private void initUI() {
        if (mImageList == null || mImageList.size() == 0) {
            return;
        }
        LayoutInflater.from(mContext).inflate(R.layout.layout_home_slide, this, true);
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.ll_home_slide_dot);
        dotLayout.removeAllViews();

        mViewPager = (ViewPager) findViewById(R.id.vp_home_slide);
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;

        for (int i = 0; i < mImageList.size(); i++) {
            SimpleDraweeView view = new SimpleDraweeView(mContext);
            view.setTag(mImageList.get(i));
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(finalI);
                        if (mSource != null && mSource.size() > finalI) {
                            mOnItemClickListener.onItemClick(mSource.get(finalI), finalI);
                        }
                    }
                }
            });

            mImageViewList.add(view);
            ImageView dotView = new ImageView(mContext);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(18, 18);
            param.leftMargin = 4;
            param.rightMargin = 4;
            dotLayout.addView(dotView, param);
            dotLayout.setLayoutParams(params);
            mDotViewList.add(dotView);
        }

        mViewPager.setFocusable(true);
        mViewPager.setLayoutParams(params);
        ViewPager.PageTransformer pageTransformer = TransFormFactory.getTransFormer(5);
        mViewPager.setPageTransformer(true, pageTransformer);
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        startPlay();
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View container, int position, Object object) {
            //TODO Warning：not call removeView here
        }

        @Override
        public Object instantiateItem(View container, int position) {
            //get the position which View is need to show
            position %= mImageViewList.size();

            if (position < 0) {
                position = mImageViewList.size() + position;
            }
            SimpleDraweeView imageView = mImageViewList.get(position);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageView.getTag().toString()))
                    .setResizeOptions(new ResizeOptions(DpiUtils.getWidth(), DpiUtils.getWidth() / 3))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(imageView.getTag().toString()))
                    .setTapToRetryEnabled(true)
                    .setImageRequest(request)
                    .setAutoPlayAnimations(true)
                    .setOldController(imageView.getController())
                    .build();

            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(800)
                    .setFailureImage(getResources().getDrawable(R.mipmap.image_empty_fresco))
                    .setPlaceholderImage(getResources().getDrawable(R.mipmap.image_empty_fresco))
                    .build();

            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
            imageView.setHierarchy(hierarchy);
            imageView.setController(controller);

            //remove the parent,or throw IllegalStateException
            ViewParent vp = imageView.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(imageView);
            }

            ((ViewPager) container).addView(imageView);
            return imageView;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        boolean isAutoPlay = false;

        @Override
        public void onPageSelected(int position) {
            mCurrentItem = position;
            position %= mImageViewList.size();

            for (int i = 0; i < mDotViewList.size(); i++) {
                if (i == position) {
                    (mDotViewList.get(position)).setBackgroundResource(R.drawable.icon_home_slide_focus);
                } else {
                    (mDotViewList.get(i)).setBackgroundResource(R.drawable.icon_home_slide_normal);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    isAutoPlay = false;
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    isAutoPlay = true;
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
    }


    /**
     * Shuffling figure switch tasks
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            synchronized (mViewPager) {
                mCurrentItem++;
                mHandler.obtainMessage().sendToTarget();
            }
        }
    }

    /**
     * destroy Bitmaps
     */
    private void destroyBitmaps() {
        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = mImageViewList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }

    private OnItemClickListener<T> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position);

        /**
         * you must call setDataSource()
         *
         * @param result   T
         * @param position position
         */
        void onItemClick(T result, int position);
    }
}