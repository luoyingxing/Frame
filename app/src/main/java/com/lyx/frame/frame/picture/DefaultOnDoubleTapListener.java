package com.lyx.frame.frame.picture;

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * DefaultOnDoubleTapListener
 * <p>
 * Created by luoyingxing on 2017/5/2.
 */
public class DefaultOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

    private PictureViewAttach pictureViewAttach;

    /**
     * Default constructor
     *
     * @param pictureViewAttach PictureViewAttach to bind to
     */
    public DefaultOnDoubleTapListener(PictureViewAttach pictureViewAttach) {
        setPhotoViewAttach(pictureViewAttach);
    }

    /**
     * Allows to change PictureViewAttach within range of single instance
     *
     * @param pictureViewAttach PictureViewAttach to bind to
     */
    public void setPhotoViewAttach(PictureViewAttach pictureViewAttach) {
        this.pictureViewAttach = pictureViewAttach;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (this.pictureViewAttach == null)
            return false;

        ImageView imageView = pictureViewAttach.getImageView();

        if (null != pictureViewAttach.getOnPhotoTapListener()) {
            final RectF displayRect = pictureViewAttach.getDisplayRect();

            if (null != displayRect) {
                final float x = e.getX(), y = e.getY();

                // Check to see if the user tapped on the photo
                if (displayRect.contains(x, y)) {

                    float xResult = (x - displayRect.left)
                            / displayRect.width();
                    float yResult = (y - displayRect.top)
                            / displayRect.height();

                    pictureViewAttach.getOnPhotoTapListener().onPhotoTap(imageView, xResult, yResult);
                    return true;
                }
            }
        }
        if (null != pictureViewAttach.getOnViewTapListener()) {
            pictureViewAttach.getOnViewTapListener().onViewTap(imageView, e.getX(), e.getY());
        }

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent ev) {
        if (pictureViewAttach == null)
            return false;

        try {
            float scale = pictureViewAttach.getScale();
            float x = ev.getX();
            float y = ev.getY();

            if (scale < pictureViewAttach.getMediumScale()) {
                pictureViewAttach.setScale(pictureViewAttach.getMediumScale(), x, y, true);
            } else if (scale >= pictureViewAttach.getMediumScale() && scale < pictureViewAttach.getMaximumScale()) {
                pictureViewAttach.setScale(pictureViewAttach.getMaximumScale(), x, y, true);
            } else {
                pictureViewAttach.setScale(pictureViewAttach.getMinimumScale(), x, y, true);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Can sometimes happen when getX() and getY() is called
        }

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        // Wait for the confirmed onDoubleTap() instead
        return false;
    }

}
