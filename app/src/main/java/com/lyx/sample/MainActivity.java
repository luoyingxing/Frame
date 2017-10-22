package com.lyx.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.lyx.frame.slide.SlideView;
import com.lyx.sample.annotation.Id;
import com.lyx.sample.annotation.IdParser;
import com.lyx.sample.entity.SlideInfo;

public class MainActivity extends AppCompatActivity {
    @Id(id = R.id.sv_home)
    private SlideView<SlideInfo> mSlideView;

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
    }
}