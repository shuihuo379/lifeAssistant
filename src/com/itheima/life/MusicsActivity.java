package com.itheima.life;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.itheima.fragment.BaseStandardFragment;
import com.itheima.fragment.LocalMusicFragment;
import com.itheima.fragment.NetMusicFragment;

public class MusicsActivity extends BaseActivity implements BaseStandardFragment.OnFragmentInteractionListener,OnClickListener{
	private TextView tv_local_music,tv_net_music;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musics_activity);
		initView();
		initNetManager();
	}
	
	private void initView(){
		tv_local_music = (TextView)findViewById(R.id.tv_local_music);
		tv_net_music = (TextView)findViewById(R.id.tv_net_music);
		
		tv_local_music.setOnClickListener(this);
		tv_net_music.setOnClickListener(this);
		
		replaceFragment(LocalMusicFragment.class);  //默认加载的是本地音乐的Fragment
	}
	
	private <T> void replaceFragment(Class<T> cls){
		try{
			T fragment = BaseStandardFragment.newInstance(cls);
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			if(fragment instanceof BaseStandardFragment){
				BaseStandardFragment bs_fragment = (BaseStandardFragment) fragment;
				transaction.replace(R.id.fragment_container,bs_fragment);
			}
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_local_music:
			replaceFragment(LocalMusicFragment.class);
			tv_local_music.setTextColor(getResources().getColor(R.color.main_green));
			tv_net_music.setTextColor(getResources().getColor(R.color.darkgray));
			break;
		case R.id.tv_net_music:
			replaceFragment(NetMusicFragment.class);
			tv_local_music.setTextColor(getResources().getColor(R.color.darkgray));
			tv_net_music.setTextColor(getResources().getColor(R.color.main_green));
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		destroyNetManager();
		super.onDestroy();
	}
}
