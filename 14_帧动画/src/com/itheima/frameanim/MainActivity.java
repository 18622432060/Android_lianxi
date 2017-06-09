package com.itheima.frameanim;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
	
	int cancelFrames = 0;
	AnimationDrawable anim = null;
	ArrayList<Drawable> list = new ArrayList<Drawable>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ImageView rocketImage = (ImageView) findViewById(R.id.iv);
		Button btn = (Button) findViewById(R.id.btn);

		anim = new AnimationDrawable();
		for (int i = 1; i <= 11; i++) {
			int id = getResources().getIdentifier("girl_" + i, "drawable", getPackageName());
			Drawable drawable = getResources().getDrawable(id);
			anim.addFrame(drawable, 100);
			list.add(drawable);
		}
		anim.setOneShot(true);

		btn.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
//						ThreadManager.getThreadPool().execute(new Runnable() {
//							public void run() {
//								runOnUiThread(new Runnable() {
//									public void run() {
										rocketImage.setBackgroundDrawable(anim);
										anim.start();
//									}
//								});
//							}
//						});
						break;
					case MotionEvent.ACTION_MOVE:
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						cancelFrames = list.indexOf(anim.getCurrent());
						anim.stop();
//						ThreadManager.getThreadPool().execute(new Runnable() {
//							public void run() {
//								runOnUiThread(new Runnable() {
//									public void run() {
										AnimationDrawable anim = new AnimationDrawable();
										for (int i = cancelFrames; i > 0; i--) {
											int id = getResources().getIdentifier("girl_" + i, "drawable", getPackageName());
											Drawable drawable = getResources().getDrawable(id);
											anim.addFrame(drawable, 100);
										}
										anim.setOneShot(true);
										rocketImage.setBackgroundDrawable(anim);
										anim.start();
//									}
//								});
//							}
//						});
						break;
					default:
						break;
				}
				return false;
			}
		});
	}

}