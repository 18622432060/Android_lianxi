package com.lp.douyu.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lp.douyu.R;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class CopyOfChannelActivity extends Activity {

	public static int showCurrent = 01;// 当前显示状态
	private static final int SHOW_EMPTY = 01;// 屏幕什么都没有
	private static final int SHOW_TOPANDBOTTOM = 02;// 只显示上下框
	private static final int SHOW_LV_RIGHT = 03;// 显示右边ListView

	private View flTop;
	private FrameLayout flBottom;
	private RelativeLayout rlMain;
	private ListView lvRight;
	private Button btnChannel;
	long lastTime = 0;
	List<String> list = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 必须设置在setContentView前
		setContentView(R.layout.channel_main);
		flTop = (View) findViewById(R.id.fl_top);
		flBottom = (FrameLayout) findViewById(R.id.fl_bottom);
		rlMain = (RelativeLayout) findViewById(R.id.rl_main);
		lvRight = (ListView) findViewById(R.id.lv_right);
		btnChannel = (Button) findViewById(R.id.btn_channel);

		for (int i = 0; i < 150; i++) {
			list.add("频道" + i);
		}
		lvRight.setAdapter(new MyAdapter());

		rlMain.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (showCurrent) {
				case SHOW_EMPTY:
					showCurrent = SHOW_TOPANDBOTTOM;
					ViewPropertyAnimator.animate(flTop).translationY(-flTop.getHeight()).setDuration(400).start();
					ViewPropertyAnimator.animate(flBottom).translationY(flBottom.getHeight()).setDuration(400).start();
					break;
				case SHOW_TOPANDBOTTOM:
					showCurrent = SHOW_EMPTY;
					ViewPropertyAnimator.animate(flTop).translationY(0).setDuration(400).start();
					ViewPropertyAnimator.animate(flBottom).translationY(0).setDuration(400).start();
					break;
				case SHOW_LV_RIGHT:
					showCurrent = SHOW_TOPANDBOTTOM;
					ViewPropertyAnimator.animate(flTop).translationY(0).setDuration(400).start();
					ViewPropertyAnimator.animate(flBottom).translationY(0).setDuration(400).start();
					ViewPropertyAnimator.animate(lvRight).translationX(lvRight.getWidth()).setDuration(300).start();
					break;
				default:
					break;
				}
				lastTime = System.currentTimeMillis();
				return false;
			}
		});
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					SystemClock.sleep(1000);
					if (System.currentTimeMillis() - lastTime > 2000) {
						runOnUiThread(new Runnable() {
							public void run() {
								if (showCurrent == SHOW_TOPANDBOTTOM) {
									ViewPropertyAnimator.animate(flTop).translationY(-flTop.getHeight()).setDuration(400).start();
									ViewPropertyAnimator.animate(flBottom).translationY(flBottom.getHeight()).setDuration(400).start();
								}
							}
						});
					}
				}
			}
		}).start();

		btnChannel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showCurrent = SHOW_LV_RIGHT;// 点击频道按钮后只显示右边频道列表
				ViewPropertyAnimator.animate(lvRight).translationX(0).setDuration(300).start();
				ViewPropertyAnimator.animate(flTop).translationY(-flTop.getHeight()).setDuration(400).start();
				ViewPropertyAnimator.animate(flBottom).translationY(flBottom.getHeight()).setDuration(400).start();
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		showCurrent = SHOW_EMPTY;// 第一次获取焦点后屏幕为空
		ViewPropertyAnimator.animate(flTop).translationY(-flTop.getHeight()).setDuration(400).start();
		ViewPropertyAnimator.animate(flBottom).translationY(flBottom.getHeight()).setDuration(400).start();
		ViewHelper.setTranslationX(lvRight, lvRight.getWidth());
		super.onWindowFocusChanged(hasFocus);
	}

	class MyAdapter extends BaseAdapter {

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(getApplicationContext());
			tv.setHeight(100);
			tv.setText("" + list.get(position));
			return tv;
		}

	}

}