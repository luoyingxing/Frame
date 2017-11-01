package com.lyx.sample;

import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lyx.frame.annotation.Id;
import com.lyx.frame.annotation.IdParser;
import com.lyx.frame.slide.SlideView;
import com.lyx.sample.entity.Image;
import com.lyx.sample.entity.SlideInfo;
import com.lyx.sample.frame.recycler.ViewHolder;
import com.lyx.sample.frame.recycler.XAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Id(R.id.sv_home)
    private SlideView<SlideInfo> mSlideView;
    @Id(R.id.rv_home)
    private RecyclerView mRecyclerView;

    private XAdapter<Image> mXAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IdParser.inject(this);

        mSlideView.init(SlideInfo.getDefaultList());
        mSlideView.setOnItemClickListener(new SlideView.OnItemClickListener<SlideInfo>() {
            @Override
            public void onItemClick(SlideInfo info, int position) {
                Toast.makeText(MainActivity.this, info.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

//        setAdapter();
    }

    private void setAdapter() {
        mXAdapter = new XAdapter<Image>(MainActivity.this, new ArrayList<Image>(), R.layout.item_image_list) {
            @Override
            public void convert(ViewHolder holder, Image item) {
                holder.setText(R.id.tv_image_title, "相册 " + item.getTitle());

                SimpleDraweeView imageView = holder.getView(R.id.iv_image_image);
                imageView.setImageURI(Uri.parse(item.getUrl()));
            }
        };

        mRecyclerView.setAdapter(mXAdapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mXAdapter.setOnItemClickListener(new XAdapter.OnItemClickListeners<Image>() {
            @Override
            public void onItemClick(ViewHolder holder, Image item, int position) {
                Toast.makeText(MainActivity.this, "第" + position + "张", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(ViewHolder holder, Image item, int position) {
            }
        });
        mXAdapter.addAll(Image.getImageList());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(new RecyclerAdapter(Image.getImageList()));
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<Holder> {

        private List<Image> list;

        public RecyclerAdapter(List<Image> list) {
            this.list = list;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_image_list, parent, false);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.textView.setText(list.get(position).getTitle());
            holder.imageView.setImageURI(Uri.parse(list.get(position).getUrl()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class Holder extends RecyclerView.ViewHolder {
        TextView textView;
        SimpleDraweeView imageView;

        public Holder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_image_title);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.iv_image_image);
        }
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = mXAdapter.getItemCount();
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