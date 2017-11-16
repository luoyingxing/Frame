package com.lyx.frame.widget.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;

/**
 * IPictureView
 * <p>
 * Created by luoyingxing on 2017/5/2.
 */
public class IPictureView extends android.support.v7.widget.AppCompatImageView implements IPicture {
    private final PictureViewAttach mAttach;

    private ScaleType mPendingScaleType;

    public IPictureView(Context context) {
        this(context, null);
    }

    public IPictureView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public IPictureView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        super.setScaleType(ScaleType.MATRIX);
        mAttach = new PictureViewAttach(this);

        if (null != mPendingScaleType) {
            setScaleType(mPendingScaleType);
            mPendingScaleType = null;
        }
    }

    /**
     * @deprecated use {@link #setRotationTo(float)}
     */
    @Override
    public void setPhotoViewRotation(float rotationDegree) {
        mAttach.setRotationTo(rotationDegree);
    }

    @Override
    public void setRotationTo(float rotationDegree) {
        mAttach.setRotationTo(rotationDegree);
    }

    @Override
    public void setRotationBy(float rotationDegree) {
        mAttach.setRotationBy(rotationDegree);
    }

    @Override
    public boolean canZoom() {
        return mAttach.canZoom();
    }

    @Override
    public RectF getDisplayRect() {
        return mAttach.getDisplayRect();
    }

    @Override
    public Matrix getDisplayMatrix() {
        return mAttach.getDrawMatrix();
    }

    @Override
    public boolean setDisplayMatrix(Matrix finalRectangle) {
        return mAttach.setDisplayMatrix(finalRectangle);
    }

    @Override
    @Deprecated
    public float getMinScale() {
        return getMinimumScale();
    }

    @Override
    public float getMinimumScale() {
        return mAttach.getMinimumScale();
    }

    @Override
    @Deprecated
    public float getMidScale() {
        return getMediumScale();
    }

    @Override
    public float getMediumScale() {
        return mAttach.getMediumScale();
    }

    @Override
    @Deprecated
    public float getMaxScale() {
        return getMaximumScale();
    }

    @Override
    public float getMaximumScale() {
        return mAttach.getMaximumScale();
    }

    @Override
    public float getScale() {
        return mAttach.getScale();
    }

    @Override
    public ScaleType getScaleType() {
        return mAttach.getScaleType();
    }

    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        mAttach.setAllowParentInterceptOnEdge(allow);
    }

    @Override
    @Deprecated
    public void setMinScale(float minScale) {
        setMinimumScale(minScale);
    }

    @Override
    public void setMinimumScale(float minimumScale) {
        mAttach.setMinimumScale(minimumScale);
    }

    @Override
    @Deprecated
    public void setMidScale(float midScale) {
        setMediumScale(midScale);
    }

    @Override
    public void setMediumScale(float mediumScale) {
        mAttach.setMediumScale(mediumScale);
    }

    @Override
    @Deprecated
    public void setMaxScale(float maxScale) {
        setMaximumScale(maxScale);
    }

    @Override
    public void setMaximumScale(float maximumScale) {
        mAttach.setMaximumScale(maximumScale);
    }

    @Override
    // setImageBitmap calls through to this method
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (null != mAttach) {
            mAttach.update();
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (null != mAttach) {
            mAttach.update();
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if (null != mAttach) {
            mAttach.update();
        }
    }

    @Override
    public void setOnMatrixChangeListener(PictureViewAttach.OnMatrixChangedListener listener) {
        mAttach.setOnMatrixChangeListener(listener);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        mAttach.setOnLongClickListener(l);
    }

    @Override
    public void setOnPhotoTapListener(PictureViewAttach.OnPhotoTapListener listener) {
        mAttach.setOnPhotoTapListener(listener);
    }

    @Override
    public PictureViewAttach.OnPhotoTapListener getOnPhotoTapListener() {
        return mAttach.getOnPhotoTapListener();
    }

    @Override
    public void setOnViewTapListener(PictureViewAttach.OnViewTapListener listener) {
        mAttach.setOnViewTapListener(listener);
    }

    @Override
    public PictureViewAttach.OnViewTapListener getOnViewTapListener() {
        return mAttach.getOnViewTapListener();
    }

    @Override
    public void setScale(float scale) {
        mAttach.setScale(scale);
    }

    @Override
    public void setScale(float scale, boolean animate) {
        mAttach.setScale(scale, animate);
    }

    @Override
    public void setScale(float scale, float focalX, float focalY, boolean animate) {
        mAttach.setScale(scale, focalX, focalY, animate);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (null != mAttach) {
            mAttach.setScaleType(scaleType);
        } else {
            mPendingScaleType = scaleType;
        }
    }

    @Override
    public void setZoomable(boolean zoomable) {
        mAttach.setZoomable(zoomable);
    }

    @Override
    public Bitmap getVisibleRectangleBitmap() {
        return mAttach.getVisibleRectangleBitmap();
    }

    @Override
    public void setZoomTransitionDuration(int milliseconds) {
        mAttach.setZoomTransitionDuration(milliseconds);
    }

    @Override
    public IPicture getIPictureImplementation() {
        return mAttach;
    }

    @Override
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener newOnDoubleTapListener) {
        mAttach.setOnDoubleTapListener(newOnDoubleTapListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        mAttach.cleanup();
        super.onDetachedFromWindow();
    }

}