package com.itheima.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itheima.life.R;

public class MsgNotificationAdapter extends BaseAdapter{
	private Context context;
	private List<String> msgList;
	
	public MsgNotificationAdapter(Context context,List<String> msgList){
		this.context = context;
		this.msgList = msgList;
	}
	
	@Override
	public int getCount() {
		return msgList.size();
	}

	@Override
	public Object getItem(int position) {
		return msgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.msg_notification_item,parent,false);
			holder.tv_data = (TextView)convertView.findViewById(R.id.tv_message);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.tv_data.setText(msgList.get(position));
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView tv_data;
	}
}
