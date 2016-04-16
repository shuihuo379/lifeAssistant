package com.itheima.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.life.R;

/**
 * 本地音乐Fragment
 * @author zhangming
 */
public class LocalMusicFragment extends BaseStandardFragment{
	private View rootView;
	
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
		return rootView;
	}
	
	private void initView(){
		
	}
}
