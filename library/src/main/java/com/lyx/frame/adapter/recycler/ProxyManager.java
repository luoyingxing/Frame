package com.lyx.frame.adapter.recycler;

import android.support.v4.util.SparseArrayCompat;

/**
 * ProxyManager
 * <p>
 * author:  luoyingxing
 * date: 2017/11/7.
 */
public class ProxyManager<T> {
    /**
     * this Cloneable cache the Proxy
     */
    private SparseArrayCompat<Proxy<T>> mProxyArrays = new SparseArrayCompat();

    public int getProxyCount() {
        return mProxyArrays.size();
    }

    public ProxyManager<T> addProxy(Proxy<T> proxy) {
        if (proxy != null) {
            int viewTypeCount = mProxyArrays.size();
            mProxyArrays.put(viewTypeCount, proxy);
        }
        return this;
    }

    public ProxyManager<T> addProxy(int viewType, Proxy<T> proxy) {
        if (mProxyArrays.get(viewType) != null) {
            throw new IllegalArgumentException(
                    "An Proxy is already registered for the viewType = " + viewType
                            + ". Already registered Proxy is " + mProxyArrays.get(viewType));
        }
        mProxyArrays.put(viewType, proxy);
        return this;
    }

    public ProxyManager<T> removeProxy(Proxy<T> proxy) {
        if (proxy == null) {
            throw new NullPointerException("Proxy is null");
        }
        int indexToRemove = mProxyArrays.indexOfValue(proxy);

        if (indexToRemove >= 0) {
            mProxyArrays.removeAt(indexToRemove);
        }
        return this;
    }

    public ProxyManager<T> removeProxy(int itemType) {
        int indexToRemove = mProxyArrays.indexOfKey(itemType);

        if (indexToRemove >= 0) {
            mProxyArrays.removeAt(indexToRemove);
        }
        return this;
    }

    public int getItemViewType(T item, int position) {
        int count = mProxyArrays.size();
        for (int i = count - 1; i >= 0; i--) {
            Proxy<T> proxy = mProxyArrays.valueAt(i);
            if (proxy.isApplyFromViewType(item, position)) {
                return mProxyArrays.keyAt(i);
            }
        }
        throw new IllegalArgumentException("No Proxy added that matches position=" + position + " in data source");
    }

    public void convert(ViewHolder holder, T item, int position) {
        int count = mProxyArrays.size();
        for (int i = 0; i < count; i++) {
            Proxy<T> proxy = mProxyArrays.valueAt(i);

            if (proxy.isApplyFromViewType(item, position)) {
                proxy.convert(holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException("No ProxyManager added that matches position=" + position + " in data source");
    }

    public int getItemViewLayoutId(int viewType) {
        return mProxyArrays.get(viewType).getItemViewLayoutId();
    }

    public int getItemViewType(Proxy proxy) {
        return mProxyArrays.indexOfValue(proxy);
    }

    public Proxy getItemViewProxy(T item, int position) {
        int count = mProxyArrays.size();
        for (int i = count - 1; i >= 0; i--) {
            Proxy<T> proxy = mProxyArrays.valueAt(i);
            if (proxy.isApplyFromViewType(item, position)) {
                return proxy;
            }
        }
        throw new IllegalArgumentException("No Proxy added that matches position=" + position + " in data source");
    }

    public int getItemViewLayoutId(T item, int position) {
        return getItemViewProxy(item, position).getItemViewLayoutId();
    }
}