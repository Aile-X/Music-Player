package com.aile.musicplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.aile.adapter.MusicAdapter;
import com.aile.biz.MusicBiz;
import com.aile.entity.Music;
import com.aile.utils.GlobalUtils;

import java.util.ArrayList;

public class MusicPlayerActivity extends Activity {
	private ListView lvMusics;
	private TextView tvName, tvDuration, tvProgress;
	private Button btnPlay;
	private SeekBar sbProgress;
	private MusicApplication app;
	private MusicAdapter adapter;
	private MediaPlayer player;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	// ��ʼ�������.
	private void setupView() {
		//����listview��ͼ.
		lvMusics = (ListView) findViewById(R.id.lvMusics);
		adapter = new MusicAdapter(this, app.getMusics());
		lvMusics.setAdapter(adapter);

		tvName = (TextView) findViewById(R.id.tvMusicName);
		tvProgress = (TextView) findViewById(R.id.tvCurrentPosition);
		tvDuration = (TextView) findViewById(R.id.tvDuration);

		sbProgress = (SeekBar) findViewById(R.id.sbMusicProgress);

		btnPlay = (Button) findViewById(R.id.btnPlay);

		// ��ʼ���Ի���.
		Builder builder = new Builder(this);
		dialog = builder.setTitle("��ʾ").setIcon(R.drawable.ic_launcher)
				.setMessage("����ɨ��sdCard,���Ժ�...").setCancelable(false).create();
	}

	// ������������.
	public void addLitener() {
		sbProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
										  boolean fromUser) {
				if (fromUser) {
					// ���͹㲥,������������϶����û����������͹㲥����ָ�����Ȳ���,�϶��¼����û��϶�����,���������﷢�㲥.
					// ���͵�MusicPlayService�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
					Intent intent = new Intent(GlobalUtils.ACTION_SEEK_TO);
					intent.putExtra(GlobalUtils.EXTRA_PROGRESS, progress);//��Ҫ���������,����ָ�û��϶��Ľ���ֵ
					sendBroadcast(intent);
				}
			}
		});



