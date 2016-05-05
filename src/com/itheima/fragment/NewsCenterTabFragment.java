package com.itheima.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.adapter.ImagePagerAdapter;
import com.itheima.adapter.NewsCenterTabPageAdapter;
import com.itheima.adapter.SlideListViewAdapter;
import com.itheima.adapter.SlideListViewAdapter.MessageItem;
import com.itheima.adapter.SlideListViewAdapter.onRemoveItemListener;
import com.itheima.life.R;
import com.itheima.model.Images;
import com.itheima.util.ImageCacheManager;
import com.itheima.view.MyPictureScrollViewPager;
import com.itheima.view.SlideListView;

/**
 * 新闻中心模块Fragment
 * @author zhangming
 * @date 2016/04/16
 */
public class NewsCenterTabFragment extends Fragment{
	private MyPictureScrollViewPager mviewPager;
	private LinearLayout dotLayout; //小圆点的LinearLayout
	private List<ImageView> dotViewList; //存放小圆点图片的集合
	private List<ImageView> imageList; //存放轮播效果图片的集合
	
	private Context context;
	private int newsType;
	private ScheduledExecutorService executorService;
	private boolean isPlay;
	private int currentItem;//当前页面
	private int ImgCount = 6;
	
	private SlideListView slideView;
	private SlideListViewAdapter mAdapter;
	private List<MessageItem> msgList = new ArrayList<MessageItem>();
	private int newsItemCount = 15;

	public NewsCenterTabFragment(int newsType){
		this.newsType = newsType;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		this.context = getActivity();
		this.isPlay = true;
		this.currentItem = 0; //初始化变量
		
		if(newsType == 0){ //第一个Fragment
			View listView = inflater.inflate(R.layout.news_center_one_fragment, null);
			View headView = inflater.inflate(R.layout.news_viewpager_header, null);
			mviewPager = (MyPictureScrollViewPager) headView.findViewById(R.id.myviewPager);
			dotLayout = (LinearLayout)headView.findViewById(R.id.dotLayout);
			dotLayout.removeAllViews();
			
			initView();
			initListView(listView,headView);
			startPlay();
			
			return listView;
		}
		
		View view = inflater.inflate(R.layout.news_center_fragment_item, null);
		TextView tip = (TextView) view.findViewById(R.id.id_tip);
		tip.setText(NewsCenterTabPageAdapter.tabList.get(newsType));
		return view;
	}
	
	@Override
	public void onDestroyView() {
		isPlay = false;
		if(executorService!=null){
			executorService.shutdown();
			executorService = null;
		}
		super.onDestroyView();
	}
	
	/**
	 * 初始化ListView条目
	 */
	public void initListView(View rootView,View headView){
		slideView = (SlideListView)rootView.findViewById(R.id.slideListView);
		
		if(msgList.size()>0){
			msgList.clear();
		}
		for(int i=0; i<newsItemCount; i++){
			SlideListViewAdapter.MessageItem item = new SlideListViewAdapter.MessageItem("新闻"+(i+1));
			msgList.add(item); 
		}
		mAdapter = new SlideListViewAdapter((Activity)context,msgList);
		slideView.setAdapter(mAdapter);
		
		mAdapter.setOnRemoveItemListener(new onRemoveItemListener() {
			@Override
			public void removeItem(int position) {
				Log.i("test","移除条目的所处List集合位置索引===>"+position);
				msgList.remove(position);
				mAdapter.notifyDataSetChanged();
			}
		});
		
		slideView.addHeaderView(headView); //为ListView添加头视图
	}
	
	public void initView(){
		dotViewList = new ArrayList<ImageView>();
		imageList = new ArrayList<ImageView>();
		
		for (int i = 0; i < ImgCount; i++) {
			ImageView dotView = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			
			params.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.dp5);//设置小圆点的外边距
			params.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dp5);
			params.height = context.getResources().getDimensionPixelSize(R.dimen.dp15); //设置小圆点的大小
			params.width = context.getResources().getDimensionPixelSize(R.dimen.dp15);
			
			if(i == 0){
				dotView.setBackgroundResource(R.drawable.point_pressed);
			}else{
				dotView.setBackgroundResource(R.drawable.point_unpressed);
			}
			dotLayout.addView(dotView, params);
			dotViewList.add(dotView);
		}
		
		LayoutInflater inflater = LayoutInflater.from(context);
		for(int i=0;i<ImgCount;i++){
			ImageView img = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
			ImageCacheManager.loadImage(Images.imageThumbUrls[i],img,
					BitmapFactory.decodeResource(getResources(),R.drawable.wall),
					BitmapFactory.decodeResource(getResources(),R.drawable.wall));
			imageList.add(img);
		}
		
		ImagePagerAdapter adapter = new ImagePagerAdapter(imageList);
		mviewPager.setAdapter(adapter);
		mviewPager.setCurrentItem(0);
		mviewPager.setOnPageChangeListener(new MyViewPagerChangeListener());
	}
	
	private void startPlay(){
		executorService = Executors.newSingleThreadScheduledExecutor();
		//command：执行线程,initialDelay：初始化延时,period：前一次执行结束到下一次执行开始的间隔时间（间隔执行延迟时间),unit：计时单位
		executorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if(isPlay){
					synchronized(context){
						currentItem = (currentItem+1)%imageList.size();
			            ((Activity)context).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mviewPager.setCurrentItem(currentItem,false);
							}
						});
					}
				}
			}
		},1,4,TimeUnit.SECONDS);
	}
	
	private class MyViewPagerChangeListener implements OnPageChangeListener{
		boolean isAutoPlay = false;
		
		/**
		 * onPageScrollStateChanged方法中status参数为1的时候表示开始滑动，为2的时候表示手指松开了页面自动滑动，为0的时候表示停止在某页
		 */
		@Override
		public void onPageScrollStateChanged(int status) {
			switch (status) {
			case 0: // 滑动结束,即切换完毕或者加载完毕
				if(mviewPager.getCurrentItem() == mviewPager.getAdapter().getCount()-1 && !isAutoPlay){
				   mviewPager.setCurrentItem(0,false); //解决界面切换时,图片滑动存在现象的bug,将平滑属性设置为false状态即可
				}else if(mviewPager.getCurrentItem() == 0 && !isAutoPlay){
				   mviewPager.setCurrentItem(mviewPager.getAdapter().getCount() - 1,false);
				}
				currentItem = mviewPager.getCurrentItem();
				isPlay = true;
				break;
			case 1: // 手势滑动,空闲中
				isAutoPlay = false;
				isPlay = false;
				break;
			case 2: // 界面切换中
				isAutoPlay = true;
				isPlay = false;
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@SuppressLint("NewApi") @Override
		public void onPageSelected(int pos) {
			for(int i=0;i<dotViewList.size();i++){
				if(i==pos){
					dotViewList.get(i).setBackground(context.getResources().getDrawable(R.drawable.point_pressed));
				}else{
					dotViewList.get(i).setBackground(context.getResources().getDrawable(R.drawable.point_unpressed));
				}
			}
		}
	}
}
