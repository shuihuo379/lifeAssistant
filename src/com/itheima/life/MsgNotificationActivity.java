package com.itheima.life;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.igexin.sdk.PushManager;

/**
 * 消息通知模块,使用到消息推送机制
 * @author zhangming
 */
public class MsgNotificationActivity extends BaseActivity{
	public static TextView tv_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initManager();
		setContentView(R.layout.msg_notification_activity);
		
		tv_content = (TextView)findViewById(R.id.tv_content);
	}
	
	private void initManager(){
        // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
        Log.i("test", "initializing sdk...");
        PushManager.getInstance().initialize(this.getApplicationContext());
	}
}
