package com.itheima.life;

import com.itheima.net.Http;
import com.itheima.net.NetworkURL;
import com.itheima.net.OnResult;
import com.itheima.response.MusicEntity;
import com.itheima.util.T;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * 音乐模块界面
 * @author zhangming
 * @date 2016/04/09
 */
public class MusicActivity extends BaseActivity{
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_activity);
		context = this;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){
			Http.getInstance().getTask(this,NetworkURL.MUSIC_URL,"",MusicEntity.class,new OnResult() {
				@Override
				public void onResult(Object entity) {
					if(entity == null){
						Log.i("test","failed...");
						return;
					}
					MusicEntity musicEntity = (MusicEntity) entity;
					if("success".equals(musicEntity.getStatus())){
						Log.i("test",musicEntity.getData().get(0).getMusicTitle());
						try {
							MediaPlayer mediaPlayer =  new MediaPlayer();
							mediaPlayer.reset();//把各项参数恢复到初始状态  
				            mediaPlayer.setDataSource(context,Uri.parse(musicEntity.getData().get(0).getMusicUrl())); 
				            mediaPlayer.prepare();  //进行缓冲  
				            mediaPlayer.start(); 
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						Log.i("test","failed...");
					}
				}
			});
		}
		super.onWindowFocusChanged(hasFocus);
	}
}
