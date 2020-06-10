package com.lyx.frame.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * MultiAdapter
 * <p>
 * author:  luoyingxing
 * date: 2017/11/7.
 *
 * @param <T>
 */
public class MultiAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private Context mContext;
    private List<T> mList;
    private int mPosition;
    private OnItemClickListener<T> mItemClickListener;
    private OnLongItemClickListener<T> mOnLongItemClickListener;
    private ProxyManager<T> mProxyManager;

    public void add(T item) {
        mList.add(getItemCount(), item);
        notifyItemInserted(getItemCount());
    }

    public void insert(int index, T item) {
        mList.add(index, item);
        notifyItemInserted(getItemCount());
    }

    public List<T> getDataSource() {
        return mList;
    }

    public void addAll(List<T> list) {
        int oldSize = mList.size();
        int newSize = list.size();
        mList.addAll(list);
        notifyItemRangeInserted(oldSize, newSize);
    }

    public void remove(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        int size = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void changed(T item, int position) {
        mList.set(position, item);
        notifyItemChanged(position);
    }

    public MultiAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list == null ? new ArrayList<T>() : list;
        mProxyManager = new ProxyManager<>();
    }

    public MultiAdapter addProxy(Proxy<T> delegate) {
        if (null != mProxyManager) {
            mProxyManager.addProxy(delegate);
        }
        return this;
    }

    private boolean useItemViewProxyManager() {
        return null != mProxyManager && mProxyManager.getProxyCount() > 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Proxy delegate = mProxyManager.getItemViewProxy(mList.get(mPosition), mPosition);
        int layoutId = delegate.getItemViewLayoutId();
        return ViewHolder.create(mContext, layoutId, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindItem(holder, position);
    }

    private void bindItem(final ViewHolder holder, final int position) {
        mProxyManager.convert(holder, mList.get(position), position);

        if (null != mItemClickListener) {
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(holder, mList.get(position), position);
                }
            });
        }

        if (null != mOnLongItemClickListener) {
            holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnLongItemClickListener.onLongItemClick(holder, mList.get(position), position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (null != mList) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        mPosition = position;
        if (useItemViewProxyManager()) {
            return mProxyManager.getItemViewType(mList.get(position), position);
        }
        return super.getItemViewType(position);
    }

    public T getItem(int position) {
        if (mList.isEmpty()) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFooterView(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    private boolean isFooterView(int position) {
        return position >= getItemCount() - 1;
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mItemClickListener = listener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener<T> listener) {
        this.mOnLongItemClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(ViewHolder holder, T item, int position);
    }

    public interface OnLongItemClickListener<T> {
        void onLongItemClick(ViewHolder holder, T item, int position);
    }
}