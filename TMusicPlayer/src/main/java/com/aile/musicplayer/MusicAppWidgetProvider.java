package com.aile.musicplayer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.aile.entity.Music;
import com.aile.utils.GlobalUtils;

//��������СӦ�ó��򴴽���,���Ǹ��㲥,��Ҫ���嵥��ע��,��������xml�ļ�(musicprovider.xml��provider_music.xml)
public class MusicAppWidgetProvider extends AppWidgetProvider {
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		String action = intent.getAction();
		RemoteViews views = new RemoteViews(context.getPackageName(),// ��ͼ
				R.layout.provider_music);
		ComponentName provider = new ComponentName(context,// ���
				MusicAppWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		if (GlobalUtils.ACTION_CURRENT_MUSIC_CHANGED.equals(action)) {
			// ��ǰ����.
			Music music = (Music) intent
					.getSerializableExtra(GlobalUtils.EXTRA_CURRENT_MUSIC);
			views.setTextViewText(R.id.tvMusicName_Provider, music.getName());
			views.setTextViewText(R.id.tvSinger_Provider, music.getSinger());
			manager.updateAppWidget(provider, views);
		} else if (GlobalUtils.ACTION_STOP.equals(action)) {
			views.setTextViewText(R.id.tvMusicName_Provider, "������");
			views.setTextViewText(R.id.tvSinger_Provider, "����");
			manager.updateAppWidget(provider, views);
		}

	}

	// ��ʼ������.
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.provider_music);
		views.setTextViewText(R.id.tvMusicName_Provider, "������");
		views.setTextViewText(R.id.tvSinger_Provider, "����");
		views.setOnClickPendingIntent(R.id.btnPrevious_Provider, PendingIntent
				.getBroadcast(context, 0, new Intent(
						GlobalUtils.ACTION_PREVIOUS),
						PendingIntent.FLAG_UPDATE_CURRENT));
		views.setOnClickPendingIntent(R.id.btnPlay_Provider, PendingIntent
				.getBroadcast(context, 1, new Intent(GlobalUtils.ACTION_PLAY),
						PendingIntent.FLAG_UPDATE_CURRENT));
		views.setOnClickPendingIntent(R.id.btnPause_Provider, PendingIntent
				.getBroadcast(context, 2, new Intent(GlobalUtils.ACTION_PAUSE),
						PendingIntent.FLAG_UPDATE_CURRENT));
		views.setOnClickPendingIntent(R.id.btnNext_Provider, PendingIntent
				.getBroadcast(context, 3, new Intent(GlobalUtils.ACTION_NEXT),
						PendingIntent.FLAG_UPDATE_CURRENT));
		views.setOnClickPendingIntent(R.id.btnList_Provider, PendingIntent
				.getActivity(context, 5, new Intent(context,
						MusicPlayerActivity.class),
						PendingIntent.FLAG_UPDATE_CURRENT));
		appWidgetManager.updateAppWidget(appWidgetIds, views);
	}
}
