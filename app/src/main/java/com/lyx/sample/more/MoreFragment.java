package com.lyx.sample.more;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lyx.frame.adapter.abs.MultiAdapter;
import com.lyx.frame.adapter.abs.Proxy;
import com.lyx.frame.adapter.abs.ViewHolder;
import com.lyx.frame.annotation.Id;
import com.lyx.frame.refresh.FooterLayout;
import com.lyx.frame.refresh.FooterPresenter;
import com.lyx.frame.refresh.RefreshLayout;
import com.lyx.frame.slide.SlideView;
import com.lyx.frame.widget.scroll.ScrollListView;
import com.lyx.sample.R;
import com.lyx.sample.entity.Image;
import com.lyx.sample.frame.BaseFragment;

import java.util.ArrayList;

public class MoreFragment extends BaseFragment implements View.OnTouchListener, FooterPresenter {
    @Id(R.id.sv_more)
    private SlideView<Image> mSlideView;
    @Id(R.id.lv_more_list_list)
    private ScrollListView mListView;
    @Id(R.id.scroll_view_more)
    private ScrollView mScrollView;
    @Id(R.id.refresh_more)
    private RefreshLayout mRefreshLayout;
    @Id(R.id.refresh_footer_layout)
    private FooterLayout mFooterLayout;
    @Id(R.id.refresh_footer_arrows)
    private ImageView mLoadMoreIV;
    @Id(R.id.refresh_footer_progress_bar)
    private ProgressBar mLoadMoreProgressBar;
    @Id(R.id.refresh_footer_text)
    private TextView mLoadMoreTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadList();
    }

    private void init() {
        mSlideView.init(Image.getImageList());
        mSlideView.setOnItemClickListener(new SlideView.OnItemClickListener<Image>() {
            @Override
            public void onItemClick(Image info, int position) {
                Toast.makeText(getContext(), info.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        mFooterLayout.setPresenter(this);
        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                loadList();
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "第" + position + "张", Toast.LENGTH_SHORT).show();
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

        mListView.setAdapter(mAdapter);
    }

    private void loadList() {
        mAdapter.addAll(Image.getImageList());
        mRefreshLayout.onLoadMoreComplete();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.94f, 1.0f);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.94f, 1.0f);
                ObjectAnimator.ofPropertyValuesHolder(v, pvhX, pvhY).setDuration(100).start();
                break;
        }
        return false;
    }

    @Override
    public ImageView getFooterArrows() {
        return mLoadMoreIV;
    }

    @Override
    public ProgressBar getFooterProgressBar() {
        return mLoadMoreProgressBar;
    }

    @Override
    public TextView getFooterTipTextView() {
        return mLoadMoreTV;
    }

}