//		lvMusics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			public void onItemClick(
//					AdapterView<?> parent,
//					View view,
//					int position,
//					long id) {
//				Music music = (Music) parent.getItemAtPosition(position);
//				Intent intent = new Intent(GlobalUtils.ACTION_LV_ITEM_PLAY);
//				intent.putExtra(GlobalUtils.EXTRA_CURRENT_MUSIC, music);
//				sendBroadcast(intent);
//				}
//		});
	}

	// ���û������ť�¼��ù㲥��MusicAppProvider��MusicPlayService���͹㲥
	public void doClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
			case R.id.btnPrevious:// ��һ��
				intent.setAction(GlobalUtils.ACTION_PREVIOUS);
				break;
			case R.id.btnPlay:// ���Ż���ͣ
				String text = btnPlay.getText().toString();
				if ("��  ��".equals(text)) {
					intent.setAction(GlobalUtils.ACTION_PLAY);
				} else if ("��  ͣ".equals(text)) {
					Log.i("info", text);
					intent.setAction(GlobalUtils.ACTION_PAUSE);
				}
				break;
			case R.id.btnNext:// ��һ��
				intent.setAction(GlobalUtils.ACTION_NEXT);
				break;
		}
		// ���͵�MusicPlayService�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
		sendBroadcast(intent);
	}

	// ͨ��layoutxml�����������˵�,���ϵͳ�˵��������Ĳ˵�.xml�ļ�Ϊoptions.xml
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.options, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// ϵͳ�˵��������Ĳ˵��¼�����.���͹㲥��MusicPlayService
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {//����ģʽ�����û�ѡ���,���������﷢�㲥.
			case R.id.sub_menu_loop:// ѭ������
				intent.setAction(GlobalUtils.ACTION_PLAYMODE_CHANGED);
				intent.putExtra(GlobalUtils.EXTRA_PLAY_MODE,
						GlobalUtils.PLAY_MODE_LOOP);
				break;
			case R.id.sub_menu_random:// �������
				intent.setAction(GlobalUtils.ACTION_PLAYMODE_CHANGED);
				intent.putExtra(GlobalUtils.EXTRA_PLAY_MODE,
						GlobalUtils.PLAY_MODE_RANDOM);
				break;
			case R.id.menu_opts_flush:// ˢ��
				intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
				intent.setData(Uri.parse("file://"
						+ Environment.getExternalStorageDirectory()));
				break;
			case R.id.menu_opts_exit:// �˳�
				intent = new Intent(this, MusicPlayService.class);
				stopService(intent);
				intent = new Intent(GlobalUtils.ACTION_STOP);
				finish();
				break;
		}
		// ���͵�MusicPlayService��MusicAppProvider�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
		sendBroadcast(intent);
		return super.onOptionsItemSelected(item);
	}

	// ����һ������������ʶ�Ƿ��˳�
	private static boolean isExit = false;

	Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
					Toast.LENGTH_SHORT).show();
			// ����handler�ӳٷ��͸���״̬��Ϣ
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(this, MusicPlayService.class);
			stopService(intent);
			finish();
		}
	}

	// ʵ����������.
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// ��ȡapp��������,�����������.
		app = (MusicApplication) getApplication();
		player = new MediaPlayer();
		// �����ʼ��
		setupView();
		addLitener();

		// ����Service
		Intent intent = new Intent(this, MusicPlayService.class);
		startService(intent);// ��������.

		// �����㲥����������� filter����,ϵͳ�㲥,Ϊ��ˢ��---------��̬ע��
		receiver = new InnerReceiver();
		filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		filter.addDataScheme("file");

		// ���ֲ�����ع㲥,����û��Э��,�Զ���㲥------��̬ע��
		receiver1 = new InnerReceiver();
		filter1 = new IntentFilter();
		filter1.addAction(GlobalUtils.ACTION_CURRENT_MUSIC_CHANGED);
		filter1.addAction(GlobalUtils.ACTION_UPDATE_PROGRESS);
		filter1.addAction(GlobalUtils.ACTION_PLAY_STATE);
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	// ע�᱾�ع㲥�ͷ��͹㲥,���˳�ʱ��д�÷���.
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, filter);
		registerReceiver(receiver1, filter1);

		// ���͸���״̬����㲥,���ֻ��ڲ���,�����˳��������,��������ʱ��û����.����Ϊtrue,��ִ�н������.
		// ���͵�MusicPlayService�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
		Intent intent = new Intent(GlobalUtils.ACTION_UPDATE_STATE_CHANGED);
		intent.putExtra(GlobalUtils.EXTRA_UPDATE_STATE, true);
		sendBroadcast(intent);

		// ���ͻ�ȡ���󲥷�״̬�Ĺ㲥,�����˳���,�ܱ�����ǰ�İ�ť��״̬,���Ż���ͣ,�ػ��㲥
		// ���͵�MusicPlayService�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
		intent = new Intent(GlobalUtils.ACTION_GET_PLAY_STATE);
		sendBroadcast(intent);
	}

	// ���ٱ��ع㲥�ͷ��͹㲥,����ͣ�˳����淵�غ���д�÷���.
	// ��һ��activity������ͣ״̬�ǲ������intents��,����������Ҳ���Լ�Сϵͳ����Ҫ�Ŀ���
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
		unregisterReceiver(receiver1);

		// ���͸���״̬����㲥,���ֻ��ڲ���,�����˳��������,���²��Ű�ť��״̬,����ͣʱ(false).�����ǲ���Ҫ���½���.
		// ���͵�MusicPlayService�е�onCreate()�����еĶ�̬ע��㲥������InnerReceiver�ﴦ��
		Intent intent = new Intent(GlobalUtils.ACTION_UPDATE_STATE_CHANGED);
		intent.putExtra(GlobalUtils.EXTRA_UPDATE_STATE, false);
		sendBroadcast(intent);
	}

	private AlertDialog dialog;
	private InnerReceiver receiver, receiver1;
	private IntentFilter filter, filter1;

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	public Action getIndexApiAction() {
		Thing object = new Thing.Builder()
				.setName("MusicPlayer Page") // TODO: Define a title for the content shown.
				// TODO: Make sure this auto-generated URL is correct.
				.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
				.build();
		return new Action.Builder(Action.TYPE_VIEW)
				.setObject(object)
				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
				.build();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		AppIndex.AppIndexApi.start(client, getIndexApiAction());
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		AppIndex.AppIndexApi.end(client, getIndexApiAction());
		client.disconnect();
	}

	// ���չ㲥������㲥�¼�.
	private class InnerReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {// ��ʼɨ��sdcard
				dialog.show();
			} else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {// sdcardɨ�����
				dialog.dismiss();
				// ��ȡ����Ĳ����б���Ϣ
				ArrayList<Music> musics = new MusicBiz(MusicPlayerActivity.this)
						.getLocalMusics();
				app.setMusics(musics);// ���¹���Ĳ����б�����,��������.
				adapter.changeDataSet(musics);// ���½���
			} else if (GlobalUtils.ACTION_CURRENT_MUSIC_CHANGED.equals(action)) {
				// ��ȡ��ǰ���ŵ�������Ϣ
				Music music = (Music) intent
						.getSerializableExtra(GlobalUtils.EXTRA_CURRENT_MUSIC);
				tvName.setText(music.getName());// ����������ʾ��Ϣ
			} else if (GlobalUtils.ACTION_UPDATE_PROGRESS.equals(action)) {
				// ��ȡ ��ǰ���� ��ʱ�� ��������
				int duration = intent.getIntExtra(GlobalUtils.EXTRA_DURATION,
						-1);// ��ʱ��
				int progress = intent.getIntExtra(GlobalUtils.EXTRA_PROGRESS,
						-1);// ��ǰ����
				String name = intent
						.getStringExtra(GlobalUtils.EXTRA_MUSIC_NAME);// ������
				// �����ı����е�����
				tvName.setText(name);
				// �����ı����е�����
				tvProgress.setText(GlobalUtils.formatTime(progress));
				tvDuration.setText(GlobalUtils.formatTime(duration));
				sbProgress.setProgress(progress * 100 / duration);// ���ý�����
			} else if (GlobalUtils.ACTION_PLAY_STATE.endsWith(action)) {
				// ���ڰ�ť֮���ı��л�
				boolean playState = intent.getBooleanExtra(
						GlobalUtils.EXTRA_PLAY_STATE, false);//����״̬Ĭ����ͣ��
				if (playState) {
					btnPlay.setText("��  ͣ");
				} else {
					btnPlay.setText("��  ��");
				}
			}
		}
	}

}