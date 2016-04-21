package com.itheima.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.itheima.adapter.MusicAdapter;
import com.itheima.constant.LifeAssistantConstant;
import com.itheima.life.R;
import com.itheima.model.MusicLoader;
import com.itheima.response.Music_Data;
import com.itheima.service.MusicService;

/**
 * 本地音乐Fragment
 * @author zhangming
 */
public class LocalMusicFragment extends BaseStandardFragment{
	private Activity mActivity;
	private View rootView;
	private ListView music_list;
	private MusicAdapter mAdapter;
	private List<Music_Data> musicList;
	
	@Override
	public View onFragmentCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		if(null == rootView) {
            rootView = inflater.inflate(R.layout.local_music_fragment, container, false);
        }
        if(null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
		this.initView();
		this.setListener();
		return rootView;
	}
	
	private void initView(){
		mActivity = getActivity();
		List<MusicLoader.MusicInfo> mList = MusicLoader.instance(mActivity.getContentResolver()).getMusicList();
		musicList = new ArrayList<Music_Data>();
		
		for(int i=0;i<mList.size();i++){
			Music_Data data = new Music_Data();
			data.setId(UUID.randomUUID().toString().substring(0,6));
			data.setAuthor(mList.get(i).getArtist());
			data.setMusicTitle(mList.get(i).getTitle());
			data.setMusicUrl(mList.get(i).getUrl());
			musicList.add(data);
		}
		
		music_list = (ListView) rootView.findViewById(R.id.music_list);
		mAdapter = new MusicAdapter(mActivity, musicList);
		music_list.setAdapter(mAdapter);
	}
	
	private void setListener(){
		music_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				playMusic(position);
			}
		});
	}
	
	/**
	 * 发起播放音乐命令
	 * @param position 播放音乐的List条目的位置
	 */
	private void playMusic(int position){
		Music_Data data = musicList.get(position);
		if(data!=null){
		     Intent intent = new Intent();  
		     intent.putExtra("MSG", LifeAssistantConstant.PlayerMsg.PLAY_MSG);  
	         intent.putExtra("MUSIC_URL", data.getMusicUrl());         
	         intent.setClass(mActivity,MusicService.class);  
	         mActivity.startService(intent);
		}
	}
}
