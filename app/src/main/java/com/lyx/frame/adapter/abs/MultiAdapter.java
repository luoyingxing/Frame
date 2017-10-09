package com.lyx.frame.adapter.abs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * MultiAdapter
 * <p>
 * Created by luoyingxing on 2017/5/23.
 */

public class MultiAdapter<T> extends ArrayAdapter<T> {
    protected Context mContext;
    private List<T> mDataList;

    private DelegateManager mDelegateManager;

    public MultiAdapter(Context context, List<T> list) {
        super(context, 0, list);
        this.mContext = context;
        this.mDataList = list;
        mDelegateManager = new DelegateManager();
    }

    public MultiAdapter addDelegate(Delegate<T> delegate) {
        mDelegateManager.addDelegate(delegate);
        return this;
    }

    private boolean useItemViewDelegateManager() {
        return mDelegateManager.getDelegateCount() > 0;
    }

    @Override
    public int getViewTypeCount() {
        if (useItemViewDelegateManager()) {
            return mDelegateManager.getDelegateCount();
        }
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (useItemViewDelegateManager()) {
            int viewType = mDelegateManager.getItemViewType(mDataList.get(position), position);
            return viewType;
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Delegate delegate = mDelegateManager.getItemViewDelegate(mDataList.get(position), position);
        int layoutId = delegate.getItemViewLayoutId();
        ViewHolder viewHolder;
        if (convertView == null) {
            View itemView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            viewHolder = new ViewHolder(mContext, itemView, parent, position);
            viewHolder.mLayoutId = layoutId;
            onViewHolderCreated(viewHolder, viewHolder.getConvertView());
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
        }

        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    protected void convert(ViewHolder viewHolder, T item, int position) {
        mDelegateManager.convert(viewHolder, item, position);
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}