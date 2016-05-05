package com.itheima.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ListView;

import com.itheima.adapter.SlideListViewAdapter;

public class SlideListView extends ListView {
	private SlideView mSlideView; // 线性布局容器实例
	private GestureDetector mGestureDetector;

	public SlideListView(Context context) {
		super(context);
		init();
		setFadingEdgeLength(0);
	}

	public SlideListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlideListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		mGestureDetector = new GestureDetector(new YScrollDetector());
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
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int x = (int) event.getX();
			int y = (int) event.getY();
			int position = pointToPosition(x, y); // 获取触摸的ListView的条目的位置

			if (position != INVALID_POSITION) {
				SlideListViewAdapter.MessageItem data = (SlideListViewAdapter.MessageItem) getItemAtPosition(position);
				mSlideView = data.slideView;
			}
			break;
		}
		if (mSlideView != null) {
			mSlideView.onRequireTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}
	
	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (distanceY != 0 && distanceX != 0) {

			}
			if (Math.abs(distanceY) >= Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}
}
