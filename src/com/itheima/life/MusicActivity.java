package com.itheima.life;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.itheima.adapter.MusicAdapter;
import com.itheima.constant.LifeAssistantConstant;
import com.itheima.net.Http;
import com.itheima.net.NetworkURL;
import com.itheima.net.OnResult;
import com.itheima.response.MusicEntity;
import com.itheima.response.Music_Data;
import com.itheima.service.MusicService;
import com.itheima.util.T;

/**
 * 音乐模块界面
 * @author zhangming
 * @date 2016/04/09
 */
public class MusicActivity extends BaseActivity{
	private Context context;
	private ListView music_list;
	private List<Music_Data> mList;
	private MusicAdapter mAdapter;
	private SeekBar music_seek;
	private TextView music_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_activity);
		initView();
		setListener();
	}
	
	private void initView() {
		context = this;
		music_list = (ListView)findViewById(R.id.music_list);
		music_seek = (SeekBar)findViewById(R.id.music_seek);
		music_title = (TextView)findViewById(R.id.music_title);
		
		mList = new ArrayList<Music_Data>();
		mAdapter = new MusicAdapter(this,mList);
		music_list.setAdapter(mAdapter);
	}
	
	private void setListener(){
		music_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Music_Data data = mList.get(position);
				if(data!=null){
				     Intent intent = new Intent();  
				     intent.putExtra("MSG", LifeAssistantConstant.PlayerMsg.PLAY_MSG);  
			         intent.putExtra("MUSIC_URL", data.getMusicUrl());         
			         intent.setClass(context,MusicService.class);  
			         startService(intent);
			         music_seek.setProgress(0); //重置seekbar进度条
			         music_title.setText(data.getMusicTitle());
				}
			}
		});
		
		music_seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			private int curProgress;
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Intent intent = new Intent();  
				intent.putExtra("MSG", LifeAssistantConstant.PlayerMsg.SEEK_MSG);  
				intent.putExtra("maxBarProgress",seekBar.getMax());  
				intent.putExtra("curProgress",this.curProgress);  
				intent.setClass(context,MusicService.class);  
				startService(intent);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				this.curProgress = progress;
			}
		});
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){
			Http.getInstance().getTask(this,NetworkURL.MUSIC_URL,"",MusicEntity.class,new OnResult() {
				@Override
				public void onResult(Object entity) {
					if(entity == null){
						T.show(context,context.getString(R.string.common_error));
						return;
					}
					MusicEntity musicEntity = (MusicEntity) entity;
					if("success".equals(musicEntity.getStatus())){
						updateData(musicEntity.getData());
						music_title.setText(mList.get(0).getMusicTitle());  //默认显示第一个音乐条目的信息
					}else{
						T.show(context,context.getString(R.string.common_error));
					}
				}
			});
		}
	}
	
	protected void updateData(List<Music_Data> mList) {
		this.mList.addAll(mList);
		mAdapter.notifyDataSetChanged();
	}
	
}
