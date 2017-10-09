package com.lyx.frame.adapter.abs;

import android.support.v4.util.SparseArrayCompat;

/**
 * DelegateManager
 * <p>
 * Created by luoyingxing on 2017/5/23.
 */

public class DelegateManager<T> {
    /**
     * this Cloneable cache the Delegate
     */
    private SparseArrayCompat<Delegate<T>> mDelegates = new SparseArrayCompat();

    public int getDelegateCount() {
        return mDelegates.size();
    }

    public DelegateManager<T> addDelegate(Delegate<T> delegate) {
        int viewTypeCount = mDelegates.size();
        if (delegate != null) {
            mDelegates.put(viewTypeCount, delegate);
            viewTypeCount++;
        }
        return this;
    }

    public DelegateManager<T> addDelegate(int viewType, Delegate<T> delegate) {
        if (mDelegates.get(viewType) != null) {
            throw new IllegalArgumentException(
                    "An Delegate is already registered for the viewType = " + viewType
                            + ". Already registered Delegate is " + mDelegates.get(viewType));
        }
        mDelegates.put(viewType, delegate);
        return this;
    }

    public DelegateManager<T> removeDelegate(Delegate<T> delegate) {
        if (delegate == null) {
            throw new NullPointerException("Delegate is null");
        }
        int indexToRemove = mDelegates.indexOfValue(delegate);

        if (indexToRemove >= 0) {
            mDelegates.removeAt(indexToRemove);
        }
        return this;
    }

    public DelegateManager<T> removeDelegate(int itemType) {
        int indexToRemove = mDelegates.indexOfKey(itemType);

        if (indexToRemove >= 0) {
            mDelegates.removeAt(indexToRemove);
        }
        return this;
    }

    public int getItemViewType(T item, int position) {
        int count = mDelegates.size();
        for (int i = count - 1; i >= 0; i--) {
            Delegate<T> delegate = mDelegates.valueAt(i);
            if (delegate.isForViewType(item, position)) {
                return mDelegates.keyAt(i);
            }
        }
        throw new IllegalArgumentException("No Delegate added that matches position=" + position + " in data source");
    }

    public void convert(ViewHolder holder, T item, int position) {
        int count = mDelegates.size();
        for (int i = 0; i < count; i++) {
            Delegate<T> delegate = mDelegates.valueAt(i);

            if (delegate.isForViewType(item, position)) {
                delegate.convert(holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException("No DelegateManager added that matches position=" + position + " in data source");
    }


    public int getItemViewLayoutId(int viewType) {
        return mDelegates.get(viewType).getItemViewLayoutId();
    }

    public int getItemViewType(Delegate delegate) {
        return mDelegates.indexOfValue(delegate);
    }

    public Delegate getItemViewDelegate(T item, int position) {
        int count = mDelegates.size();
        for (int i = count - 1; i >= 0; i--) {
            Delegate<T> delegate = mDelegates.valueAt(i);
            if (delegate.isForViewType(item, position)) {
                return delegate;
            }
        }
        throw new IllegalArgumentException("No Delegate added that matches position=" + position + " in data source");
    }

    public int getItemViewLayoutId(T item, int position) {
        return getItemViewDelegate(item, position).getItemViewLayoutId();
    }
}