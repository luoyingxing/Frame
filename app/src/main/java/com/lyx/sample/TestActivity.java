package com.lyx.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lyx.frame.widget.TabIndicatorView;

/**
 * <p/>
 * Created by luoyingxing on 2019/4/18.
 */
public class TestActivity extends AppCompatActivity {
    private TabIndicatorView tabIndicatorView;

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

    @Override
    protected void onResume() {
        super.onResume();
        tabIndicatorView.updateList();
    }
}
