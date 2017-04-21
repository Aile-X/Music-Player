package com.aile.musicplayer;

import android.app.Application;

import com.aile.biz.MusicBiz;
import com.aile.entity.Music;

import java.util.ArrayList;

//����Ŀȫ�ֹ������ֿ������ֵ����.��Ҫ���嵥�ļ���ע��,ֻҪһ��onCreate����дget��set����.
public class MusicApplication extends Application {
	private ArrayList<Music> musics;//����������Ϣ����.
	private int currentIndex;//��ǰ����ֵ.

	public void onCreate() {
		super.onCreate();
		MusicBiz biz = new MusicBiz(getApplicationContext());
		this.setMusics(biz.getLocalMusics());
		
		if(musics.size()==0)
			this.setCurrentIndex(-1);
		else
			this.setCurrentIndex(0);//��������ָ���б��е�һ����Ƶ�ļ�.
	}

	public ArrayList<Music> getMusics() {
		return musics;
	}

	public void setMusics(ArrayList<Music> musics) {
		if(musics!=null)
			this.musics = musics;
		else
			this.musics = new ArrayList<Music>();//��ֹ��ָ��.
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

}
