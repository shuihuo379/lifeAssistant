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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.igexin.sdk.PushManager;
import com.itheima.adapter.MsgNotificationAdapter;
import com.itheima.constant.LifeAssistantConstant;
import com.itheima.response.MsgNotificationEntity;

/**
 * 消息通知模块,使用到消息推送机制
 * @author zhangming
 */
public class MsgNotificationActivity extends BaseActivity{
	private MessageReceiver msgReceiver;
	private ListView mListView;
	private TextView tv_back;
	private MsgNotificationAdapter mAdapter;
	private static List<String> dataList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initManager();
		setContentView(R.layout.msg_notification_activity);
		registerMsgReceiver();
		initNetManager();
		initView();
	}
	
	@Override
	protected void onDestroy() {
		unRegisterMsgReceiver();
		destroyNetManager();
		super.onDestroy();
	}
	
	private void initManager(){
        // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
        Log.i("test", "initializing sdk...");
        PushManager.getInstance().initialize(this.getApplicationContext());
	}
	
	private void registerMsgReceiver(){
		msgReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter(LifeAssistantConstant.MsgNotification_Text.ACTION_INTENT_RECEIVER);
		registerReceiver(msgReceiver, filter);
	}
	
	private void unRegisterMsgReceiver(){
		if(msgReceiver!=null){
			unregisterReceiver(msgReceiver);
		}
	}
	
	private void initView(){
		tv_back = (TextView)findViewById(R.id.tv_back);
		dataList = new ArrayList<String>();
		mListView = (ListView)findViewById(R.id.msg_list);
		mAdapter = new MsgNotificationAdapter(this,dataList);
		mListView.setAdapter(mAdapter);
		
		tv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	public class MessageReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(LifeAssistantConstant.MsgNotification_Text.ACTION_INTENT_RECEIVER)){
				List<String> msgList = intent.getStringArrayListExtra("msgList");
				dataList.clear();
				for(int i=0;i<msgList.size();i++){
					MsgNotificationEntity msgEntity = JSONObject.parseObject(msgList.get(i),MsgNotificationEntity.class);
					dataList.add("data "+msgEntity.getData().substring(0, 6));
				}
				mAdapter.notifyDataSetChanged(); //刷新适配器,更新数据列表
			}
		}
	}
}
