package com.itheima.life;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.itheima.adapter.NewsCenterTabPageAdapter;
import com.viewpager.indicator.TabPageIndicator;

/**
 * 新闻中心模块(使用ViewPagerIndicator+Fragment的UI框架实现)
 * @author zhangming
 * @date 2016/04/15
 */
public class NewsCenterActivity extends FragmentActivity{
	private TabPageIndicator mPageIndicator;
	private ViewPager mViewPager;
	private NewsCenterTabPageAdapter mAdapter;
	public static final String[] TITLES = new String[] { "业界", "移动", "研发", "程序员", "云计算" };
	public List<String> tabList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_center_activity);
		
		tabList.addAll(Arrays.asList(TITLES));
		mPageIndicator = (TabPageIndicator) findViewById(R.id.id_indicator);
		mViewPager = (ViewPager) findViewById(R.id.id_pager);
		mAdapter = new NewsCenterTabPageAdapter(getSupportFragmentManager(),tabList);
		mViewPager.setAdapter(mAdapter);
		mPageIndicator.setViewPager(mViewPager,0);
	}
}
