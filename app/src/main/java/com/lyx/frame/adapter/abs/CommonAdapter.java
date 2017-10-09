package com.lyx.frame.adapter.abs;

import android.content.Context;

import java.util.List;


public abstract class CommonAdapter<T> extends MultiAdapter<T> {

    public CommonAdapter(Context context, List<T> list, final int layoutId) {
        super(context, list);

        addDelegate(new Delegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
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