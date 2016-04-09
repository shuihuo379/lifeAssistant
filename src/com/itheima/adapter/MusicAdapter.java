package com.itheima.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.itheima.response.Music_Data;

public class MusicAdapter extends BaseAdapter{
	private Context context;
	private List<Music_Data> mList;
	
	public MusicAdapter(Context context,List<Music_Data> mList){
		this.context = context;
		this.mList = mList;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
}
