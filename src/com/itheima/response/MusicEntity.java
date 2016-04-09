package com.itheima.response;

import java.util.List;

public class MusicEntity extends BaseEntity{
	private List<Music_Data> data;

	public List<Music_Data> getData() {
		return data;
	}

	public void setData(List<Music_Data> data) {
		this.data = data;
	}
}
