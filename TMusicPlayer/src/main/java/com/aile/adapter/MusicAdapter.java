package com.aile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aile.entity.Music;
import com.aile.musicplayer.R;
import com.aile.utils.BitmapUtils;
import com.aile.utils.GlobalUtils;

import java.util.ArrayList;

//�����ֳ�������ͼ����.
public class MusicAdapter extends BaseAdapter {
	private ArrayList<Music> musics;
	private LayoutInflater inflater;
	Context mContext;
	
//	set����
	public void setMusics(ArrayList<Music> musics) {
		if (musics != null)
			this.musics = musics;
		else
			this.musics = new ArrayList<Music>();
	}

	//���ݸ���
	public void changeDataSet(ArrayList<Music> musics) {
		this.setMusics(musics);
		this.notifyDataSetChanged();
	}

	public MusicAdapter(Context context, ArrayList<Music> musics) {
		this.setMusics(musics);
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return musics.size();
	}

	@Override
	public Object getItem(int position) {
		return musics.get(position);
	}

	@Override
	public long getItemId(int position) {
		return musics.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_music, null);
			holder = new ViewHolder();
			holder.ivAlbum = (ImageView) convertView.findViewById(R.id.ivAlbum);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tvMusicName_Item);
			holder.tvAlbum = (TextView) convertView
					.findViewById(R.id.tvAlbum_Item);
			holder.tvSinger = (TextView) convertView
					.findViewById(R.id.tvSinger_Item);
			holder.tvDuration = (TextView) convertView
					.findViewById(R.id.tvDuration_Item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Music music = musics.get(position);

		holder.tvName.setText(music.getName());
		holder.tvAlbum.setText(music.getAlbum());
		holder.tvSinger.setText(music.getSinger());
		holder.tvDuration.setText(GlobalUtils.formatTime(music.getDuration()));

		Bitmap bm = BitmapUtils.getBitmap(music.getAlbumPath(), 64, 64);
		if (bm != null) {
			holder.ivAlbum.setImageBitmap(bm);
		} else {
			holder.ivAlbum.setImageResource(R.drawable.ic_launcher);
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView ivAlbum;
		TextView tvName;
		TextView tvAlbum;
		TextView tvSinger;
		TextView tvDuration;
	}
}
