package com.itheima.life;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * 项目主界面
 * @author zhangming
 * @date 2016/04/09
 */
public class MainActivity extends BaseActivity implements OnClickListener{
	private LinearLayout ll_news,ll_music,ll_map,ll_weather,ll_notepad,ll_messages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}
	
	private void initView(){
		ll_news = (LinearLayout)findViewById(R.id.ll_news);
		ll_music = (LinearLayout)findViewById(R.id.ll_music);
		ll_map = (LinearLayout)findViewById(R.id.ll_map);
		ll_weather = (LinearLayout)findViewById(R.id.ll_weather);
		ll_notepad = (LinearLayout)findViewById(R.id.ll_notepad);
		ll_messages = (LinearLayout)findViewById(R.id.ll_messages);
		
		ll_news.setOnClickListener(this);
		ll_music.setOnClickListener(this);
		ll_map.setOnClickListener(this);
		ll_weather.setOnClickListener(this);
		ll_notepad.setOnClickListener(this);
		ll_messages.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_news:
			break;
		case R.id.ll_music:
			startActivity(MusicActivity.class);
			break;
		case R.id.ll_map:
			startActivity(BaiduMapActivity.class);
			break;
		case R.id.ll_weather:
			startActivity(WeatherActivity.class);
			break;
		case R.id.ll_notepad:
			break;
		case R.id.ll_messages:
			startActivity(MsgNotificationActivity.class);
			break;
		}
	}
}
