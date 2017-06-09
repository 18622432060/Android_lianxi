package com.itheima.baidumusic;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return new MusicBinder();
	}

	public void start() {
		System.out.println("start");
	}

	public void pause() {
		System.out.println("pause");
	}

	public void resume() {
		System.out.println("resume");
	}

	public class MusicBinder extends Binder implements IService {
		
		@Override
		public void callStart() {
			start();
		}

		@Override
		public void callPause() {
			pause();
		}

		@Override
		public void callResume() {
			resume();
		}
	}

}