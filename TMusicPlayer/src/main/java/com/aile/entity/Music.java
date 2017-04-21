package com.aile.entity;

import java.io.Serializable;

//����ʵ��
public class Music implements Serializable {
	private int id;
	private String name;
	private String album;
	private String singer;
	private String musicPath;
	private String albumPath;
	private int duration;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getMusicPath() {
		return musicPath;
	}

	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}

	public String getAlbumPath() {
		return albumPath;
	}

	public void setAlbumPath(String albumPath) {
		this.albumPath = albumPath;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
