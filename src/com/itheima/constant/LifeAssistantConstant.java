package com.itheima.constant;


public class LifeAssistantConstant {
	/**音乐模块常量字段**/
	public static class PlayerMsg{
		public static final int PLAY_MSG = 0;
		public static final int PAUSE_MSG = 1;
		public static final int RESTART_MSG = 2;
		public static final int STOP_MSG = 3;
		public static final int SEEK_MSG = 4;
		public static final int PROGRESS_MSG = 5;
	}
	public static class ProgressMsgReceiver_Text{
		public static final String ACTION_MSG = "com.itheima.progress.msg";
		public static final String PARAM_ONE_KEY_MSG = "progress";
		public static final String PARAM_TWO_KEY_MSG = "duration";
		public static boolean isChanging = false;  //互斥变量，防止定时器与SeekBar拖动时进度冲突   
		public static boolean isStarting = true;  //控制变量
	}
	public static class Status_Text{
		public static final int PLAY_STATUS = 0;
		public static final int PAUSE_STATUS = 1;
		public static final int INIT_PERPARE_STATUS = 2; //初始状态,准备播放
	}
	
	/**消息通知模块常量字段**/
	public static class MsgNotification_Text{
		public static final String ACTION_INTENT_RECEIVER = "com.itheima.msg.notification";
	}
}
