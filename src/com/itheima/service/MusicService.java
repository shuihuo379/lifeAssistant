package com.itheima.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.itheima.constant.LifeAssistantConstant;

/**
 * 音乐播放服务
 * @author zhangming
 * @date 2016/04/09
 */
public class MusicService extends Service{
	private MediaPlayer mediaPlayer =  new MediaPlayer();   //媒体播放器对象  
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if(!LifeAssistantConstant.ProgressMsgReceiver_Text.isChanging){
				Intent intent = new Intent(LifeAssistantConstant.ProgressMsgReceiver_Text.ACTION_MSG);
				intent.putExtra(LifeAssistantConstant.ProgressMsgReceiver_Text.PARAM_ONE_KEY_MSG,mediaPlayer.getCurrentPosition());  
				intent.putExtra(LifeAssistantConstant.ProgressMsgReceiver_Text.PARAM_TWO_KEY_MSG,mediaPlayer.getDuration());  
	            sendBroadcast(intent);  
	            handler.postDelayed(runnable,100);  
			}
		}
	};
	public static int curBufferingProgress;  //当前第二进度记录
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        if(mediaPlayer.isPlaying()) {  
           stop();  
        }  
        int msg = intent.getIntExtra("MSG", 0);  
        
        if(msg == LifeAssistantConstant.PlayerMsg.PLAY_MSG) {  
        	LifeAssistantConstant.ProgressMsgReceiver_Text.isStarting = true;
        	String path = intent.getStringExtra("MUSIC_URL");  //音乐文件路径 
            play(path,0); 
            handler.post(runnable); //启动
        } else if(msg == LifeAssistantConstant.PlayerMsg.PAUSE_MSG) { 
        	LifeAssistantConstant.ProgressMsgReceiver_Text.isStarting = false;
            pause();
        } else if(msg == LifeAssistantConstant.PlayerMsg.RESTART_MSG){
        	LifeAssistantConstant.ProgressMsgReceiver_Text.isStarting = true;
        	restart();
        } else if(msg == LifeAssistantConstant.PlayerMsg.STOP_MSG) {  
            stop();
        } else if(msg == LifeAssistantConstant.PlayerMsg.SEEK_MSG){
        	LifeAssistantConstant.ProgressMsgReceiver_Text.isStarting = true;
        	int maxBarProgress = intent.getIntExtra("maxBarProgress",0);
        	int curProgress = intent.getIntExtra("curProgress",0);
        	seek(maxBarProgress,curProgress);
        	handler.post(runnable); //启动
        }
        return super.onStartCommand(intent, flags, startId);  
    } 
	
	/** 
     * 播放音乐 
     * @param path 本地路径或网络路径
     */  
    private void play(String path,int position) {  
        try {  
	        if (mediaPlayer != null){
	        	mediaPlayer.reset();//把各项参数恢复到初始状态  
	            mediaPlayer.setDataSource(path);  
	            mediaPlayer.prepare();  //进行缓冲  
	            mediaPlayer.setOnPreparedListener(new PreparedListener(position));//注册一个监听器 
	            mediaPlayer.setOnBufferingUpdateListener(new MyBufferUpdateListener());
	        }
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    
    /** 
     * 暂停音乐 
     */  
    private void pause() {  
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {  
            mediaPlayer.pause();  
        }  
    }  
    
    /**
     * 暂停后重新播放音乐
     */
    private void restart(){
    	if (mediaPlayer != null && !mediaPlayer.isPlaying()){
    		Log.w("test","restart...");
    		mediaPlayer.start();
    	}
    }
      
    /** 
     * 停止音乐 
     */  
    private void stop(){  
        if(mediaPlayer != null) {  
            mediaPlayer.stop();  
            try {  
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
    /**
     * 定位音乐(设置音乐进度)
     * @param curProgress 当前播放进度
     * @param maxBarProgress 进度条最大进度
     */
    private void seek(int maxBarProgress,int curProgress){
    	if(mediaPlayer!=null){
    		int index = curProgress * mediaPlayer.getDuration()/maxBarProgress;  
			mediaPlayer.seekTo(index);
		}
    }
    
    @Override  
    public void onDestroy() {  
        if(mediaPlayer != null){  
            mediaPlayer.stop();  
            mediaPlayer.release();  
        }
        handler.removeCallbacks(runnable); //取消线程
        runnable = null;
    }  
    
    /** 
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放 
     */  
    private final class PreparedListener implements OnPreparedListener {  
        private int positon;  
          
        public PreparedListener(int positon) {  
            this.positon = positon;  
        }  
          
        @Override  
        public void onPrepared(MediaPlayer mp) {  
        	if(LifeAssistantConstant.ProgressMsgReceiver_Text.isStarting){
        		 mediaPlayer.start();    //开始播放  
        	}
            if(positon > 0) {  //如果音乐不是从头播放  
                mediaPlayer.seekTo(positon);  
            }  
        }  
    }
    
    private final class MyBufferUpdateListener implements OnBufferingUpdateListener{
		@Override
		public void onBufferingUpdate(MediaPlayer mp, int bufferingProgress) {
			Log.i("test","bufferingProgress===>" + bufferingProgress);  //可以用来设置第二进度 
			curBufferingProgress = bufferingProgress;
		}
    }
}
