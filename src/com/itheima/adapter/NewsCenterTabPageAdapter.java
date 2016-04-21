package com.itheima.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.itheima.fragment.NewsCenterTabFragment;

public class NewsCenterTabPageAdapter extends FragmentPagerAdapter{
	public static List<String> tabList;

	public NewsCenterTabPageAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public NewsCenterTabPageAdapter(FragmentManager fm,List<String> tabList){
		this(fm);
		this.tabList = tabList;
	}

	@Override
	public Fragment getItem(int position) {
		NewsCenterTabFragment mFragment = new NewsCenterTabFragment(position);
		return mFragment;
	}

	@Override
	public int getCount() {
		return tabList.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return tabList.get(position % getCount());
	}
}
