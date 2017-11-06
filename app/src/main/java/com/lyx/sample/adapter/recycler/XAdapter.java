package com.lyx.sample.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * XAdapter
 * <p>
 * Created by luoyingxing on 2017/8/14.
 */

public  class XAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private Context mContext;
    private List<T> mList;
    private int mPosition;
    private OnItemClickListeners<T> mItemClickListener;
    private DelegateManager mDelegateManager;

    public void add(T item) {
        mList.add(getItemCount(), item);
        notifyItemInserted(getItemCount());
    }

    public void addAll(List<T> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public XAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list == null ? new ArrayList<T>() : list;
        mDelegateManager = new DelegateManager();
    }

    public XAdapter addDelegate(Delegate<T> delegate) {
        mDelegateManager.addDelegate(delegate);
        return this;
    }

    private boolean useItemViewDelegateManager() {
        return mDelegateManager.getDelegateCount() > 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Delegate delegate = mDelegateManager.getItemViewDelegate(mList.get(mPosition), mPosition);
        int layoutId = delegate.getItemViewLayoutId();

        return ViewHolder.create(mContext, layoutId, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindItem(holder, position);
    }

    private void bindItem(final ViewHolder holder, final int position) {
        mDelegateManager.convert(holder, mList.get(position), position);

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(holder, mList.get(position), position);
            }
        });
        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mItemClickListener.onItemLongClick(holder, mList.get(position), position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        mPosition = position;
        if (useItemViewDelegateManager()) {
            int viewType = mDelegateManager.getItemViewType(mList.get(position), position);
            return viewType;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFooterView(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
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

    public T getItem(int position) {
        if (mList.isEmpty()) {
            return null;
        }
        return mList.get(position);
    }

    private boolean isFooterView(int position) {
        return position >= getItemCount() - 1;
    }

    public void setOnItemClickListener(OnItemClickListeners<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListeners<T> {
        void onItemClick(ViewHolder holder, T item, int position);

        void onItemLongClick(ViewHolder holder, T item, int position);
    }


}