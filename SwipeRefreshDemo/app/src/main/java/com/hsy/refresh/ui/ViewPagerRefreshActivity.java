package com.hsy.refresh.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.hsy.refresh.R;
import com.hsy.refresh.adapter.pager.PageOneView;
import com.hsy.refresh.adapter.pager.PageThreeView;
import com.hsy.refresh.adapter.pager.PageTwoView;
import com.hsy.refresh.adapter.pager.PageView;
import com.hsy.refresh.library.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 支持自定义Viewpage下拉刷新界面,和Google原生用法一致
 */
public class ViewPagerRefreshActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.swiperefresh)
    VpSwipeRefreshLayout swiperefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_view_refresh);
        ButterKnife.bind(this);

        /*初始化viewpager*/
        initData();

    }

    private List<PageView> pageList;//页面数组

    private List<String> titleList;  //标题列表数组

    private void initData() {
        titleList = new ArrayList<String>();// 每个页面的Title数据
        titleList.add("Page1");
        titleList.add("Page2");
        titleList.add("Page3");
        titleList.add("Page1");
        titleList.add("Page2");
        titleList.add("Page3");
        pageList = new ArrayList<>();
        pageList.add(new PageOneView(ViewPagerRefreshActivity.this));
        pageList.add(new PageTwoView(ViewPagerRefreshActivity.this));
        pageList.add(new PageThreeView(ViewPagerRefreshActivity.this));
        pageList.add(new PageOneView(ViewPagerRefreshActivity.this));
        pageList.add(new PageTwoView(ViewPagerRefreshActivity.this));
        pageList.add(new PageThreeView(ViewPagerRefreshActivity.this));
        viewpager.setAdapter(new SamplePagerAdapter());


    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }
       

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageList.get(position));
            return pageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        /**
         * 标题
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return titleList.get(position);
        }


    }
}
