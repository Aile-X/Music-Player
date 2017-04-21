package com.aile.musicplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

import com.aile.entity.Music;
import com.aile.utils.GlobalUtils;

import java.io.IOException;
import java.util.Random;

//���ֲ��Ÿ�״̬������.
public class MusicPlayService extends Service {
	private MediaPlayer player;
	private boolean isPause;
	private MusicApplication app;// ��������.
	private int playMode;// ����ģʽ.
	private Random rand;// �������.
	private InnerReceiver receiver;
	private Thread thread;
	private boolean isLoop;
	private boolean needUpdate;

	public void listvewPlay() {
		if (isPause) {// ���������ͣ״̬�����������
			player.start();
		} else {
			int currentIndex = app.getCurrentIndex();// ��ǰ��������ֵ.
			if (currentIndex >= 0 && currentIndex < app.getMusics().size()) {
				try {
					player.reset();
					player.setDataSource(app.getMusics().get(currentIndex)
							.getMusicPath());
					player.prepare();
					player.start();

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// ������ͣ״̬��ʶΪfalse
		isPause = false;
		// �����߳�
		synchronized (thread) {
			thread.notify();
		}

		// ���Ͳ���״̬�㲥,�� pause()��Ĺ㲥���ʹ��,���ڰ�ť�ֵĲ�ͬ��ʾ.(����/��ͣ)
		// ���͵�MusicPlayerActivity�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
		Intent intent1 = new Intent(GlobalUtils.ACTION_PLAY_STATE);
		intent1.putExtra(GlobalUtils.EXTRA_PLAY_STATE, player.isPlaying());
		sendBroadcast(intent1);


	}

	// �����㲥:
	// ���Ͳ���״̬�㲥----4��
	// �������ֱ���㲥��Activity---1��
	// ���͸��½������㲥----1��.

	// �㲥:
	// 1:��ʼ���㲥������,ע�ᶯ̬�㲥
	// 2:sendBroadcast(intent),���㲥.
	// 3:���չ㲥,���̳�BroadcastReceiver,��дonReceive����.

	public void play() {
		if (isPause) {// ���������ͣ״̬�����������
			player.start();
		} else {
			int currentIndex = app.getCurrentIndex();// ��ǰ��������ֵ.
			if (currentIndex >= 0 && currentIndex < app.getMusics().size()) {
				try {
					// ����
					player.reset();
					player.setDataSource(app.getMusics().get(currentIndex)
							.getMusicPath());
					player.prepare();
					player.start();

					//���������������﷢����,���Խ����ŵ�������Ϣ���㲥���ݵ����������ʾ.
					// �������ֱ���㲥��Activity��MusicAppProvider,�����ֿⷢ���仯ʱ,���µ�MusicPlayerActivity
					// ���͵�MusicPlayerActivity�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
					Intent intent = new Intent(
							GlobalUtils.ACTION_CURRENT_MUSIC_CHANGED);
					intent.putExtra(GlobalUtils.EXTRA_CURRENT_MUSIC, app
							.getMusics().get(currentIndex));
					sendBroadcast(intent);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// ������ͣ״̬��ʶΪfalse
		isPause = false;
		// �����߳�
		synchronized (thread) {
			thread.notify();
		}

		// ���Ͳ���״̬�㲥,�� pause()��Ĺ㲥���ʹ��,���ڰ�ť�ֵĲ�ͬ��ʾ.(����/��ͣ)
		// ���͵�MusicPlayerActivity�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
		Intent intent1 = new Intent(GlobalUtils.ACTION_PLAY_STATE);
		intent1.putExtra(GlobalUtils.EXTRA_PLAY_STATE, player.isPlaying());
		sendBroadcast(intent1);
	}

	public void pause() {
		if (player.isPlaying()) {
			player.pause();// ��ͣ
			isPause = true;

			// ���Ͳ���״̬�㲥,��play()��Ĺ㲥���ʹ��,���ڰ�ť�ֵĲ�ͬ��ʾ.(����/��ͣ)
			// ���͵�MusicPlayerActivity�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
			Intent intent1 = new Intent(GlobalUtils.ACTION_PLAY_STATE);
			intent1.putExtra(GlobalUtils.EXTRA_PLAY_STATE, player.isPlaying());
			sendBroadcast(intent1);
		}
	}

	public void previous() {
		// ��ȡ��ǰ�������ֵ�����
		int currentIndex = app.getCurrentIndex();
		if (currentIndex != -1) {
			// ������һ�����ֵ�����
			switch (playMode) {
			case GlobalUtils.PLAY_MODE_LOOP:// ѭ��ģʽ
				if (--currentIndex < 0) {
					currentIndex = app.getMusics().size() - 1;
				}
				break;

			case GlobalUtils.PLAY_MODE_RANDOM:// ���ģʽ
				currentIndex = rand.nextInt(app.getMusics().size());
				break;
			}
			// ��������
			app.setCurrentIndex(currentIndex);
			// ����
			play();

			isPause = false;
		}
	}

	public void next() {
		// ��ȡ��ǰ�������ֵ�����
		int currentIndex = app.getCurrentIndex();
		if (currentIndex != -1) {
			// ������һ�����ֵ�����
			switch (playMode) {
			case GlobalUtils.PLAY_MODE_LOOP:// ѭ��ģʽ
				if (++currentIndex == app.getMusics().size()) {
					currentIndex = 0;
				}
				break;

			case GlobalUtils.PLAY_MODE_RANDOM:// ���ģʽ
				currentIndex = rand.nextInt(app.getMusics().size());
				break;
			}
			// ��������
			app.setCurrentIndex(currentIndex);
			// ����
			play();

			isPause = false;
		}
	}

	public void seekTo(int position) {
		player.seekTo(position);
		player.start();
		synchronized (thread) {
			thread.notify();
		}
		// ���Ͳ���״̬�㲥
		// ���͵�MusicPlayerActivity�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
		Intent intent1 = new Intent(GlobalUtils.ACTION_PLAY_STATE);
		intent1.putExtra(GlobalUtils.EXTRA_PLAY_STATE, player.isPlaying());
		sendBroadcast(intent1);
	}

	public void onCreate() {
		super.onCreate();

		isPause = false;// ��ͣ״̬��ʶ����
		// ��ȡӦ�ó������
		app = (MusicApplication) getApplication();
		// ����ģʽ
		playMode = GlobalUtils.PLAY_MODE_LOOP;
		rand = new Random();
		// ����������
		player = new MediaPlayer();
		try {
			player.setDataSource(app.getMusics().get(app.getCurrentIndex())
                    .getMusicPath());
			player.prepare();
			player.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		player.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				next();// �����ֲ�����ɣ�������һ��
			}
		});

		// ���������̣߳�ÿ����²��Ž���,�����ý���Ĺ�����������.
		isLoop = true;
		needUpdate = true;
		thread = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isLoop) {
					if (needUpdate && player.isPlaying()) {

						// ���͸��½������㲥.�ù�����������.�����﷢�㲥����Ϊ��������Ĺ������ƶ����������﷢����.
						// ���͵�MusicPlayerActivity�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
						Intent intent = new Intent(
								GlobalUtils.ACTION_UPDATE_PROGRESS);
						intent.putExtra(GlobalUtils.EXTRA_PROGRESS, player
								.getCurrentPosition());
						intent.putExtra(GlobalUtils.EXTRA_DURATION, player
								.getDuration());
						intent.putExtra(GlobalUtils.EXTRA_MUSIC_NAME, app
								.getMusics().get(app.getCurrentIndex())
								.getName());
						sendBroadcast(intent);

						try {
							sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						synchronized (this) {
							try {
								wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		};
		thread.start();

		// ��ʼ���㲥������
		// ע�ᶯ̬�㲥
		receiver = new InnerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalUtils.ACTION_PLAY);
		filter.addAction(GlobalUtils.ACTION_PREVIOUS);
		filter.addAction(GlobalUtils.ACTION_PAUSE);
		filter.addAction(GlobalUtils.ACTION_NEXT);
		filter.addAction(GlobalUtils.ACTION_PLAYMODE_CHANGED);//����ģʽ.
		filter.addAction(GlobalUtils.ACTION_SEEK_TO);//�û��϶��Ĺ�����λ��.
		filter.addAction(GlobalUtils.ACTION_UPDATE_STATE_CHANGED);// ����״̬���(��������ʱ���ĸ���.)
		filter.addAction(GlobalUtils.ACTION_GET_PLAY_STATE);//���Ż���ͣ�����ʾ.
		filter.addAction(GlobalUtils.ACTION_LV_ITEM_PLAY);
		registerReceiver(receiver, filter);
	}

	// ��������activity�Ĺ㲥, �Զ����ڲ��㲥��,ִ�в�����10s,����ANR;�㲥���մ�����.
	private class InnerReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (GlobalUtils.ACTION_PLAY.equals(action)) {
				play();
			} else if (GlobalUtils.ACTION_PAUSE.equals(action)) {
				pause();
			} else if (GlobalUtils.ACTION_PREVIOUS.equals(action)) {
				previous();
			} else if (GlobalUtils.ACTION_NEXT.equals(action)) {
				next();
			}else if (GlobalUtils.ACTION_LV_ITEM_PLAY.equals(action)){
				Music music = (Music) intent.getSerializableExtra(GlobalUtils.EXTRA_CURRENT_MUSIC);
				try {
					if (player.isPlaying()){
						player.pause();

					}else {

						player.setDataSource(music.getMusicPath());
						player.prepare();
						player.start();
					}


				} catch (IOException e) {
					e.printStackTrace();
				}

			} else if (GlobalUtils.ACTION_SEEK_TO.equals(action)) {
				if (isPause || player.isPlaying()) {
					int position = intent.getIntExtra(
							GlobalUtils.EXTRA_PROGRESS, -1);
					if (position != -1) {
						seekTo(position * player.getDuration() / 100);// ��λ���û��϶���������λ�ú󲥷�.
					}
				}
			} else if (GlobalUtils.ACTION_PLAYMODE_CHANGED.equals(action)) {
				playMode = intent.getIntExtra(GlobalUtils.EXTRA_PLAY_MODE,
						GlobalUtils.PLAY_MODE_LOOP);// Ĭ��ѭ������.
			} else if (GlobalUtils.ACTION_UPDATE_STATE_CHANGED.equals(action)) {
				needUpdate = intent.getBooleanExtra(
						GlobalUtils.EXTRA_UPDATE_STATE, true);//������,ʱ��,������ͣ��ť��״̬����.����Ϊtrue,������Ҫ����.
				synchronized (thread) {
					thread.notify();
				}
			} else if (GlobalUtils.ACTION_GET_PLAY_STATE.equals(action)) {//���ǻػ��㲥.
				// ���Ͳ���״̬�㲥,���ֻ��ڲ���,�����˳��������,���Ű�ťû����.
				// ���͵�MusicPlayerActivity�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
				Intent intent1 = new Intent(GlobalUtils.ACTION_PLAY_STATE);
				intent1.putExtra(GlobalUtils.EXTRA_PLAY_STATE, player
						.isPlaying());
				sendBroadcast(intent1);
			}
		}
	}

	public void onDestroy() {
		super.onDestroy();
		player.release();// �ͷŲ�����ʵ��
		unregisterReceiver(receiver);// ע���㲥
		isLoop = false;
		synchronized (thread) {
			thread.notify();
		}
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

}
