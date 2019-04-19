package com.lyx.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lyx.frame.widget.TabIndicatorView;

import java.util.Arrays;
import java.util.List;

/**
 * <p/>
 * Created by luoyingxing on 2019/4/18.
 */
public class TestActivity extends AppCompatActivity {
    private TabIndicatorView<String> tabIndicatorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tabIndicatorView = findViewById(R.id.tab_view);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i += 100;

                tabIndicatorView.scrollTo(i, 0);
            }
        });

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i -= 100;

                tabIndicatorView.scrollTo(i, 0);
            }
        });

    }

    int i = 0;
    private List<String> list;

    @Override
    protected void onResume() {
        super.onResume();
        list = Arrays.asList("人民1", "新华2", "央视3", "国际4", "在线5", "中国6", "日报7", "最后8");
        tabIndicatorView.updateList(list);

        tabIndicatorView.setOnItemClickListener(new TabIndicatorView.OnItemClickListener<String>() {
            @Override
            public void onClick(int position, String obj) {
                Toast.makeText(getApplicationContext(), position + "  " + obj, Toast.LENGTH_SHORT).show();
            }

            @Override
            public String getText(int position) {
                return list.get(position);
            }
        });
    }
}
