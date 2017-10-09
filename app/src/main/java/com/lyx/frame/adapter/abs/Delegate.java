package com.lyx.frame.adapter.abs;

/**
 * Delegate
 * <p>
 * Created by luoyingxing on 2017/5/23.
 */

public interface Delegate<T> {
    /**
     * return layout id
     *
     * @return layout id
     */
    int getItemViewLayoutId();

    /**
     * @param item     T
     * @param position position
     * @return if return true program will get the layout witch is fill this type,
     * otherwise return false.
     */
    boolean isForViewType(T item, int position);

    /**
     * you can fill data in this method
     *
     * @param holder   ViewHolder
     * @param item     T
     * @param position position
     */
    void convert(ViewHolder holder, T item, int position);

}