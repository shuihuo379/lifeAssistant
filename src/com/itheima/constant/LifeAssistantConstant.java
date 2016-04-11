package com.itheima.constant;


public class LifeAssistantConstant {
	public static class PlayerMsg{
		public static final int PLAY_MSG = 0;
		public static final int PAUSE_MSG = 1;
		public static final int STOP_MSG = 2;
		public static final int SEEK_MSG = 3;
		public static final int PROGRESS_MSG = 4;
	}
	public static class ProgressMsgReceiver_Text{
		public static final String ACTION_MSG = "com.itheima.progress.msg";
		public static final String PARAM_ONE_KEY_MSG = "progress";
		public static final String PARAM_TWO_KEY_MSG = "duration";
		public static boolean isChanging = false;  //互斥变量，防止定时器与SeekBar拖动时进度冲突   
	}
}
