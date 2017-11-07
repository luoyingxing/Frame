package com.lyx.sample;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;

import com.lyx.frame.annotation.Id;
import com.lyx.frame.annotation.IdParser;
import com.lyx.sample.frame.BaseActivity;
import com.lyx.sample.home.HomeFragment;
import com.lyx.sample.more.MoreFragment;
import com.lyx.sample.personal.PersonalFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    @Id(R.id.vp_main)
    private ViewPager mViewPager;
    @Id(R.id.navigation_main)
    private BottomNavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IdParser.inject(this);

        mNavigationView.setOnNavigationItemSelectedListener(this);
        mViewPager.addOnPageChangeListener(this);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MoreFragment());
        fragments.add(new PersonalFragment());

        mViewPager.setAdapter(new PageViewAdapter(getSupportFragmentManager(), fragments));
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mNavigationView.setSelectedItemId(R.id.navigation_home);
                break;
            case 1:
                mNavigationView.setSelectedItemId(R.id.navigation_more);
                break;
            case 2:
                mNavigationView.setSelectedItemId(R.id.navigation_personal);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class PageViewAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public PageViewAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                mViewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_more:
                mViewPager.setCurrentItem(1);
                return true;
            case R.id.navigation_personal:
                mViewPager.setCurrentItem(2);
                return true;
        }
        return false;
    }

    private long mExitTime;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            showToast("再按一次退出");
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}