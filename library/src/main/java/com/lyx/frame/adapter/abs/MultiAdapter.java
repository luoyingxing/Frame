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
 * author:  luoyingxing
 * date: 2017/11/7.
 *
 * @param <T>
 */
public class MultiAdapter<T> extends ArrayAdapter<T> {
    protected Context mContext;
    private List<T> mDataList;
    private ProxyManager mProxyManager;

    public MultiAdapter(Context context, List<T> list) {
        super(context, 0, list);
        this.mContext = context;
        this.mDataList = list;
        mProxyManager = new ProxyManager();
    }

    public MultiAdapter addProxy(Proxy<T> proxy) {
        if (null != mProxyManager) {
            mProxyManager.addProxy(proxy);
        }
        return this;
    }

    private boolean useItemViewProxyManager() {
        return null != mProxyManager && mProxyManager.getProxyCount() > 0;
    }

    @Override
    public int getViewTypeCount() {
        if (useItemViewProxyManager()) {
            return mProxyManager.getProxyCount();
        }
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (useItemViewProxyManager()) {
            return mProxyManager.getItemViewType(mDataList.get(position), position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Proxy proxy = mProxyManager.getItemViewProxy(mDataList.get(position), position);
        int layoutId = proxy.getItemViewLayoutId();
        ViewHolder viewHolder;
        if (null == convertView) {
            View itemView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            viewHolder = new ViewHolder(mContext, itemView, parent, position);
            viewHolder.mLayoutId = layoutId;
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
        }
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    protected void convert(ViewHolder viewHolder, T item, int position) {
        if (null != mProxyManager) {
            mProxyManager.convert(viewHolder, item, position);
        }
    }

    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        if (mDataList != null) {
            return mDataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}