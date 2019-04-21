package com.lyx.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lyx.frame.widget.TabIndicatorView;

import java.util.Arrays;
import java.util.List;

/**
 * TestActivity
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> list = Arrays.asList("可爱", "陶醉", "吟诵", "风铃", "悦耳", "清脆", "动听", "优美", "消瘦", "细挑", "富态", "富相", "臃肿", "干瘪", "丽质", "黑瘦", "彪壮",
                "强健", "刚健", "单薄", "憔悴", "肥大", "耳廓", "瘦削", "耳轮", "耳垂", "浓黑", "细长", "浓重", "墨黑", "粗长", "凤眼", "媚眼", "杏眼", "斜眼", "美目",
                "俊目", "秀目", "朗目", "星眸", "失望", "慈祥", "敏锐", "呆滞", "凝视", "眺望", "慧眼", "秋波", "明亮", "温柔", "赞许", "狡诈", "专注", "深邃", "浑浊",
                "关切", "坚定", "苗条", "丰满", "丰腴", "魁梧", "结实", "强壮", "匀称", "标致", "精悍", "短小", "粗实", "粗犷", "笨重", "消瘦", "细挑", "富态", "富相");
        tabIndicatorView.updateList(list);

        tabIndicatorView.setOnItemClickListener(new TabIndicatorView.OnItemClickListener<String>() {
            @Override
            public void onClick(int position, String obj) {
                Toast.makeText(getApplicationContext(), position + "  " + obj, Toast.LENGTH_SHORT).show();
            }

            @Override
            public String getTabText(String obj) {
                return obj;
            }
        });
    }
}