package com.itheima.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Application类,提供全局上下文对象
 * @author zhangming
 */
public class MyApplication extends Application{
	public static MyApplication myApplication;
	
	@Override
    public void onCreate() {
        super.onCreate();
        myApplication = this; //初始化上下文 
        
        SDKInitializer.initialize(getApplicationContext());
	}
	
	/**
	 * 用于产生上下文对象
	 * @return myApplication
	 */
	public static MyApplication newInstance(){
		return myApplication;  
	}
}
