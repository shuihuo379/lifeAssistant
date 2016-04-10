package com.itheima.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.life.R;
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false);
			holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
			holder.tv_author = (TextView)convertView.findViewById(R.id.tv_author);
			holder.iv_pic = (ImageView)convertView.findViewById(R.id.iv_pic);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		if(mList.get(position)!=null){
			holder.tv_title.setText(mList.get(position).getMusicTitle());
			holder.tv_author.setText(mList.get(position).getAuthor());
		}
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView iv_pic;
		TextView tv_title;
		TextView tv_author;
	}
}
