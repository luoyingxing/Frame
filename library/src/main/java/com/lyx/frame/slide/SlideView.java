package com.lyx.frame.slide;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lyx.frame.R;
import com.lyx.frame.annotation.ImageUrl;
import com.lyx.frame.slide.animator.TransFormFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * SlideView
 * 当图片数量超过一定数量时，底部样式将会切换成文字显示样式
 * <p>
 * 功能优点：
 * -> 实现自动无限循环切换；
 * -> 当图片数量小于2时，手动切换时，不会出现空白页面；
 * -> 支持点击事件；
 * -> 支持通过xml配置相关参数；
 * -> 最重要的一点，支持泛型数据源，通过注解配置和反射机制实现轮播效果
 * <p/>
 * author:  luoyingxing
 * date: 2017/10/16.
 */
public class SlideView<T> extends FrameLayout {
    private Context mContext;
    private ViewPager mViewPager;
    /**
     * 超过图片最大数量，底部样式将智能切换成文字样式
     */
    private static final int MAX_COUNT = 10;
    /**
     * 决定于MAX_COUNT的值
     */
    private boolean mIsTextStyle;
    /**
     * 底部样式的背景
     */
    private int mTextBackground;
    /**
     * 底部样式的颜色
     */
    private int mTextColor;
    /**
     * 轮播图数量
     */
    private int mImageCount;
    /**
     * 轮播图切换时间间隔（单位：秒）
     */
    private int mIntervalTime;
    /**
     * 数据源
     */
    private List<T> mSource;
    /**
     * 当前滚动页
     */
    private int mCurrentItem = 0;
    /**
     * 执行轮播任务
     */
    private ScheduledExecutorService mScheduled;
    /**
     * 相对宽度
     */
    private int mSlideWidthScale = 2;
    /**
     * 相对高度
     */
    private int mSlideHeightScale = 1;
    /**
     * 底部小圆圈未选中时的图标
     */
    private int mDotNormalId;
    /**
     * 底部小圆圈选中时的图标
     */
    private int mDotFocusId;
    /**
     * 轮播图的占位图
     */
    private int mPlaceHolderImage;

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
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.SlideView, 0, 0);
        mSlideWidthScale = array.getInteger(R.styleable.SlideView_widthScale, 2);
        mSlideHeightScale = array.getInteger(R.styleable.SlideView_heightScale, 1);
        mIntervalTime = array.getInteger(R.styleable.SlideView_intervalTime, 3);

        mDotNormalId = array.getResourceId(R.styleable.SlideView_dotNormal, 0);
        mDotFocusId = array.getResourceId(R.styleable.SlideView_dotFocus, 0);

        mPlaceHolderImage = array.getResourceId(R.styleable.SlideView_placeHolderImage, 0);

        mTextBackground = array.getColor(R.styleable.SlideView_textBackground, 0x40000000);
        mTextColor = array.getColor(R.styleable.SlideView_textColor, 0xFFFFFFFF);

        array.recycle();
    }

    /**
     * 默认宽高比 2/1
     *
     * @param widthScale  相对宽度
     * @param heightScale 相对高度
     */
    public SlideView setSlideScale(int widthScale, int heightScale) {
        mSlideWidthScale = widthScale;
        mSlideHeightScale = heightScale;
        return this;
    }

    /**
     * 设置轮播时间间隔
     *
     * @param intervalTime 时间间隔（单位：秒）
     * @return SlideView
     */
    public SlideView setIntervalTime(int intervalTime) {
        mIntervalTime = intervalTime;
        return this;
    }

    /**
     * 设置底部小圆圈未选中时的图标
     *
     * @param normalIcon icon
     * @return SlideView
     */
    public SlideView setDotNormalIcon(@IdRes int normalIcon) {
        mDotNormalId = normalIcon;
        return this;
    }

    /**
     * 设置底部小圆圈选中时的图标
     *
     * @param focusIcon icon
     * @return SlideView
     */
    public SlideView setDotFocusIcon(@IdRes int focusIcon) {
        mDotFocusId = focusIcon;
        return this;
    }

    /**
     * 设置轮播图片的占位符
     *
     * @param image image
     * @return SlideView
     */
    public SlideView setPlaceHolderImage(@IdRes int image) {
        mPlaceHolderImage = image;
        return this;
    }

    /**
     * 设置底部文字样式的背景颜色
     *
     * @param background ColorRes
     * @return SlideView
     */
    public SlideView setTextBackground(@ColorRes int background) {
        mTextBackground = background;
        return this;
    }

    /**
     * 设置底部文字样式的字体颜色
     *
     * @param color ColorRes
     * @return SlideView
     */
    public SlideView setTextColor(@ColorRes int color) {
        mTextColor = color;
        return this;
    }

    /**
     * 初始化数据源
     *
     * @param dataSource 数据源
     */
    public void init(List<T> dataSource) {
        if (null == dataSource || 0 == dataSource.size()) {
            return;
        }

        mSource = dataSource;
        mImageCount = mSource.size();
        mIsTextStyle = mImageCount >= MAX_COUNT;

        initUI();
    }

    private TextView mTextStyleTV;

    /**
     * 初始化视图中的各个组件
     */
    private void initUI() {
        stopPlay();
        removeAllViews();
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        int width = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int height = (width / mSlideWidthScale) * mSlideHeightScale;
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(width, height));

        mViewPager = new ViewPager(mContext);
        mViewPager.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        LinearLayout linearLayout = new LinearLayout(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        linearLayout.setPadding(0, 12, 0, 12);
        linearLayout.setLayoutParams(layoutParams);

        List<SimpleDraweeView> imageViewList = new ArrayList<>();
        List<View> dotViewList = new ArrayList<>();

        for (int i = 0; i < mImageCount; i++) {
            SimpleDraweeView view = new SimpleDraweeView(mContext);
            view.setTag(getImageUrl(mSource.get(i)));
            view.setOnClickListener(new OnItemListener(mSource.get(i), i));
            imageViewList.add(view);
            if (!mIsTextStyle) {
                ImageView dotView = new ImageView(mContext);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(24, 24);
                param.leftMargin = 4;
                param.rightMargin = 4;
                linearLayout.addView(dotView, param);
                dotViewList.add(dotView);
            }
        }

        //当图片数量为2时，将数量翻倍，避免滑动出现白屏
        if (2 == mImageCount) {
            for (int i = 0; i < mImageCount; i++) {
                SimpleDraweeView view = new SimpleDraweeView(mContext);
                view.setTag(getImageUrl(mSource.get(i)));
                view.setOnClickListener(new OnItemListener(mSource.get(i), i));
                imageViewList.add(view);
            }
        }

        relativeLayout.addView(mViewPager);

        if (mIsTextStyle) {
            mTextStyleTV = new TextView(mContext);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mTextStyleTV.setPadding(20, 12, 20, 12);
            mTextStyleTV.setLayoutParams(params);
            mTextStyleTV.setBackgroundColor(mTextBackground);
            mTextStyleTV.setTextColor(mTextColor);
            mTextStyleTV.setText("第1/" + mImageCount + "页");
            mTextStyleTV.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            relativeLayout.addView(mTextStyleTV);
        } else {
            relativeLayout.addView(linearLayout);
        }

        addView(relativeLayout);

        mViewPager.setFocusable(true);
        mViewPager.setPageTransformer(true, TransFormFactory.getTransFormer(5));
        mViewPager.setAdapter(new SlideAdapter(imageViewList));

        //当图为数量为1时，不滚动
        if (1 == mImageCount) {
            mViewPager.setCurrentItem(0);
        } else {
            mViewPager.addOnPageChangeListener(new SlidePageChangeListener(dotViewList));
            mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
            startPlay();
        }
    }

    /**
     * 开启轮播任务
     */
    private void startPlay() {
        if (null == mScheduled) {
            mScheduled = Executors.newSingleThreadScheduledExecutor();
        }
        mScheduled.scheduleAtFixedRate(new SlideShowTask(), 1, mIntervalTime, TimeUnit.SECONDS);
    }

    /**
     * 结束轮播任务
     */
    private void stopPlay() {
        if (null != mScheduled) {
            mScheduled.shutdown();
            mScheduled = null;
        }
    }

    private class SlideAdapter extends PagerAdapter {
        List<SimpleDraweeView> list;

        public SlideAdapter(List<SimpleDraweeView> imageList) {
            list = imageList;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //TODO Warning：not call removeView here, just do nothing
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mImageCount == 1) {
                //当图为数量为1时，不滚动
                position = 0;
            } else if (mImageCount == 2) {
                position %= mImageCount * 2;
            } else {
                position %= mImageCount;
            }

            if (position < 0) {
                position = mImageCount + position;
            }

            SimpleDraweeView imageView = list.get(position);
            setImageLoader(imageView);

            //TODO remove the parent,or throw IllegalStateException
            ViewParent viewParent = imageView.getParent();
            if (null != viewParent) {
                ((ViewGroup) viewParent).removeView(imageView);
            }

            container.addView(imageView);
            return imageView;
        }

        @Override
        public int getCount() {
            if (1 == mImageCount) {
                //当图为数量为1时，不滚动
                return 1;
            } else {
                return Integer.MAX_VALUE;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /**
     * 初始化imageView相关配置参数
     *
     * @param imageView SimpleDraweeView
     */
    private void setImageLoader(SimpleDraweeView imageView) {
        if (null == imageView.getTag()) {
            return;
        }
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(imageView.getTag().toString()))
                .setTapToRetryEnabled(true)
                .setAutoPlayAnimations(true)
                .setOldController(imageView.getController())
                .build();

        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setFadeDuration(800)
                .setFailureImage(getResources().getDrawable(mPlaceHolderImage))
                .setPlaceholderImage(getResources().getDrawable(mPlaceHolderImage))
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                .build();

        imageView.setHierarchy(hierarchy);
        imageView.setController(controller);
    }

    private class SlidePageChangeListener implements ViewPager.OnPageChangeListener {
        List<View> list;

        public SlidePageChangeListener(List<View> dotViewList) {
            list = dotViewList;
        }

        @Override
        public void onPageSelected(int position) {
            mCurrentItem = position;
            position %= mImageCount;

            if (mIsTextStyle) {
                mTextStyleTV.setText("第" + (position + 1) + "/" + mImageCount + "页");
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (i == position) {
                        (list.get(position)).setBackgroundResource(mDotFocusId);
                    } else {
                        (list.get(i)).setBackgroundResource(mDotNormalId);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
    }

    /**
     * 轮播图切换Runnable
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
     * 释放资源
     */
    private void releaseResources() {
        if (mHandler != null) {
            mHandler.removeMessages(0);
            mHandler = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopPlay();
        releaseResources();
    }

    private OnItemClickListener<T> mOnItemClickListener;

    /**
     * 设置轮播图的点击事件
     *
     * @param onItemClickListener OnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T result, int position);
    }

    private class OnItemListener implements OnClickListener {
        private T info;
        private int position;

        public OnItemListener(T info, int position) {
            this.info = info;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != mOnItemClickListener) {
                mOnItemClickListener.onItemClick(info, position);
            }
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

    private Type getType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType genericSuperclassType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = genericSuperclassType.getActualTypeArguments();
        return actualTypeArguments[0];
    }
}