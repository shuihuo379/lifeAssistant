package com.itheima.life;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;

import com.itheima.adapter.MusicAdapter;
import com.itheima.net.Http;
import com.itheima.net.NetworkURL;
import com.itheima.net.OnResult;
import com.itheima.response.MusicEntity;
import com.itheima.response.Music_Data;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_activity);
		initView();
	}
	
	private void initView() {
		context = this;
		music_list = (ListView)findViewById(R.id.music_list);
		mList = new ArrayList<Music_Data>();
		mAdapter = new MusicAdapter(this,mList);
		music_list.setAdapter(mAdapter);
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

	private void play(String musicUrl){
		try {
			MediaPlayer mediaPlayer =  new MediaPlayer();
			mediaPlayer.reset();//把各项参数恢复到初始状态  
            mediaPlayer.setDataSource(context,Uri.parse(musicUrl)); 
            mediaPlayer.prepare();  //进行缓冲  
            mediaPlayer.start(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
