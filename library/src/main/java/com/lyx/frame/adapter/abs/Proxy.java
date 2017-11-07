package com.lyx.frame.adapter.abs;

/**
 * Proxy, The agent interface which to deal with AbsListView's item type
 * <p>
 * author:  luoyingxing
 * date: 2017/11/7.
 */

public interface Proxy<T> {

    /**
     * return item layout id
     *
     * @return item layout id
     */
    int getItemViewLayoutId();

    /**
     * @param item     T
     * @param position position
     * @return return true program will apply the layout which is fill this type, otherwise return false.
     */
    boolean isApplyFromViewType(T item, int position);

    /**
     * you can convert data in this method
     *
     * @param holder   ViewHolder
     * @param item     T
     * @param position position
     */
    void convert(ViewHolder holder, T item, int position);

}