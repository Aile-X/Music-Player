package com.aile.biz;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Audio.Media;

import com.aile.entity.Music;

import java.util.ArrayList;

//��ȡ�洢�����������ֵ�ҵ����.
public class MusicBiz {
	private ContentResolver resolver;

	public MusicBiz(Context context) {
		resolver = context.getContentResolver();// ��ʼ�����ݷ�����.
	}

	//��ȡ��洢����������.
	public ArrayList<Music> getLocalMusics() {
		ArrayList<Music> musics = null;
		String[] projection = { "_id", "title", "_data", "duration", "artist",
				"album", "album_key" };// ��Ƶ������.
		Cursor c = resolver.query(Media.EXTERNAL_CONTENT_URI, projection,
				"duration>20000", null, null);
		if (c != null && c.getCount() > 0) {
			musics = new ArrayList<Music>();
			while (c.moveToNext()) {
				Music music = new Music();
				music.setId(c.getInt(c.getColumnIndex("_id")));
				music.setName(c.getString(c.getColumnIndex("title")));
				music.setAlbum(c.getString(c.getColumnIndex("album")));
				music.setSinger(c.getString(c.getColumnIndex("artist")));
				music.setMusicPath(c.getString(c.getColumnIndex("_data")));
				music.setDuration(c.getInt(c.getColumnIndex("duration")));

				// ��ȡalbum_key
				String album_key = c.getString(c.getColumnIndex("album_key"));
				Cursor albumCursor = resolver.query(
						Albums.EXTERNAL_CONTENT_URI,
						new String[] { "album_art" }, "album_key = ?",
						new String[] { album_key }, null);
				// ����album_key����ר��ͼƬ·��
				if (albumCursor != null && albumCursor.moveToFirst()) {
					music.setAlbumPath(albumCursor.getString(0));
					albumCursor.close();
				}

				musics.add(music);
			}
			c.close();
		}
		return musics;
	}
}
