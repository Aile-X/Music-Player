package com.aile.utils;

//ȫ�ֵĳ�����.
public class GlobalUtils {
	public static final String ACTION_LV_ITEM_PLAY= "com.tarena.action.LV_ITEM_PLAY";
	public static final String ACTION_PREVIOUS = "com.tarena.action.PREVIOUS";//��һ��
	public static final String ACTION_PLAY = "com.tarena.action.PLAY";//����
	public static final String ACTION_PAUSE = "com.tarena.action.PAUSE";//��ͣ
	public static final String ACTION_NEXT = "com.tarena.action.NEXT";//��һ��
	public static final String ACTION_STOP = "com.tarena.action.STOP";//ֹͣ
	public static final String ACTION_SEEK_TO = "com.tarena.action.SEEK_TO";//�û��϶��Ĺ�����λ��..
	public static final String ACTION_PLAYMODE_CHANGED = "com.tarena.action.PLAYMODE_CHANGED";//����ģʽ���(�����ѭ��)
	public static final String ACTION_GET_PLAY_STATE = "com.tarena.action.GET_PLAY_STATE";//����״̬(���Ż���ͣ)
	
	public static final String ACTION_CURRENT_MUSIC_CHANGED = "com.tarena.action.CURRENT_MUSIC_CHANGED";//��ǰ���ֱ��
	public static final String ACTION_UPDATE_PROGRESS = "com.tarena.action.UPDATE_PROGRESS";//���²��Ž���
	public static final String ACTION_UPDATE_STATE_CHANGED = "com.tarena.action.UPDATE_STATE_CHANGED";//����״̬���(�˳�������ٴν���ʱ��������ʱ����û�и���.)
	public static final String ACTION_PLAY_STATE = "com.tarena.action.PLAY_STATE";//�����˳��󷵻ؽ���ʱ�ܱ����ϴε�״̬.
	
	public static final String EXTRA_CURRENT_MUSIC = "currentMusic";
	public static final String EXTRA_PROGRESS = "progress";//�û��϶��Ľ���ֵ
	public static final String EXTRA_DURATION = "duration";//��ʱ��
	public static final String EXTRA_MUSIC_NAME = "musicName";
	public static final String EXTRA_PLAY_MODE = "playMod";
	public static final String EXTRA_UPDATE_STATE = "updateState";
	public static final String EXTRA_PLAY_STATE = "playState";//���Ż���ͣ
	
	public static final int PLAY_MODE_LOOP = 1;
	public static final int PLAY_MODE_RANDOM = 2;
	
	
	public static String formatTime(int time) {
		int min = time / 1000 / 60;//����
		int sec = time / 1000 % 60;//��
		return format(min) + ":" + format(sec);
	}

	private static String format(int value) {
		return value < 10 ? "0" + value : "" + value;
	}
}
