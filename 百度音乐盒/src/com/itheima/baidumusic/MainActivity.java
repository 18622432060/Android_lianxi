package com.itheima.baidumusic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ��Ϸ�ʽ�������� ���䰸�����ֺй���
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends Activity {

	private IService iservice; // ����������Ƕ�����м��˶���
	private MyConn conn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);

		Intent intent = new Intent(this, MusicService.class);
		startService(intent);
		conn = new MyConn();
		bindService(intent, conn, BIND_AUTO_CREATE);
		
		// [1]�ҵ�iv�ؼ� ������ʾ����Ч��
		ImageView rocketImage = (ImageView) findViewById(R.id.iv);
		// [2]���ñ�����Դ
		rocketImage.setBackgroundResource(R.drawable.my_anim);
		// [3]��ȡAnimationDrawable ����
		AnimationDrawable ad = (AnimationDrawable) rocketImage.getBackground();
		// [4]��ʼִ�ж���
		ad.start();
	}

	public class MyConn implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			iservice = (IService) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	}

	@OnClick(R.id.btn_start)
	void start() {
		iservice.callStart();
	}

	@OnClick(R.id.btn_pause)
	void pause() {
		iservice.callPause();
	}

	@OnClick(R.id.btn_resume)
	void resume() {
		iservice.callResume();
	}

	@Override
	protected void onDestroy() {
		unbindService(conn);
		super.onDestroy();
	}

}