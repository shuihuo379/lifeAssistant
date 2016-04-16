package com.itheima.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.itheima.adapter.MusicAdapter;
import com.itheima.constant.LifeAssistantConstant;
import com.itheima.life.R;
import com.itheima.net.Http;
import com.itheima.net.NetworkURL;
import com.itheima.net.OnResult;
import com.itheima.response.MusicEntity;
import com.itheima.response.Music_Data;
import com.itheima.service.MusicService;
import com.itheima.util.DateCompute;
import com.itheima.util.T;

/**
 * 网络音乐Fragment
 * @author zhangming
 */
public class NetMusicFragment extends BaseStandardFragment{
	private Activity mActivity;
	private ListView music_list;
	private List<Music_Data> mList;
	private MusicAdapter mAdapter;
	private SeekBar music_seek;
	private TextView music_title,music_time;
	private ProgressMsgReceiver msgReceiver;
	private ImageView iv_play,iv_arrow_left,iv_arrow_right;;
	private View rootView;
	private static int status = LifeAssistantConstant.Status_Text.INIT_PERPARE_STATUS;
	private static int playCurPosition = 0;  //当前处在播放的条目位置,默认初始值是0
	private static int curProgress; //当前音乐播放的进度
	
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
        	String curTime = DateCompute.getInstance().getNewTime(progress,"mm:ss");
        	String allTime = DateCompute.getInstance().getNewTime(duration,"mm:ss");
        	long pos = music_seek.getMax() * progress / duration;
        	Log.i("test","pos===>"+pos);
        	music_seek.setProgress((int)pos);
        	music_time.setText(curTime+"/"+allTime);
        }  
    }  
	
	@Override
	public View onFragmentCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		if(null == rootView) {
            rootView = inflater.inflate(R.layout.net_music_fragment, container, false);
        }
        if(null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
		this.initView();
		this.initData();
		this.setListener();
		this.registerProgressMsgReceiver();
		
		return rootView;
	}
	
	private void initView(){
		mActivity = getActivity();
		music_list = (ListView)rootView.findViewById(R.id.music_list);
		music_seek = (SeekBar)rootView.findViewById(R.id.music_seek);
		music_title = (TextView)rootView.findViewById(R.id.music_title);
		music_time = (TextView)rootView.findViewById(R.id.music_time);
		iv_play = (ImageView)rootView.findViewById(R.id.iv_play);
		iv_arrow_left = (ImageView)rootView.findViewById(R.id.arrow_left);
		iv_arrow_right = (ImageView)rootView.findViewById(R.id.arrow_right);
		
		mList = new ArrayList<Music_Data>();
		mAdapter = new MusicAdapter(mActivity,mList);
		music_list.setAdapter(mAdapter);
		
		if(status == LifeAssistantConstant.Status_Text.PLAY_STATUS){
			iv_play.setImageResource(R.drawable.play);
		}else if(status == LifeAssistantConstant.Status_Text.PAUSE_STATUS){
			iv_play.setImageResource(R.drawable.pause);
		}
	}
	
	private void initData(){
		Http.getInstance().getTask(mActivity,NetworkURL.MUSIC_URL,"",MusicEntity.class,new OnResult() {
			@Override
			public void onResult(Object entity) {
				if(entity == null){
					T.show(mActivity,mActivity.getString(R.string.common_error));
					return;
				}
				MusicEntity musicEntity = (MusicEntity) entity;
				if("success".equals(musicEntity.getStatus())){
					updateData(musicEntity.getData());
					music_title.setText(mList.get(playCurPosition).getMusicTitle());  //初次进来,默认显示第一个音乐条目的信息;之后进来,显示上一次播放的音乐条目
					music_seek.setProgress(curProgress);
				}else{
					T.show(mActivity,mActivity.getString(R.string.common_error));
				}
			}
		});
	}
	
	protected void updateData(List<Music_Data> mList) {
		this.mList.addAll(mList);
		mAdapter.notifyDataSetChanged();
	}
	
	private void setListener(){
		music_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				playMusic(position);
			}
		});
		
		music_seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				LifeAssistantConstant.ProgressMsgReceiver_Text.isChanging = false;
				
				Intent intent = new Intent();  
				intent.putExtra("MSG", LifeAssistantConstant.PlayerMsg.SEEK_MSG);  
				intent.putExtra("maxBarProgress",seekBar.getMax());  
				intent.putExtra("curProgress",curProgress);  
				intent.setClass(mActivity,MusicService.class);  
				mActivity.startService(intent);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				LifeAssistantConstant.ProgressMsgReceiver_Text.isChanging = true;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				curProgress = progress;  //记录当前播放的进度
			}
		});
		
		iv_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity,MusicService.class);  
				if(status == LifeAssistantConstant.Status_Text.PLAY_STATUS){
					status = LifeAssistantConstant.Status_Text.PAUSE_STATUS;
					iv_play.setImageResource(R.drawable.pause);
					intent.putExtra("MSG", LifeAssistantConstant.PlayerMsg.PAUSE_MSG);  
					mActivity.startService(intent);
				}else if(status == LifeAssistantConstant.Status_Text.PAUSE_STATUS){
					status = LifeAssistantConstant.Status_Text.PLAY_STATUS;
					iv_play.setImageResource(R.drawable.play);
					intent.putExtra("MSG", LifeAssistantConstant.PlayerMsg.RESTART_MSG);  
					mActivity.startService(intent);
				}else if(status == LifeAssistantConstant.Status_Text.INIT_PERPARE_STATUS){
					intent.putExtra("MSG", LifeAssistantConstant.PlayerMsg.PLAY_MSG);  //发送开始播放的指示消息
					intent.putExtra("MUSIC_URL",mList.get(0).getMusicUrl()); //初始状态下默认是第一首音乐       
					mActivity.startService(intent);
					status = LifeAssistantConstant.Status_Text.PLAY_STATUS;
					iv_play.setImageResource(R.drawable.play);
				}
			}
		});
		
		iv_arrow_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { //上一首
				if(playCurPosition == 0){ //循环制
					playMusic(mList.size()-1);
				}else{
					playMusic(playCurPosition-1);
				}
			}
		});
		
		iv_arrow_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { //下一首
				if(playCurPosition == mList.size()-1){
					playMusic(0);
				}else{
					playMusic(playCurPosition+1);
				}
			}
		});
	}
	
	/**
	 * 发起播放音乐命令
	 * @param position 播放音乐的List条目的位置
	 */
	private void playMusic(int position){
		Music_Data data = mList.get(position);
		if(data!=null){
		     Intent intent = new Intent();  
		     intent.putExtra("MSG", LifeAssistantConstant.PlayerMsg.PLAY_MSG);  
	         intent.putExtra("MUSIC_URL", data.getMusicUrl());         
	         intent.setClass(mActivity,MusicService.class);  
	         mActivity.startService(intent);
	         music_seek.setProgress(0); //重置seekbar进度条
	         music_title.setText(data.getMusicTitle());
	         status = LifeAssistantConstant.Status_Text.PLAY_STATUS; //设置状态为播放状态
	         playCurPosition = position;  //记录播放的音乐条目索引位置
	         iv_play.setImageResource(R.drawable.play);
		}
	}
	
	private void registerProgressMsgReceiver(){
		//动态注册广播接收器  
        msgReceiver = new ProgressMsgReceiver();  
        IntentFilter intentFilter = new IntentFilter();  
        intentFilter.addAction(LifeAssistantConstant.ProgressMsgReceiver_Text.ACTION_MSG);  
        mActivity.registerReceiver(msgReceiver, intentFilter);  
	}
}
