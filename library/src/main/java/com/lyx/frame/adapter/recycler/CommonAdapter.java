package com.lyx.frame.adapter.recycler;

import android.content.Context;

import java.util.List;

/**
 * CommonAdapter
 * <p>
 * author:  luoyingxing
 * date: 2017/11/7.
 *
 * @param <T>
 */
public abstract class CommonAdapter<T> extends MultiAdapter<T> {

    public CommonAdapter(Context context, List<T> list, final int layoutId) {
        super(context, list);

        addProxy(new Proxy<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isApplyFromViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T item, int position) {
                CommonAdapter.this.convert(holder, item, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T item, int position);
}