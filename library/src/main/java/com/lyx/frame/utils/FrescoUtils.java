package com.lyx.frame.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * FrescoUtils
 * <p>
 * Created by luoyingxing on 2017/5/18.
 */

public class FrescoUtils {
    private static RoundingParams circleParams;
    private static Context mContext;
    private static int mPlaceholderImage;

    static {
        circleParams = new RoundingParams();
        circleParams.setRoundAsCircle(true);
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setPlaceholderImage(@DrawableRes int placeholderImage) {
        mPlaceholderImage = placeholderImage;
    }

    public static RoundingParams getCircleParams() {
        return circleParams;
    }

    private FrescoUtils() {
    }

    public static void loadImageRes(Context context, @DrawableRes int resId, SimpleDraweeView view) {
        Uri uri = Uri.parse("res://" + context.getPackageName() + "/" + resId);
        loadImage(uri, view);
    }

    public static void loadImageResAsCircle(Context context, @DrawableRes int resId, SimpleDraweeView view) {
        Uri uri = Uri.parse("res://" + context.getPackageName() + "/" + resId);
        loadImageAsCircle(uri, view);
    }

    public static void loadImageFile(String filePath, SimpleDraweeView view) {
        Uri uri = Uri.parse("file://" + filePath);
        loadImage(uri, view);
    }

    public static void loadImageFileAsCircle(String filePath, SimpleDraweeView view) {
        Uri uri = Uri.parse("file://" + filePath);
        loadImageAsCircle(uri, view);
    }

    public static void loadNetImage(String url, SimpleDraweeView view) {
        Uri uri = Uri.parse(url);
        loadImage(uri, view);
    }

    public static void loadNetImageAsCircle(String url, SimpleDraweeView view) {
        Uri uri = Uri.parse(url);
        loadImageAsCircle(uri, view);
    }

    public static void loadImage(Uri uri, SimpleDraweeView view) {
        GenericDraweeHierarchy hierarchy =
                new GenericDraweeHierarchyBuilder(mContext.getResources())
                        .setFadeDuration(300)
                        .setPlaceholderImage(mContext.getResources().getDrawable(mPlaceholderImage), ScalingUtils.ScaleType.CENTER_INSIDE)
                        .setFailureImage(mContext.getResources().getDrawable(mPlaceholderImage), ScalingUtils.ScaleType.CENTER_INSIDE)
                        .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE)
                        .build();
        view.setHierarchy(hierarchy);
        view.setImageURI(uri);
    }

    public static void loadImage(Uri uri, SimpleDraweeView view, int width, int height) {
        GenericDraweeHierarchy hierarchy =
                new GenericDraweeHierarchyBuilder(mContext.getResources())
                        .setFadeDuration(300)
                        .setPlaceholderImage(mContext.getResources().getDrawable(mPlaceholderImage), ScalingUtils.ScaleType.CENTER_INSIDE)
                        .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE)
                        .build();
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(view.getController())
                .setImageRequest(request)
                .build();

