package com.lyx.sample.home;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.lyx.frame.annotation.Id;
import com.lyx.frame.slide.SlideView;
import com.lyx.frame.widget.picture.PictureDialog;
import com.lyx.sample.R;
import com.lyx.sample.TestActivity;
import com.lyx.sample.entity.Image;
import com.lyx.sample.entity.SlideInfo;
import com.lyx.sample.frame.BaseFragment;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {
    @Id(R.id.sv_home)
    private SlideView<SlideInfo> mSlideView;
    @Id(R.id.rv_home)
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mSlideView.init(SlideInfo.getDefaultList());
        mSlideView.setOnItemClickListener(new SlideView.OnItemClickListener<SlideInfo>() {
            @Override
            public void onItemClick(SlideInfo info, int position) {
                new PictureDialog<SlideInfo>(getContext(), new PictureDialog.OnSaveClickListener() {
                    @Override
                    public void save(int position) {
                        Toast.makeText(getContext(), "下载第" + position + "张", Toast.LENGTH_SHORT).show();
                    }
                }).setImageUrl(SlideInfo.getDefaultList(), PictureDialog.REMOTE)
                        .setPalaceHolderImage(R.mipmap.image_empty_fresco)
                        .show();
            }
        });

        mRecyclerView.setNestedScrollingEnabled(false);

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
                holder.setText(R.id.tv_image_title, item.getTitle());
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
                holder.setText(R.id.tv_image_title_two, item.getTitle());
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
                holder.setText(R.id.tv_image_title_three, item.getTitle());
                SimpleDraweeView imageView = holder.getView(R.id.iv_image_image_three);
                imageView.setImageURI(Uri.parse(item.getUrl()));
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(8));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        mAdapter.setOnItemClickListener(new MultiAdapter.OnItemClickListener<Image>() {
            @Override
            public void onItemClick(ViewHolder holder, Image item, int position) {
                Toast.makeText(getContext(), "第" + position + "张", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getBase(), TestActivity.class));
            }
        });

        mAdapter.setOnLongItemClickListener(new MultiAdapter.OnLongItemClickListener<Image>() {
            @Override
            public void onLongItemClick(ViewHolder holder, Image item, int position) {
                Snackbar.make(getView(), "第" + position + "张", Snackbar.LENGTH_SHORT).show();
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