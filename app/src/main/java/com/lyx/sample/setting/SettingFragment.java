package com.lyx.sample.setting;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lyx.frame.adapter.recycler.MultiAdapter;
import com.lyx.frame.adapter.recycler.Proxy;
import com.lyx.frame.adapter.recycler.ViewHolder;
import com.lyx.frame.slide.SlideView;
import com.lyx.sample.R;
import com.lyx.sample.entity.Image;
import com.lyx.sample.entity.SlideInfo;
import com.lyx.sample.frame.BaseFragment;

import java.util.ArrayList;

public class SettingFragment extends BaseFragment {
    //    @Id(R.id.sv_home)
    private SlideView<SlideInfo> mSlideView;
    //    @Id(R.id.rv_home)
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void init() {
        mSlideView.init(SlideInfo.getDefaultList());
        mSlideView.setOnItemClickListener(new SlideView.OnItemClickListener<SlideInfo>() {
            @Override
            public void onItemClick(SlideInfo info, int position) {
                Toast.makeText(getContext(), info.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        setAdapter();
    }

    private MultiAdapter<Image> mAdapter;

    private void setAdapter() {
        mAdapter = new MultiAdapter<>(getContext(), new ArrayList<Image>());
        mAdapter.addProxy(new Proxy<Image>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_image_list;
            }

            @Override
            public boolean isApplyFromViewType(Image item, int position) {
                return item.getType() == 1;
            }

            @Override
            public void convert(ViewHolder holder, Image item, int position) {
                holder.setText(R.id.tv_image_title, "相册 " + item.getTitle());
                SimpleDraweeView imageView = holder.getView(R.id.iv_image_image);
                imageView.setImageURI(Uri.parse(item.getUrl()));
            }
        });

        mAdapter.addProxy(new Proxy<Image>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_image_list_two;
            }

            @Override
            public boolean isApplyFromViewType(Image item, int position) {
                return item.getType() == 2;
            }

            @Override
            public void convert(ViewHolder holder, Image item, int position) {
                holder.setText(R.id.tv_image_title_two, "item_image_list_two " + item.getTitle());
                SimpleDraweeView imageView = holder.getView(R.id.iv_image_image_two);
                imageView.setImageURI(Uri.parse(item.getUrl()));
            }
        });

        mAdapter.addProxy(new Proxy<Image>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_image_list_three;
            }

            @Override
            public boolean isApplyFromViewType(Image item, int position) {
                return item.getType() == 3;
            }

            @Override
            public void convert(ViewHolder holder, Image item, int position) {
                holder.setText(R.id.tv_image_title_three, "哈哈3 " + item.getTitle());
                SimpleDraweeView imageView = holder.getView(R.id.iv_image_image_three);
                imageView.setImageURI(Uri.parse(item.getUrl()));
            }
        });


        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        mAdapter.setOnItemClickListener(new MultiAdapter.OnItemClickListeners<Image>() {
            @Override
            public void onItemClick(ViewHolder holder, Image item, int position) {
                Toast.makeText(getContext(), "第" + position + "张", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(ViewHolder holder, Image item, int position) {
            }
        });

        mAdapter.addAll(Image.getImageList());
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = mAdapter.getItemCount();
            int pos = parent.getChildAdapterPosition(view);

            outRect.left = mSpace;
            outRect.top = mSpace;
            outRect.bottom = mSpace;

            if (pos != (itemCount - 1)) {
                outRect.right = mSpace;
            } else {
                outRect.right = 0;
            }
        }
    }
}