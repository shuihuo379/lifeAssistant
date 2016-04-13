package com.itheima.life;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
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
	private ProgressMsgReceiver msgReceiver;
	private ImageView iv_play;
	private int status;
	
	/**
	 * 音乐进度广播接收器,接收传过来的mediaPlayer.getCurrentPosition()的值
	 * SeekBar拖动的事件：SeekBar的Progress是0~SeekBar.getMax()之内的数，
	 * 而MediaPlayer.seekTo()的参数是0~MediaPlayer.getDuration()之内数，
	 * 所以MediaPlayer.seekTo()的参数是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
	 * @author zhangming
	 */
    public class ProgressMsgReceiver extends BroadcastReceiver{  
        @Override  
        public void onReceive(Context context, Intent intent) {  
        	int progress = intent.getIntExtra(LifeAssistantConstant.ProgressMsgReceiver_Text.PARAM_ONE_KEY_MSG, 0);  
        	int duration = intent.getIntExtra(LifeAssistantConstant.ProgressMsgReceiver_Text.PARAM_TWO_KEY_MSG, 0); 
        	long pos = music_seek.getMax() * progress / duration;
        	Log.i("test","pos===>"+pos);
        	music_seek.setProgress((int)pos);
        }  
    }  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_activity);
		initView();
		initNetManager();
		setListener();
		registerProgressMsgReceiver();
	}
	
	private void initView() {
		context = this;
		status = LifeAssistantConstant.Status_Text.INIT_PERPARE_STATUS;
		music_list = (ListView)findViewById(R.id.music_list);
		music_seek = (SeekBar)findViewById(R.id.music_seek);
		music_title = (TextView)findViewById(R.id.music_title);
		iv_play = (ImageView)findViewById(R.id.iv_play);
		
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
			         status = LifeAssistantConstant.Status_Text.PLAY_STATUS; //设置状态为播放状态
			         iv_play.setImageResource(R.drawable.play);
				}
			}
		});
		
		music_seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			private int curProgress;
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				LifeAssistantConstant.ProgressMsgReceiver_Text.isChanging = false;
				
				Intent intent = new Intent();  
				intent.putExtra("MSG", LifeAssistantConstant.PlayerMsg.SEEK_MSG);  
				intent.putExtra("maxBarProgress",seekBar.getMax());  
				intent.putExtra("curProgress",this.curProgress);  
				intent.setClass(context,MusicService.class);  
				startService(intent);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				LifeAssistantConstant.ProgressMsgReceiver_Text.isChanging = true;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				this.curProgress = progress;
			}
		});
		
		iv_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,MusicService.class);  
				if(status == LifeAssistantConstant.Status_Text.PLAY_STATUS){
					status = LifeAssistantConstant.Status_Text.PAUSE_STATUS;
					iv_play.setImageResource(R.drawable.pause);
					intent.putExtra("MSG", LifeAssistantConstant.PlayerMsg.PAUSE_MSG);  
					startService(intent);
				}else if(status == LifeAssistantConstant.Status_Text.PAUSE_STATUS){
					status = LifeAssistantConstant.Status_Text.PLAY_STATUS;
					iv_play.setImageResource(R.drawable.play);
					intent.putExtra("MSG", LifeAssistantConstant.PlayerMsg.RESTART_MSG);  
					startService(intent);
				}else if(status == LifeAssistantConstant.Status_Text.INIT_PERPARE_STATUS){
					intent.putExtra("MSG", LifeAssistantConstant.PlayerMsg.PLAY_MSG);  //发送开始播放的指示消息
					intent.putExtra("MUSIC_URL",mList.get(0).getMusicUrl()); //初始状态下默认是第一首音乐       
					startService(intent);
					status = LifeAssistantConstant.Status_Text.PLAY_STATUS;
					iv_play.setImageResource(R.drawable.play);
				}
			}
		});
	}
	
	private void registerProgressMsgReceiver(){
		//动态注册广播接收器  
        msgReceiver = new ProgressMsgReceiver();  
        IntentFilter intentFilter = new IntentFilter();  
        intentFilter.addAction(LifeAssistantConstant.ProgressMsgReceiver_Text.ACTION_MSG);  
        registerReceiver(msgReceiver, intentFilter);  
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
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(msgReceiver); //退出时注销广播
		destroyNetManager();
		super.onDestroy();
	}
}