        view.setHierarchy(hierarchy);
        view.setController(controller);
        view.setImageURI(uri);
    }

    /**
     * 加载图片为圆形图片
     *
     * @param uri  图片的uri
     * @param view 要加载的视图
     */
    public static void loadImageAsCircle(Uri uri, SimpleDraweeView view) {
        GenericDraweeHierarchy hierarchy =
                new GenericDraweeHierarchyBuilder(mContext.getResources())
                        .setFadeDuration(300)
                        .setRoundingParams(circleParams)
                        .setPlaceholderImage(mContext.getResources().getDrawable(mPlaceholderImage), ScalingUtils.ScaleType.CENTER_INSIDE)
                        .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE)
                        .build();
        view.setHierarchy(hierarchy);
        view.setImageURI(uri);
    }

    /**
     * 加载Gif资源
     *
     * @param resId Gif的资源Id
     * @param view  要填充的View
     */
    public static void loadGifRes(Context context, @DrawableRes int resId, SimpleDraweeView view) {
        Uri uri = Uri.parse("res://" + context.getPackageName() + "/" + resId);
        loadGif(uri, view);
    }

    /**
     * 加载Gif文件
     *
     * @param filePath Gif的路径
     * @param view     要填充的View
     */
    public static void loadGifFile(String filePath, SimpleDraweeView view) {
        Uri uri = Uri.parse("file://" + filePath);
        loadGif(uri, view);
    }

    /**
     * 加载网络Gif
     *
     * @param url  Gif的URI
     * @param view 要填充的View
     */
    public static void loadNetGif(String url, SimpleDraweeView view) {
        Uri uri = Uri.parse(url);
        loadGif(uri, view);
    }

    /**
     * 加载Gif
     *
     * @param uri  Gif的URI
     * @param view 要填充的View
     */
    public static void loadGif(Uri uri, SimpleDraweeView view) {
        GenericDraweeHierarchy hierarchy =
                new GenericDraweeHierarchyBuilder(mContext.getResources())
                        .setFadeDuration(300)
                        .setPlaceholderImage(mContext.getResources().getDrawable(mPlaceholderImage), ScalingUtils.ScaleType.CENTER_INSIDE)
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setRetainImageOnFailure(true)
                .setAutoPlayAnimations(true)
                .build();
        view.setHierarchy(hierarchy);
        view.setController(controller);
    }

    public static void loadGif(Uri uri, SimpleDraweeView view, int width, int height) {
        GenericDraweeHierarchy hierarchy =
                new GenericDraweeHierarchyBuilder(mContext.getResources())
                        .setFadeDuration(300)
                        .setPlaceholderImage(mContext.getResources().getDrawable(mPlaceholderImage), ScalingUtils.ScaleType.CENTER_INSIDE)
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .build();

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setRetainImageOnFailure(true)
                .setAutoPlayAnimations(true)
                .setImageRequest(request)
                .setOldController(view.getController())
                .build();

        view.setHierarchy(hierarchy);
        view.setController(controller);
    }

    /**
     * 清除缓存
     *
     * @param uri 图片的URI
     */
    public static void clearDiskCache(Uri uri) {
        Fresco.getImagePipeline().evictFromDiskCache(uri);
    }

    public static void clearMemoryCache(Uri uri) {
        Fresco.getImagePipeline().evictFromMemoryCache(uri);
    }

    public static void clearCache(Uri uri) {
        clearDiskCache(uri);
        clearMemoryCache(uri);
    }

    /**
     * 清除文件的磁盘缓存
     *
     * @param path 文件路径
     */
    public static void clearFileDiskCache(String path) {
        Uri uri = Uri.parse("file://" + path);
        clearDiskCache(uri);
    }

    /**
     * 清除文件的内存缓存
     *
     * @param path 文件路径
     */
    public static void clearFileMemoryCache(String path) {
        Uri uri = Uri.parse("file://" + path);
        clearMemoryCache(uri);
    }

    /**
     * 清除文件的内存和磁盘缓存
     *
     * @param path 文件路径
     */
    public static void clearFileCache(String path) {
        Uri uri = Uri.parse("file://" + path);
        clearCache(uri);
    }

    /**
     * 清除网络图片的磁盘缓存
     *
     * @param url 链接
     */
    public static void clearUrlDiskCache(String url) {
        Uri uri = Uri.parse(url);
        clearDiskCache(uri);
    }

    /**
     * 清除网络图片的内存缓存
     *
     * @param url 图片链接
     */
    public static void clearUrlMemoryCache(String url) {
        Uri uri = Uri.parse(url);
        clearMemoryCache(uri);
    }

    /**
     * 清除网络图片的内存和磁盘缓存
     *
     * @param url 图片链接
     */
    public static void clearUrlCache(String url) {
        Uri uri = Uri.parse(url);
        clearCache(uri);
    }
}
