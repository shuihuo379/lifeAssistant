package com.itheima.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决ViewPager嵌套滑动冲突bug
 * 重写ViewPager中的dispatchTouchEvent方法
 * @author zhangming
 * @date 2016/04/16
 */
public class MyPictureScrollViewPager extends ViewPager{
	public MyPictureScrollViewPager(Context context) {
		super(context);
	}

	public MyPictureScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		if(action == MotionEvent.ACTION_DOWN){
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if(action == MotionEvent.ACTION_MOVE){
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
			getParent().requestDisallowInterceptTouchEvent(false);
		}
		return super.dispatchTouchEvent(ev);
	}
}
