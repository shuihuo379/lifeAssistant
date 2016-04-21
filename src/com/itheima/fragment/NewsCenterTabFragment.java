package com.itheima.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itheima.adapter.NewsCenterTabPageAdapter;
import com.itheima.life.R;

public class NewsCenterTabFragment extends Fragment{
	private int newsType;

	public NewsCenterTabFragment(int newsType){
		this.newsType = newsType;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.news_center_fragment_item, null);
		TextView tip = (TextView) view.findViewById(R.id.id_tip);
		tip.setText(NewsCenterTabPageAdapter.tabList.get(newsType));
		return view;
	}
}
